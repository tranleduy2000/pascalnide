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

package com.duy.pascal.backend.ast.function_declaretion.builtin;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.types.ArgumentType;
import com.duy.pascal.backend.types.DeclaredType;


public interface IMethodDeclaration {
    /**
     * @return simple name of method;
     */
    String getName();

    FunctionCall generateCall(LineInfo line, RuntimeValue[] values,
                              ExpressionContext f) throws ParsingException;

    FunctionCall generatePerfectFitCall(LineInfo line,
                                        RuntimeValue[] values, ExpressionContext f)
            throws ParsingException;

    ArgumentType[] argumentTypes();

    /**
     * return type of method
     */
    DeclaredType returnType();

    /**
     * short getDescription of method, it can be null
     */
    @Nullable
    String description();
}
