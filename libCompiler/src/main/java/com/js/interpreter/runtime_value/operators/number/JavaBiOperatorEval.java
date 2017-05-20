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

package com.js.interpreter.runtime_value.operators.number;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.ConstantAccess;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

public class JavaBiOperatorEval extends BinaryOperatorEvaluation {

    public JavaBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                              OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        switch (operator_type) {
            case EQUALS:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
        }
        return null;
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        switch (operator_type) {
            case EQUALS:
                return value1.equals(value2);
            case NOTEQUAL:
                return !value1.equals(value2);
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new JavaBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }
}
