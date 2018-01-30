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

package com.duy.pascal.ui.debug;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.function.AbstractCallableFunction;
import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 24-Mar-17.
 */

public class DebugManager {

    public static void debugAssign(LineNumber lineNumber, AssignableValue left, Object old,
                                   Object value, VariableContext context, RuntimeExecutableCodeUnit<?> main) {
        if (main.isDebug()) {
            main.getDebugListener().onAssignValue(lineNumber, left, old, value, context);
        }
    }

    public static void debugWhileStatement() {

    }

    public static void debugForStatement() {

    }

    public static void debugIfStatement() {

    }

    public static void onPreFunctionCall(AbstractCallableFunction function, RuntimeValue[] arguments,
                                         RuntimeExecutableCodeUnit<?> main) {
        if (main.isDebug()) {
            main.getDebugListener().onPreFunctionCall(function, arguments);
        }
    }

    public static void onFunctionCalled(AbstractCallableFunction function, RuntimeValue[] arguments,
                                        Object result, RuntimeExecutableCodeUnit<?> main) {
        if (main.isDebug()) {
            main.getDebugListener().onFunctionCalled(function, arguments, result);
        }
    }

    public static void onEvalParameterFunction(LineNumber lineNumber, String argName, Object value,
                                               RuntimeExecutableCodeUnit main) {
        if (main.isDebug()) {
            main.getDebugListener().onEvalParameterFunction(lineNumber, argName, value);
        }
    }

    public static void showMessage(LineNumber pos, String msg, RuntimeExecutableCodeUnit main) {
        if (main.isDebug()) {
            main.getDebugListener().showMessage(pos, msg);
        }
    }
}
