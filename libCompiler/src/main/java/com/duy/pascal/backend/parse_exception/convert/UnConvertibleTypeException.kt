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

package com.duy.pascal.backend.parse_exception.convert

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext
import com.duy.pascal.backend.ast.runtime_value.value.ConstantAccess
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue
import com.duy.pascal.backend.ast.runtime_value.value.VariableAccess
import com.duy.pascal.backend.parse_exception.ParsingException
import com.duy.pascal.backend.data_types.DeclaredType

class UnConvertibleTypeException : ParsingException {

    /**
     * identifier    :=    value;
     * [targetType]        [valueType]
     */

    /**
     * expression
     */
    var value: RuntimeValue
    var valueType: DeclaredType

    /**
     * identifier
     */
    var targetType: DeclaredType
    var identifier: RuntimeValue? = null

    /**
     * the scope of variable
     */
    var scope: ExpressionContext


    constructor(value: RuntimeValue,
                targetType: DeclaredType,
                valueType: DeclaredType, scope: ExpressionContext) : super(value.lineNumber,
            "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                    + ", which cannot be converted to the type \"" + targetType + "\"") {

        this.value = value
        this.valueType = valueType
        this.targetType = targetType
        this.scope = scope
    }

    /**
     * @param identifier - variable identifier, constant, function
     */
    constructor(value: RuntimeValue,
                identifierType: DeclaredType,
                valueType: DeclaredType,
                identifier: RuntimeValue, scope: ExpressionContext) : super(value.lineNumber,
            "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                    + ", which cannot be "
                    + "converted to the type \"" + identifierType + "\" of expression or variable " + identifier) {

        this.value = value
        this.valueType = valueType
        this.targetType = identifierType
        this.identifier = identifier
        this.scope = scope
    }

    override val isAutoFix: Boolean
        get() = identifier is VariableAccess || value is VariableAccess
                || identifier is ConstantAccess<*> && (identifier as ConstantAccess<*>).name != null ||
                value is ConstantAccess<*> && (value as ConstantAccess<*>).name != null
}
