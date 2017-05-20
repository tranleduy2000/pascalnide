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

package com.duy.pascal.backend.exceptions.operator;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.runtime_value.RuntimeValue;

public class BadOperationTypeException extends ParsingException {
    public DeclaredType declaredType, declaredType1;
    public RuntimeValue value1, value2;
    public OperatorTypes operatorTypes;

    public BadOperationTypeException() {
        super(new LineInfo(-1, "Unknown"));
    }

    public BadOperationTypeException(LineInfo line, DeclaredType t1,
                                     DeclaredType t2, RuntimeValue v1, RuntimeValue v2,
                                     OperatorTypes operation) {
        super(line, "Operator " + operation
                + " cannot be applied to arguments '" + v1 + "' and '" + v2
                + "'.  One has operator " + t1 + " and the other has operator " + t2
                + ".");
        this.value1 = v1;
        this.value2 = v2;
        this.operatorTypes = operation;
        declaredType = t1;
        declaredType1 = t2;
    }

    public BadOperationTypeException(LineInfo line, DeclaredType t1,
                                     RuntimeValue v1,
                                     OperatorTypes operation) {
        super(line, "Operator " + operation
                + " cannot be applied to argument '" + v1
                + "' of type " + t1
                + ".");
    }

    public BadOperationTypeException(LineInfo line, OperatorTypes operator) {
        super(line, "Operator " + operator + " is not a unary operator.");
    }
}
