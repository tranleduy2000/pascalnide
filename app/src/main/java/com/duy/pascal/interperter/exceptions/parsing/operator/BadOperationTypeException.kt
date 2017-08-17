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

package com.duy.pascal.interperter.exceptions.parsing.operator

import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue
import com.duy.pascal.interperter.linenumber.LineInfo
import com.duy.pascal.interperter.exceptions.parsing.ParsingException
import com.duy.pascal.interperter.declaration.lang.types.Type
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes

class BadOperationTypeException : ParsingException {
    var declaredType: Type? = null
    var declaredType1: Type? = null
    var value1: RuntimeValue? = null
    var value2: RuntimeValue? = null
    var operatorTypes: OperatorTypes? = null

    constructor() : super(LineInfo(-1, "Unknown")) {}

    constructor(line: LineInfo, t1: Type,
                t2: Type, v1: RuntimeValue?, v2: RuntimeValue?,
                operation: OperatorTypes) : super(line, "Operator " + operation
            + " cannot be applied to arguments '" + v1 + "' and '" + v2
            + "'.  One has type " + t1 + " and the other has type " + t2
            + ".") {
        this.value1 = v1
        this.value2 = v2
        this.operatorTypes = operation
        declaredType = t1
        declaredType1 = t2
    }

    constructor(line: LineInfo, t1: Type,
                v1: RuntimeValue,
                operation: OperatorTypes) : super(line, "Operator " + operation
            + " cannot be applied to argument '" + v1
            + "' of type " + t1
            + ".") {
    }

    constructor(line: LineInfo, operator: OperatorTypes) : super(line, "Operator $operator is not a unary operator.") {}
}
