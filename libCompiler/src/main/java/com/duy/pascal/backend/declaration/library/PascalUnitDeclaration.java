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

package com.duy.pascal.backend.declaration.library;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeUnitPascal;
import com.duy.pascal.backend.declaration.function.AbstractFunction;
import com.duy.pascal.backend.declaration.function.FunctionDeclaration;
import com.duy.pascal.backend.ast.codeunit.ExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.builtin_libraries.PascalLibrary;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.MissingBodyFunctionException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.syntax.MisplacedDeclarationException;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.FinalizationToken;
import com.duy.pascal.backend.tokens.basic.ForwardToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.ImplementationToken;
import com.duy.pascal.backend.tokens.basic.InitializationToken;
import com.duy.pascal.backend.tokens.basic.InterfaceToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.closing.EndToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.UnitToken;
import com.duy.pascal.backend.declaration.types.DeclaredType;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.google.common.collect.ArrayListMultimap;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PascalUnitDeclaration extends ExecutableCodeUnit implements PascalLibrary {
    private IRunnablePascal handler;

    public PascalUnitDeclaration(Reader program,
                                 String sourceName,
                                 List<ScriptSource> includeDirectories,
                                 @Nullable IRunnablePascal handler)
            throws ParsingException {
        super(program, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected UnitExpressionContext getExpressionContextInstance(IRunnablePascal handler) {
        return new UnitExpressionContext(handler);
    }

    @Override
    public RuntimeUnitPascal generate() {
        return new RuntimeUnitPascal(this);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * Do not declare constants to parentContext because
     * it can be duplicate with constants of user
     */
    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {
        Map<String, DeclaredType> typedefs = context.getTypedefs();
        for (Map.Entry<String, DeclaredType> type : typedefs.entrySet()) {
            parentContext.declareTypedef(type.getKey(), type.getValue());
        }
    }

    /**
     * Do not declare variable to parentContext because
     * value of variable can change in unit
     */
    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {
        /*ArrayList<VariableDeclaration> variables = context.getVariables();
        for (VariableDeclaration variable : variables) {
            parentContext.declareVariable(variable);
        }*/
    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {
        // get list name interface instead of get map function
        // because I don't want to add built in function twice,
        //this is bad performance when match argument and leak memory
        ArrayList<String> forwardFunctions = ((UnitExpressionContext) context).getForwardFunctions();

        ArrayListMultimap<String, AbstractFunction> callableFunctions = context.getCallableFunctions();
        for (String name : forwardFunctions) {
            List<AbstractFunction> abstractFunctions = callableFunctions.get(name);
            for (AbstractFunction function : abstractFunctions) {
                parentContext.declareFunction(function);
            }
        }

    }

    public class UnitExpressionContext extends CodeUnitExpressionContext {
        private boolean isParsed = false;
        @Nullable
        private Executable initInstruction;
        @Nullable
        private Executable finalInstruction;
        private ArrayList<String> publicVariables = new ArrayList<>();
        private ArrayList<String> publicConstants = new ArrayList<>();
        private ArrayList<String> forwardFunctions = new ArrayList<>();

        @Nullable
        private LineInfo startLine;

        UnitExpressionContext(@NonNull IRunnablePascal handler) {
            super(handler);
            config.setLibrary(true);
        }

        @NonNull
        @Override
        public LineInfo getStartLine() {
            return startLine;
        }

        public ArrayList<String> getForwardFunctions() {
            return forwardFunctions;
        }


        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken i)
                throws ParsingException {

            if (next instanceof UnitToken) {
                GrouperToken container = (GrouperToken) next;
                this.startLine = next.getLineNumber();
                programName = container.nextWordValue();
                container.assertNextSemicolon();

                if (!(container.peek() instanceof InterfaceToken))
                    throw new ExpectedTokenException("interface", container.peek());

                while (container.hasNext()) {
                    this.addNextDeclaration(container);
                }

                // dot token
                if (i.peek() instanceof PeriodToken) {
                    i.take();
                } else {
                    throw new ExpectedTokenException(".", i.take());
                }
                return true;
            } else if (next instanceof InterfaceToken) {
                declareInterface(i);
                return true;

            } else if (next instanceof ImplementationToken) {
                declareImplementation(i);
                return true;

            } else if (next instanceof InitializationToken) {

                declareInit(i);
                return true;

            } else if (next instanceof FinalizationToken) {
                declareFinal(i);
                return true;

            }
            //end region
            return false;
        }

        private void declareInterface(GrouperToken i) throws ParsingException {
            while (!(i.peek() instanceof ImplementationToken ||
                    i.peek() instanceof EndToken || i.peek() instanceof InitializationToken ||
                    i.peek() instanceof FinalizationToken ||
                    i.peek() instanceof EOFToken)) {
                Token next = i.peek();
                if (next.canDeclareInInterface())
                    this.addNextDeclaration(i);
                else
                    throw new ExpectedTokenException("implementation", next);
            }
        }

        private void declareImplementation(GrouperToken i) throws ParsingException {
            Token next = i.peek();
            while (!(next instanceof InitializationToken ||
                    next instanceof EndToken || next instanceof FinalizationToken
                    || i.peek() instanceof EOFToken)) {
                super.addNextDeclaration(i);
                next = i.peek();
            }

            ArrayListMultimap<String, AbstractFunction> callableFunctions = getCallableFunctions();
            for (String name : forwardFunctions) {
                List<AbstractFunction> abstractFunctions = callableFunctions.get(name);
                for (AbstractFunction f : abstractFunctions) {
                    if (f instanceof FunctionDeclaration) {
                        if (((FunctionDeclaration) f).instructions == null) {
                            throw new MissingBodyFunctionException(f.getName(),
                                    ((FunctionDeclaration) f).line);
                        }
                    }
                }
            }
        }

        private void declareInit(GrouperToken grouperToken) throws ParsingException {
            this.initInstruction = grouperToken.getNextCommand(this);
            grouperToken.assertNextSemicolon();
        }

        private void declareFinal(GrouperToken grouperToken) throws ParsingException {
            this.finalInstruction = grouperToken.getNextCommand(this);
            grouperToken.assertNextSemicolon();

        }

        @Override
        public void addNextDeclaration(GrouperToken i) throws ParsingException {
            Token next = i.peek();
            if (next instanceof ProcedureToken || next instanceof FunctionToken) {
                i.take();
                boolean is_procedure = next instanceof ProcedureToken;
                FunctionDeclaration declaration = new FunctionDeclaration(this, i, is_procedure);
                getExistFunction(declaration);

                //check exception
                if (i.peek() instanceof ForwardToken) {
                    throw new MisplacedDeclarationException(i.peek().getLineNumber(), "forward", this);
                }

                forwardFunctions.add(declaration.getName());
            } else {
                super.addNextDeclaration(i);
            }
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
         /*   if (initInstruction != null) {
                throw new MultipleDefinitionsMainException(i.getLineNumber());
            } else {
                initInstruction = i.getNextCommand(this);

                // dot token
                if (i.peek() instanceof PeriodToken) {
                    i.take();
                } else {
                    throw new ExpectedTokenException(".", i.take());
                }
            }*/
            throw new MisplacedDeclarationException(i.peek().getLineNumber(),
                    "main function", this);
        }

        @Nullable
        public Executable getInitInstruction() {
            return initInstruction;
        }

        @Nullable
        public Executable getFinalInstruction() {
            return finalInstruction;
        }
    }

}
