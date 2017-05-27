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
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.VariableAccess;
import com.js.interpreter.expressioncontext.ExpressionContext;

public class UnConvertibleTypeException extends ParsingException {

    /**
     * identifier    :=    value;
     * [targetType]        [valueType]
     */

    /**
     * expression
     */
    public RuntimeValue value;
    public DeclaredType valueType;

    /**
     * identifier
     */
    public DeclaredType targetType;
    public RuntimeValue identifier;

    /**
     * the context of variable
     */
    private ExpressionContext context;


    public UnConvertibleTypeException(RuntimeValue value,
                                      DeclaredType targetType,
                                      DeclaredType valueType, ExpressionContext context) {
        super(value.getLineNumber(),
                "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                        + ", which cannot be converted to the type \"" + targetType + "\"");

        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
        this.context = context;
    }

    /**
     * @param identifier - variable identifier, constant, function
     */
    public UnConvertibleTypeException(RuntimeValue value,
                                      DeclaredType identifierType,
                                      DeclaredType valueType,
                                      RuntimeValue identifier, ExpressionContext context) {
        super(value.getLineNumber(),
                "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                        + ", which cannot be "
                        + "converted to the type \"" + identifierType + "\" of expression or variable " + identifier);

        this.value = value;
        this.valueType = valueType;
        this.targetType = identifierType;
        this.identifier = identifier;
        this.context = context;
    }

    @Override
    public boolean isAutoFix() {
        if (identifier == null) {
            return false;
        }
        return identifier instanceof VariableAccess || identifier instanceof FunctionCall
                || identifier instanceof ConstantAccess;
    }

    public ExpressionContext getContext() {
        return context;
    }

    public void setContext(ExpressionContext context) {
        this.context = context;
    }
}
