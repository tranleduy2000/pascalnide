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

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.NameEntityImpl;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.VarargsType;
import com.duy.pascal.interperter.utils.ArrayUtil;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFunction extends NameEntityImpl {

    private static final String TAG = "AbstractFunction";

    public abstract ArgumentType[] argumentTypes();

    @Nullable
    public abstract Type returnType();

    @Override
    public String toString() {
        Type declaredType = returnType();
        if (argumentTypes().length == 0) {
            return getName() + (declaredType != null ? ":" + declaredType.toString() : "");
        } else {
            return getName() + ArrayUtil.argToString(argumentTypes())
                    + (declaredType != null ? ":" + declaredType.toString() : "");
        }
    }

    /**
     * @return converted arguments, or null, if they do not fit.
     */
    @Nullable
    public RuntimeValue[] formatArgs(List<RuntimeValue> values,
                                     ExpressionContext expressionContext) throws ParsingException {
        ArgumentType[] accepted_types = argumentTypes();
        RuntimeValue[] result = new RuntimeValue[accepted_types.length];
        Iterator<RuntimeValue> iterator = values.iterator();
        for (int i = 0; i < accepted_types.length; i++) {
            result[i] = accepted_types[i].convertArgType(iterator, expressionContext);
            if (result[i] == null) {
                //This indicates that it cannot fit.
                return null;
            }
        }
        if (iterator.hasNext()) {
            return null;
        }
        return result;
    }

    @Nullable
    public RuntimeValue[] perfectMatch(List<RuntimeValue> arguments,
                                       ExpressionContext context) throws ParsingException {
        ArgumentType[] acceptedTypes = argumentTypes();

        //check array
        boolean isArray = false;
        if (acceptedTypes.length > 0) {
            if (acceptedTypes[0] instanceof VarargsType)
                isArray = true;
        }

        if (!isArray && (acceptedTypes.length != arguments.size())) {
            return null;
        }

        Iterator<RuntimeValue> iterator = arguments.iterator();
        RuntimeValue[] result = new RuntimeValue[acceptedTypes.length];
        for (int i = 0; i < acceptedTypes.length; i++) {
            result[i] = acceptedTypes[i].perfectFit(iterator, context);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public abstract FunctionCall generatePerfectFitCall(LineInfo line,
                                                        List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract FunctionCall generateCall(LineInfo line,
                                              List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

}
