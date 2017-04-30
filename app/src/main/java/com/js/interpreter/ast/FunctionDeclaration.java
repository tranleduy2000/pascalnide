package com.js.interpreter.ast;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.OverridingFunctionException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.SameNameException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.ForwardToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
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
    /**
     * name of function or procedure
     */
    public String name;
    public Executable instructions;
    /**
     * this is the store class of function
     */
    public VariableDeclaration result_definition;

    public LineInfo line;

    /* These go together ----> */
    public String[] argument_names;

    public RuntimeType[] argument_types;
    private boolean isProcedure = false;
    private boolean bodyDeclared;

    public FunctionDeclaration(ExpressionContext parent, GrouperToken i,
                               boolean is_procedure) throws ParsingException {
        this.declarations = new FunctionExpressionContext(this, parent);
        this.line = i.peek().lineInfo;
        this.isProcedure = is_procedure;
        name = i.next_word_value();

        getArgumentsForDeclaration(i, is_procedure);
        Token next = i.peek();
        if (is_procedure == next instanceof ColonToken) {
            throw new ParsingException(next.lineInfo,
                    "Functions must have a return operator, and procedures cannot have one");
        }
        if (!is_procedure) {
            i.take();
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
        this.declarations = new FunctionExpressionContext(this, p);
        this.argument_names = new String[0];
        this.argument_types = new RuntimeType[0];
    }

    public String getName() {
        return name;
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
            while (!bodyDeclared) {
                declarations.addNextDeclaration(i);
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public Object call(VariableContext parentcontext,
                       RuntimeExecutable<?> main, Object[] arguments)
            throws RuntimePascalException {
        if (this.declarations.root() instanceof Library) {
            parentcontext = main.getLibrary((Library) declarations.root());
        }
        return new FunctionOnStack(parentcontext, main, this, arguments)
                .execute();
    }

    private void getArgumentsForDeclaration(GrouperToken i, boolean is_procedure)
            throws ParsingException { // need
        List<WordToken> names_list = new ArrayList<>();
        List<RuntimeType> types_list = new ArrayList<>();
        Token next = i.peek();
        if (next instanceof ParenthesizedToken) {
            ParenthesizedToken arguments_token = (ParenthesizedToken) i.take();
            while (arguments_token.hasNext()) {
                int j = 0; // counts number added of this operator
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
            // TODO: 30-Apr-17
//            declarations.declareVariable(new VariableDeclaration(n.name, argument_types[j].declType, n.lineInfo));
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
                        .println("Warning: Overriding previously declared return operator for function "
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

    public boolean isProcedure() {
        return isProcedure;
    }

    private class FunctionExpressionContext extends ExpressionContextMixin {
        FunctionDeclaration function;

        public FunctionExpressionContext(FunctionDeclaration function, ExpressionContext parent) {
            super(parent.root(), parent);
            this.function = function;
        }

        @Override
        public Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            return null;
        }

        @Override
        public boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
                throws ParsingException {
            if (next instanceof ForwardToken) {
                container.assert_next_semicolon();
                bodyDeclared = true;
                return true;
            }
            return false;
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            bodyDeclared = true;
            instructions = i.get_next_command(declarations);
            i.assert_next_semicolon();
        }

        @Override
        public VariableDeclaration getVariableDefinitionLocal(String ident) {
            VariableDeclaration unit_variable_decl = super.getVariableDefinitionLocal(ident);
            if (unit_variable_decl != null) {
                return unit_variable_decl;
            }
            for (int i = 0; i < argument_names.length; i++) {
                if (argument_names[i].equals(ident)) {
                    return new VariableDeclaration(argument_names[i], argument_types[i].declType, function.line);
                }
            }
            return null;
        }
    }

}
