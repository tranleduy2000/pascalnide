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

package com.js.interpreter.codeunit.library;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.MissingBodyFunctionException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.syntax.MisplacedDeclarationException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.FinalizationToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.ImplementationToken;
import com.duy.pascal.backend.tokens.basic.InitializationToken;
import com.duy.pascal.backend.tokens.basic.InterfaceToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.closing.EndToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.UnitToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.codeunit.ExecutableCodeUnit;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.source_include.ScriptSource;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnitPascal extends ExecutableCodeUnit implements PascalLibrary {
    private RunnableActivity handler;

    public UnitPascal(Reader program,
                      String sourceName,
                      ListMultimap<String, AbstractFunction> functionTable,
                      List<ScriptSource> includeDirectories,
                      @Nullable RunnableActivity handler)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected UnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler) {
        return new UnitExpressionContext(functionTable, handler);
    }

    @Override
    public RuntimeUnitPascal run() {
        return new RuntimeUnitPascal(this);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

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
        Map<String, DeclaredType> typedefs = mContext.getTypedefs();
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
        /*ArrayList<VariableDeclaration> variables = mContext.getVariables();
        for (VariableDeclaration variable : variables) {
            parentContext.declareVariable(variable);
        }*/
    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {
        // get list name interface instead of get map function
        // because I don't want to add built in function twice,
        //this is bad performance when match argument and leak memory
        ArrayList<String> forwardFunctions = ((UnitExpressionContext) mContext).getForwardFunctions();

        ArrayListMultimap<String, AbstractFunction> callableFunctions = mContext.getCallableFunctions();
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

        private ArrayList<String> forwardFunctions = new ArrayList<>();

        public UnitExpressionContext(ListMultimap<String, AbstractFunction> function,
                                     RunnableActivity handler) {
            super(function, handler, true);
        }

        public ArrayList<String> getForwardFunctions() {
            return forwardFunctions;
        }

        public void declareInterface(GrouperToken i) throws ParsingException {
            while (!(i.peek() instanceof ImplementationToken ||
                    i.peek() instanceof EndToken || i.peek() instanceof InitializationToken ||
                    i.peek() instanceof FinalizationToken ||
                    i.peek() instanceof EOFToken)) {
                this.addNextDeclaration(i);
            }
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken i)
                throws ParsingException {

            if (next instanceof UnitToken) {
                GrouperToken container = (GrouperToken) next;
                programName = container.nextWordValue();
                container.assertNextSemicolon(container);

                while (container.hasNext()){
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
                            throw new MissingBodyFunctionException(f.name(),
                                    ((FunctionDeclaration) f).line);
                        }
                    }
                }
            }
        }

        public void declareInit(GrouperToken grouperToken) throws ParsingException {
            this.initInstruction = grouperToken.getNextCommand(this);
            grouperToken.assertNextSemicolon(null);
        }

        public void declareFinal(GrouperToken grouperToken) throws ParsingException {
            this.finalInstruction = grouperToken.getNextCommand(this);
            grouperToken.assertNextSemicolon(null);

        }

        @Override
        public void addNextDeclaration(GrouperToken i) throws ParsingException {
            Token next = i.peek();
            if (next instanceof ProcedureToken || next instanceof FunctionToken) {
                i.take();
                boolean is_procedure = next instanceof ProcedureToken;
                FunctionDeclaration declaration = new FunctionDeclaration(this, i, is_procedure);
                checkExistFunction(declaration);
                forwardFunctions.add(declaration.name());
            } else {
                super.addNextDeclaration(i);
            }
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            throw new MisplacedDeclarationException(i.peek().getLineInfo(),
                    "main function", UnitPascal.this.mContext);
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
