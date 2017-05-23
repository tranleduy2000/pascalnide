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
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.duy.pascal.backend.utils.ArrayUtils;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFunction implements NamedEntity {

    public static final String TAG = AbstractFunction.class.getSimpleName();


    @Override
    public abstract String name();

    public abstract ArgumentType[] argumentTypes();

    public abstract DeclaredType returnType();

    @Override
    public String toString() {
        if (argumentTypes().length == 0) {
            return name();
        } else {
            return name() + ArrayUtils.argToString(argumentTypes());
        }
    }

    /**
     * @return converted arguments, or null, if they do not fit.
     */
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

    public abstract RuntimeValue generatePerfectFitCall(LineInfo line,
                                                        List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract RuntimeValue generateCall(LineInfo line,
                                              List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

}
