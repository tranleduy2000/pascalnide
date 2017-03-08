package com.js.interpreter.ast.expressioncontext;

import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.NoSuchFunctionOrVariableException;
import com.duy.interpreter.exceptions.NonConstantExpressionException;
import com.duy.interpreter.exceptions.OverridingFunctionException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.SameNameException;
import com.duy.interpreter.exceptions.UnrecognizedTokenException;
import com.duy.interpreter.pascaltypes.DeclaredType;
import com.duy.interpreter.tokens.OperatorToken;
import com.duy.interpreter.tokens.OperatorTypes;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.WordToken;
import com.duy.interpreter.tokens.basic.ConstToken;
import com.duy.interpreter.tokens.basic.FunctionToken;
import com.duy.interpreter.tokens.basic.ProcedureToken;
import com.duy.interpreter.tokens.basic.TypeToken;
import com.duy.interpreter.tokens.basic.VarToken;
import com.duy.interpreter.tokens.grouping.BeginEndToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.VariableAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExpressionContextMixin extends
		HeirarchicalExpressionContext {
    private final ListMultimap<String, AbstractFunction> callable_functions;
    public Map<String, ConstantDefinition> constants = new HashMap<String, ConstantDefinition>();
    public List<VariableDeclaration> UnitVarDefs = new ArrayList<VariableDeclaration>();
    Map<String, DeclaredType> typedefs = new HashMap<String, DeclaredType>();

	public ExpressionContextMixin(CodeUnit root, ExpressionContext parent) {
		this(root, parent, (ListMultimap) ArrayListMultimap.create());
	}
	public ExpressionContextMixin(CodeUnit root, ExpressionContext parent,
			ListMultimap<String, AbstractFunction> callable_functions) {
		super(root, parent);
		this.callable_functions = callable_functions;
	}

	public FunctionDeclaration getExistingFunction(FunctionDeclaration f)
			throws ParsingException {
		for (AbstractFunction g : callable_functions.get(f.name)) {
			if (f.headerMatches(g)) {
				if (!(g instanceof FunctionDeclaration)) {
					throw new OverridingFunctionException(g, f);
				}
				return (FunctionDeclaration) g;
			}
		}
		callable_functions.put(f.name, f);
		return f;
	}

	@Override
	public ReturnsValue getIdentifierValue(WordToken name)
			throws ParsingException {
		if (functionExistsLocal(name.name)) {
			return FunctionCall.generate_function_call(name,
					new ArrayList<ReturnsValue>(0), this);
		} else if (getConstantDefinitionLocal(name.name) != null) {
			return new ConstantAccess(getConstantDefinition(name.name)
					.getValue(), name.lineInfo);
		} else if (getVariableDefinitionLocal(name.name) != null) {
			return new VariableAccess(name.name, name.lineInfo);
		}
		if (parent == null) {
			throw new NoSuchFunctionOrVariableException(name.lineInfo,
					name.name);
		}
		return parent.getIdentifierValue(name);
	}

	public void verifyNonConflictingSymbolLocal(NamedEntity ne)
			throws SameNameException {
		String n = ne.name();
		if (functionExistsLocal(n)) {
			throw new SameNameException(getCallableFunctionsLocal(ne.name())
					.get(0), ne);
		} else if (getVariableDefinitionLocal(n) != null) {
			throw new SameNameException(getVariableDefinitionLocal(n), ne);
		} else if (getConstantDefinitionLocal(n) != null) {
			throw new SameNameException(getConstantDefinitionLocal(n), ne);
		}

	}

	public void add_next_declaration(GrouperToken i) throws ParsingException {
		Token next = i.peek();
		if (next instanceof ProcedureToken || next instanceof FunctionToken) {
			i.take();
			boolean is_procedure = next instanceof ProcedureToken;
			FunctionDeclaration declaration = new FunctionDeclaration(this, i,
					is_procedure);
			declaration = getExistingFunction(declaration);
			declaration.parse_function_body(i);
		} else if (next instanceof BeginEndToken) {
			handleBeginEnd(i);
		} else if (next instanceof VarToken) {
			i.take();
			List<VariableDeclaration> d = i.get_variable_declarations(this);
			for (VariableDeclaration dec : d) {
				declareVariable(dec);
			}
		} else if (next instanceof ConstToken) {
			i.take();
			addConstDeclarations(i);
		} else if (next instanceof TypeToken) {
			i.take();
			while (i.peek() instanceof WordToken) {
				String name = i.next_word_value();
				next = i.take();
				if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
					throw new ExpectedTokenException("=", next);
				}
				typedefs.put(name, i.get_next_pascal_type(this));
				i.assert_next_semicolon();
			}
		} else {
			handleUnrecognizedDeclaration(i.take(), i);
		}
	}

	protected abstract void handleBeginEnd(GrouperToken i) throws ParsingException;

	public VariableDeclaration getVariableDefinitionLocal(String ident) {
		for (VariableDeclaration v : UnitVarDefs) {
			if (v.name.equals(ident)) {
				return v;
			}
		}
		return null;
	}

	public List<AbstractFunction> getCallableFunctionsLocal(String name) {
		return callable_functions.get(name);
	}

	public boolean functionExistsLocal(String name) {
		return callable_functions.containsKey(name);
	}

	public ConstantDefinition getConstantDefinitionLocal(String ident) {
		return constants.get(ident);
	}

	public DeclaredType getTypedefTypeLocal(String ident) {
		return typedefs.get(ident);
	}

	public void declareTypedef(String name, DeclaredType type) {
		typedefs.put(name, type);
	}

	public void declareVariable(VariableDeclaration v) {
		UnitVarDefs.add(v);
	}

	public void declareFunction(FunctionDeclaration f) {
		callable_functions.put(f.name, f);
	}

	public void declareConst(ConstantDefinition c) {
		constants.put(c.name(), c);
	}

	protected void addConstDeclarations(GrouperToken i) throws ParsingException {
		while (i.peek() instanceof WordToken) {
			WordToken constname = (WordToken) i.take();
			String n = constname.name;
			Token equals = i.take();
			if (!(equals instanceof OperatorToken)
					|| ((OperatorToken) equals).type != OperatorTypes.EQUALS) {
				throw new ExpectedTokenException("=", constname);
			}
			ReturnsValue value = i.getNextExpression(this);
			Object comptimeval = value.compileTimeValue(this);
			if (comptimeval == null) {
				throw new NonConstantExpressionException(value);
			}
			ConstantDefinition newdef = new ConstantDefinition(constname.name,
					comptimeval, constname.lineInfo);
			verifyNonConflictingSymbol(newdef);
			this.constants.put(constname.name, newdef);
			i.assert_next_semicolon();
		}
	}

	@Override
	public CodeUnit root() {
		return root;
	}

	@Override
	public Executable handleUnrecognizedStatement(Token next,
			GrouperToken container) throws ParsingException {
		ParsingException e;
		try {
			Executable result = handleUnrecognizedStatementImpl(next, container);
			if (result != null) {
				return result;
			}
		} catch (ParsingException ex) {
			e = ex;
		}

		Executable result = parent == null ? null : parent
				.handleUnrecognizedStatement(next, container);
		if (result == null) {
			throw new UnrecognizedTokenException(next);
		}
		return result;
	}

	protected abstract Executable handleUnrecognizedStatementImpl(Token next,
			GrouperToken container) throws ParsingException;

	@Override
	public boolean handleUnrecognizedDeclaration(Token next,
			GrouperToken container) throws ParsingException {
		boolean result = handleUnrecognizedDeclarationImpl(next, container)
				|| (parent != null && parent.handleUnrecognizedDeclaration(
						next, container));
		if (!result) {
			throw new UnrecognizedTokenException(next);
		}
		return result;
	}

	protected abstract boolean handleUnrecognizedDeclarationImpl(Token next,
			GrouperToken container) throws ParsingException;

}
