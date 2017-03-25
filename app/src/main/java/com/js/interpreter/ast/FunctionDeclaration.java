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
    public VariableDeclaration resultDefinition;
    public LineInfo line;
    public String[] argument_names;
    public RuntimeType[] argument_types;
    private boolean isProcedure = false;
    private boolean bodyDeclared;

    public FunctionDeclaration(ExpressionContext parent, GrouperToken i, boolean isProcedure)
            throws ParsingException {
        this.declarations = new FunctionExpressionContext(parent);
        this.line = i.peek().lineInfo;
        this.isProcedure = isProcedure;
        name = i.next_word_value();

        get_arguments_for_declaration(i, isProcedure);
        Token next = i.peek();
        if (isProcedure == next instanceof ColonToken) {
            throw new ParsingException(next.lineInfo,
                    "Functions must have a return type, and procedures cannot have one");
        }
        if (!isProcedure) {
            i.take();
            //define variable result of function, the name of variable same as name function
            resultDefinition = new VariableDeclaration(name, i.getNextPascalType(declarations), line);
            this.declarations.declareVariable(resultDefinition);
        }

        i.assertNextSemicolon();

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

    public String getName() {
        return name;
    }

    public void parseFunctionBody(GrouperToken i) throws ParsingException {

        Token next = i.peek_no_EOF();
        if (next instanceof ForwardToken) {
            i.take();
            i.assertNextSemicolon();
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
                type = arguments_token.getNextPascalType(declarations);

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
        return resultDefinition == null ? null : resultDefinition.type;
    }

    public boolean headerMatches(AbstractFunction other)
            throws ParsingException {
        if (name.equals(other.name())
                && Arrays.equals(argument_types, other.argumentTypes())) {
            if (resultDefinition == null && other.return_type() == null) {
                return true;
            }
            if (resultDefinition == null || other.return_type() == null
                    || !resultDefinition.equals(other.return_type())) {
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

    public boolean isProcedure() {
        return isProcedure;
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
                container.assertNextSemicolon();
                bodyDeclared = true;
                return true;
            }
            return false;
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            bodyDeclared = true;
            instructions = i.getNextCommand(declarations);
            i.assertNextSemicolon();
        }
    }

}
