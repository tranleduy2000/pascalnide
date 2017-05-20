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

package com.duy.pascal.backend.function_declaretion;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.FunctionCall;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.SimpleFunctionCall;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public abstract class AbstractCallableFunction extends AbstractFunction {

    /**
     * This invokes a function call of any operator.
     *
     * @param parentContext The program context.
     * @return The return value of the called function.
     */
    public abstract Object call(VariableContext parentContext,
                                RuntimeExecutableCodeUnit<?> main, Object[] arguments)
            throws RuntimePascalException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException;

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException {
        RuntimeValue[] args = perfectMatch(values, f);
        if (args == null) {
            return null;
        }
        return new SimpleFunctionCall(this, args, line);
    }

    @Override
    public FunctionCall generateCall(LineInfo line, List<RuntimeValue> values,
                                     ExpressionContext f) throws ParsingException {
        RuntimeValue[] args = formatArgs(values, f);
        if (args == null) {
            return null;
        }
        return new SimpleFunctionCall(this, args, line);
    }


    @Override
    public String description() {
        return null;
    }
}
