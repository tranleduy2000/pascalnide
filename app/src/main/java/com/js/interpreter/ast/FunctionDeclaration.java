package com.js.interpreter.ast;

import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.OverridingFunctionException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.SameNameException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArgumentType;
import com.duy.interpreter.pascaltypes.DeclaredType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.WordToken;
import com.duy.interpreter.tokens.basic.ColonToken;
import com.duy.interpreter.tokens.basic.CommaToken;
import com.duy.interpreter.tokens.basic.ForwardToken;
import com.duy.interpreter.tokens.basic.SemicolonToken;
import com.duy.interpreter.tokens.basic.VarToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.ParenthesizedToken;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionDeclaration extends AbstractCallableFunction {
	final public ExpressionContextMixin declarations;
	public String name;

	public Executable instructions;

	public VariableDeclaration result_definition;

	public LineInfo line;
	public String[] argument_names;

	public RuntimeType[] argument_types;
	private boolean body_declared;

	public FunctionDeclaration(ExpressionContext parent, GrouperToken i,
			boolean is_procedure) throws ParsingException {
		this.declarations = new FunctionExpressionContext(parent);
		this.line = i.peek().lineInfo;
		name = i.next_word_value();

		get_arguments_for_declaration(i, is_procedure);
		Token next = i.peek();
		if (is_procedure == next instanceof ColonToken) {
			throw new ParsingException(next.lineInfo,
					"Functions must have a return type, and procedures cannot have one");
		}
		if (!is_procedure) {
			i.take();
//			result_definition = new VariableDeclaration("result",
// i.get_next_pascal_type(declarations), line);

			//define variable result of function, the name of variable same as name function
			result_definition = new VariableDeclaration(name,
					i.get_next_pascal_type(declarations), line);
			this.declarations.declareVariable(result_definition);
		}

		i.assert_next_semicolon();

		instructions = null;
		NamedEntity n = parent.getConstantDefinition(name);
		if (n != null) {
			throw new SameNameException(n, this);
		}
		n = parent.getVariableDefinition(name);
		if (n != null) {
			throw new SameNameException(n, this);
		}
	}

	public FunctionDeclaration(ExpressionContext p) {
		this.declarations = new FunctionExpressionContext(p);
		this.argument_names = new String[0];
		this.argument_types = new RuntimeType[0];
	}

	public void parse_function_body(GrouperToken i) throws ParsingException {

		Token next = i.peek_no_EOF();
		if (next instanceof ForwardToken) {
			i.take();
			i.assert_next_semicolon();
		} else {
			if (instructions != null) {
				throw new OverridingFunctionException(this, i.lineInfo);
			}
			while (!body_declared) {
				declarations.add_next_declaration(i);
			}
		}
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Object call(VariableContext parentContext,
			RuntimeExecutable<?> main, Object[] arguments)
			throws RuntimePascalException {
		if (this.declarations.root() instanceof Library) {
			parentContext = main.getLibrary((Library) declarations.root());
		}
		return new FunctionOnStack(parentContext, main, this, arguments)
				.execute();
	}

	private void get_arguments_for_declaration(GrouperToken i,
			boolean is_procedure) throws ParsingException { // need
		List<WordToken> names_list = new ArrayList<WordToken>();
		List<RuntimeType> types_list = new ArrayList<RuntimeType>();
		Token next = i.peek();
		if (next instanceof ParenthesizedToken) {
			ParenthesizedToken arguments_token = (ParenthesizedToken) i.take();
			while (arguments_token.hasNext()) {
				int j = 0; // counts number added of this type
				next = arguments_token.take();
				boolean is_varargs = false;
				if (next instanceof VarToken) {
					is_varargs = true;
					next = arguments_token.take();
				}
				while (true) {
					names_list.add((WordToken) next);
					j++;
					next = arguments_token.take();
					if (next instanceof CommaToken) {
						next = arguments_token.take();
					} else {
						break;
					}
				}

				if (!(next instanceof ColonToken)) {
					throw new ExpectedTokenException(":", next);
				}
				DeclaredType type;
				type = arguments_token.get_next_pascal_type(declarations);

				while (j > 0) {
					types_list.add(new RuntimeType(type, is_varargs));
					j--;
				}
				if (arguments_token.hasNext()) {
					next = arguments_token.take();
					if (!(next instanceof SemicolonToken)) {
						throw new ExpectedTokenException(";", next);
					}
				}
			}
		}
		argument_types = types_list.toArray(new RuntimeType[types_list.size()]);
		argument_names = new String[names_list.size()];
		for (int j = 0; j < argument_names.length; j++) {
			WordToken n = names_list.get(j);
			argument_names[j] = n.name;
			declarations.declareVariable(new VariableDeclaration(n.name,
					argument_types[j].declType, n.lineInfo));
		}

	}

	@Override
	public ArgumentType[] argumentTypes() {
		return argument_types;
	}

	@Override
	public DeclaredType return_type() {
		return result_definition == null ? null : result_definition.type;
	}

	public boolean headerMatches(AbstractFunction other)
			throws ParsingException {
		if (name.equals(other.name())
				&& Arrays.equals(argument_types, other.argumentTypes())) {
			if (result_definition == null && other.return_type() == null) {
				return true;
			}
			if (result_definition == null || other.return_type() == null
					|| !result_definition.equals(other.return_type())) {
				System.err
						.println("Warning: Overriding previously declared return type for function "
								+ name);
			}
			return true;
		}
		return false;
	}

	@Override
	public String getEntityType() {
		return "function";
	}

	@Override
	public LineInfo getLineNumber() {
		return line;
	}

	private class FunctionExpressionContext extends ExpressionContextMixin {

		public FunctionExpressionContext(ExpressionContext parent) {
			super(parent.root(), parent);
		}

		@Override
		public Executable handleUnrecognizedStatementImpl(Token next,
														  GrouperToken container) throws ParsingException {
			return null;
		}

		@Override
		public boolean handleUnrecognizedDeclarationImpl(Token next,
														 GrouperToken container) throws ParsingException {
			if (next instanceof ForwardToken) {
				container.assert_next_semicolon();
				body_declared = true;
				return true;
			}
			return false;
		}

		@Override
		public void handleBeginEnd(GrouperToken i) throws ParsingException {
			body_declared = true;
			instructions = i.get_next_command(declarations);
			i.assert_next_semicolon();
		}
	}

}
