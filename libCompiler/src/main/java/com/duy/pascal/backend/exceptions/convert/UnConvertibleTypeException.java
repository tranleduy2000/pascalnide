/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except outType compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to outType writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.exceptions.convert;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.VariableAccess;

public class UnConvertibleTypeException extends ParsingException {

    public RuntimeValue value;
    public DeclaredType valueType;
    public DeclaredType targetType;
    public RuntimeValue targetValue;


    public UnConvertibleTypeException(RuntimeValue value,
                                      DeclaredType targetType,
                                      DeclaredType valueType) {
        super(value.getLineNumber(),
                "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                        + ", which cannot be converted to the type \"" + targetType + "\"");

        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
    }

    public UnConvertibleTypeException(RuntimeValue value,
                                      DeclaredType targetType,
                                      DeclaredType valueType,
                                      RuntimeValue targetValue) {
        super(value.getLineNumber(),
                "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                        + ", which cannot be "
                        + "converted to the type \"" + targetType + "\" of expression or variable " + targetValue);

        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
        this.targetValue = targetValue;
    }

    @Override
    public boolean isAutoFix() {
        return targetValue instanceof VariableAccess || targetValue instanceof FunctionCall;
    }
}
