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

package com.duy.pascal.interperter.ast.runtime.operators.number;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.BinaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.operator.DivisionByZeroException;
import com.duy.pascal.interperter.exceptions.runtime.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class ByteBiOperatorEval extends BinaryOperatorEval {

    public ByteBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                              OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        switch (operator_type) {
            case EQUALS:
            case GREATEREQ:
            case GREATERTHAN:
            case LESSEQ:
            case LESSTHAN:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            case DIVIDE:
                return new RuntimeType(BasicType.Double, false);
            default:
                return new RuntimeType(BasicType.Integer, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        int v1 = Integer.parseInt(String.valueOf(value1));
        int v2 = Integer.parseInt(String.valueOf(value2));
        switch (operator_type) {
            case AND:
                return v1 & v2;
            case DIV:
                if (v2 == 0) {
                    throw new DivisionByZeroException(line);
                }
                return v1 / v2;
            case DIVIDE:
                if (Math.abs(v2) == 0) {
                    throw new DivisionByZeroException(line);
                }
                return (double) v1 / (double) v2;
            case EQUALS:
                return v1 == v2;
            case GREATEREQ:
                return v1 >= v2;
            case GREATERTHAN:
                return v1 > v2;
            case LESSEQ:
                return v1 <= v2;
            case LESSTHAN:
                return v1 < v2;
            case MINUS:
                return v1 - v2;
            case MOD:
                return v1 % v2;
            case MULTIPLY:
                return v1 * v2;
            case NOTEQUAL:
                return v1 != v2;
            case OR:
                return v1 | v2;
            case PLUS:
                return v1 + v2;
            case SHIFTLEFT:
                return v1 << v2;
            case SHIFTRIGHT:
                return v1 >> v2;
            case XOR:
                return v1 ^ v2;
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);

        } else {
            return new ByteBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }

}
