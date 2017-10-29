/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.declaration.lang.function;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.ui.debug.CallStack;
import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.variablecontext.FunctionOnStack;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.config.ProgramMode;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.OverridingFunctionBodyException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.ForwardToken;
import com.duy.pascal.interperter.tokens.basic.SemicolonToken;
import com.duy.pascal.interperter.tokens.basic.VarToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.grouping.ParenthesizedToken;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionDeclaration extends AbstractCallableFunction {
    private static final String TAG = "FunctionDeclaration";
    public ExpressionContextMixin declaration;
    /**
     * name of function or procedure
     */
    public Name name;
    public Node instructions;

    public LineInfo startPosition;
    public LineInfo endPosition;

    public Name[] argumentNames;
    public RuntimeType[] argumentTypes;

    /*field store value of function*/
    private VariableDeclaration resultDefinition;
    private boolean isProcedure = false;
    private boolean bodyDeclared;

    private int modifier = Modifier.PUBLIC;

    public FunctionDeclaration(Name name, ExpressionContext parent, GrouperToken grouperToken,
                               boolean isProcedure) throws Exception {
        parseHeader(name, parent, grouperToken, isProcedure);
    }

    public FunctionDeclaration(Name name, ExpressionContext parent, GrouperToken grouperToken,
                               boolean isProcedure, LineInfo lineInfo) throws Exception {
        parseHeader(name, parent, grouperToken, isProcedure);
        this.startPosition = lineInfo;
    }

    public FunctionDeclaration(ExpressionContext parent, GrouperToken grouperToken,
                               boolean isProcedure) throws Exception {
        Name name = grouperToken.nextWordValue();
        parseHeader(name, parent, grouperToken, isProcedure);
    }

    public FunctionDeclaration(ExpressionContext p) {
        this.declaration = new FunctionExpressionContext(this, p);
        this.argumentNames = new Name[0];
        this.argumentTypes = new RuntimeType[0];
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public void parseHeader(Name name, ExpressionContext parent, GrouperToken grouperToken,
                            boolean isProcedure) throws Exception {

        this.name = name;
        this.declaration = new FunctionExpressionContext(this, parent);
        this.startPosition = grouperToken.peek().getLineNumber();
        this.isProcedure = isProcedure;

        getArgumentsForDeclaration(grouperToken);
        Token next = grouperToken.peek();
        if (isProcedure == next instanceof ColonToken) {
            throw new ParsingException(next.getLineNumber(),
                    "Functions must have a return value, and procedures cannot have one");
        }
        if (!isProcedure) {
            next = grouperToken.take();
            if (!(next instanceof ColonToken)) {
                throw new ExpectedTokenException(":", next);
            }
            //define variable result of function, the name of variable same as name function
            if (parent.root().getConfig().getMode() == ProgramMode.DELPHI) {
                resultDefinition = new VariableDeclaration(Name.create("result"),
                        grouperToken.getNextPascalType(parent), startPosition);
            } else {
                resultDefinition = new VariableDeclaration(name,
                        grouperToken.getNextPascalType(parent), startPosition);
            }
            this.declaration.declareVariable(resultDefinition);
        }

        Token t = grouperToken.take(); //semicolon token
        this.endPosition = t.getLineNumber();
        if (!(t instanceof SemicolonToken)) {
            throw new ExpectedTokenException(new SemicolonToken(null), t);
        }

        instructions = null;
        NamedEntity n = parent.getConstantDefinition(name);
        if (n != null) {
            throw new DuplicateIdentifierException(n, this);
        }
        n = parent.getVariableDefinition(name);
        if (n != null) {
            throw new DuplicateIdentifierException(n, this);
        }
    }

    public Name[] getArgumentNames() {
        return argumentNames;
    }

    public RuntimeType[] getArgumentTypes() {
        return argumentTypes;
    }

    public ExpressionContextMixin getDeclaration() {
        return declaration;
    }

    @NonNull
    public Name getName() {
        return name;
    }

    public void parseFunctionBody(GrouperToken i) throws Exception {
        Token next = i.peekNoEOF();
        if (next instanceof ForwardToken) {
            i.take(); //forward token
            i.assertNextSemicolon();
        } else {
            if (instructions != null) {
                throw new OverridingFunctionBodyException(this, i.getLineNumber());
            }
            while (!bodyDeclared) {
                declaration.addNextDeclaration(i);
            }
        }
    }


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Object call(VariableContext f, RuntimeExecutableCodeUnit<?> main,
                       Object[] arguments) throws RuntimePascalException {
        if (this.declaration.root() instanceof PascalUnitDeclaration) {
            f = main.getLibraryContext((PascalUnitDeclaration) declaration.root());
        }
        FunctionOnStack functionOnStack = new FunctionOnStack(f, main, this, arguments);
        if (main.isDebug()) {
            main.getDebugListener().onVariableChange(new CallStack(functionOnStack));
        }
        Object execute = functionOnStack.execute();
        if (main.isDebug()) {
            main.getDebugListener().onVariableChange(new CallStack(f));
        }
        return execute;
    }

    private void getArgumentsForDeclaration(GrouperToken i) throws Exception { // need
        List<WordToken> namesList = new ArrayList<>();
        List<RuntimeType> typesList = new ArrayList<>();
        Token next = i.peek();
        if (next instanceof ParenthesizedToken) {
            ParenthesizedToken argumentsToken = (ParenthesizedToken) i.take();
            while (argumentsToken.hasNext()) {
                int j = 0; // counts number added of this operator
                next = argumentsToken.take();
                boolean is_varargs = false;
                if (next instanceof VarToken) {
                    is_varargs = true;
                    next = argumentsToken.take();
                }
                while (true) {
                    namesList.add((WordToken) next);
                    j++;
                    next = argumentsToken.take();
                    if (next instanceof CommaToken) {
                        next = argumentsToken.take();
                    } else {
                        break;
                    }
                }

                if (!(next instanceof ColonToken)) {
                    throw new ExpectedTokenException(":", next);
                }
                Type type;
                type = argumentsToken.getNextPascalType(declaration);

                while (j > 0) {
                    typesList.add(new RuntimeType(type, is_varargs));
                    j--;
                }
                if (argumentsToken.hasNext()) {
                    next = argumentsToken.take();
                    if (!(next instanceof SemicolonToken)) {
                        throw new ExpectedTokenException(";", next);
                    }
                }
            }
        }
        argumentTypes = typesList.toArray(new RuntimeType[typesList.size()]);
        argumentNames = new Name[namesList.size()];
        for (int j = 0; j < argumentNames.length; j++) {
            WordToken n = namesList.get(j);
            argumentNames[j] = n.name;
        }

    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    @Nullable
    public Type returnType() {
        return resultDefinition == null ? null : resultDefinition.type;
    }

    public boolean headerMatches(AbstractFunction other) throws Exception {
        if (name.equals(other.getName())
                && Arrays.equals(argumentTypes, other.argumentTypes())) {
            if (resultDefinition == null && other.returnType() == null) {
                return true;
            }
            if (resultDefinition == null || other.returnType() == null
                    || !resultDefinition.equals(other.returnType())) {
                System.err.println("Warning: " +
                        "Overriding previously declared return type for function " + name);
            }
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return isProcedure ? "procedure" : "function";
    }

    @Override
    public LineInfo getLineNumber() {
        return startPosition;
    }

    public boolean isProcedure() {
        return isProcedure;
    }

    public class FunctionExpressionContext extends ExpressionContextMixin {
        public FunctionDeclaration function;

        FunctionExpressionContext(FunctionDeclaration function, ExpressionContext parent) {
            super(parent.root(), parent);
            this.function = function;
        }

        @NonNull
        @Override
        public LineInfo getStartPosition() {
            return startPosition;
        }

        @Override
        public Node handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws Exception {
            return null;
        }

        @Override
        public boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
                throws Exception {
            if (next instanceof ForwardToken) {
                container.assertNextSemicolon();
                bodyDeclared = true;
                return true;
            }
            return false;
        }


        @Override
        public void handleBeginEnd(GrouperToken i) throws Exception {
            bodyDeclared = true;
            instructions = i.getNextCommand(this);
            i.assertNextSemicolon();
        }

        @Override
        public VariableDeclaration getVariableDefinitionLocal(Name ident) {
            VariableDeclaration unitVariableDecl = super.getVariableDefinitionLocal(ident);
            if (unitVariableDecl != null) {
                return unitVariableDecl;
            }
            for (int i = 0; i < argumentNames.length; i++) {
                if (argumentNames[i].equals(ident)) {
                    return new VariableDeclaration(argumentNames[i], argumentTypes[i].getRawType(),
                            function.getLineNumber());
                }
            }
            return null;
        }
    }

}
