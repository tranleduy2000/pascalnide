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

package com.duy.pascal.backend.ast.runtime_value.operators;


import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.PointerType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.OperatorTypes;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.operators.pointer.AddressEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.BoolUniOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.pointer.DerefEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.DoubleUniOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.IntegerUniOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.LongUniOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.internal.InternalInterpreterException;

public abstract class UnaryOperatorEval extends DebuggableReturnValue {
    public OperatorTypes operator;
    public RuntimeType type;
    public RuntimeValue operon;
    public LineInfo line;

    protected UnaryOperatorEval(RuntimeValue operon, OperatorTypes operator,
                                LineInfo line) {
        this.operator = operator;
        this.line = line;
        this.operon = operon;
    }

    public static RuntimeValue generateOp(ExpressionContext f,
                                          RuntimeValue v1, OperatorTypes op_type,
                                          LineInfo line) throws ParsingException {
        DeclaredType t1 = v1.getType(f).declType;

        if (!op_type.canBeUnary) {
            throw new BadOperationTypeException(line, t1, v1, op_type);
        }
        if (op_type == OperatorTypes.ADDRESS) {
            AssignableValue target = v1.asAssignableValue(f);
            if (target != null) {
                return new AddressEval(target, line);
            }
        }
        if (op_type == OperatorTypes.DEREF) {
            if (t1 instanceof PointerType) {
                return new DerefEval(v1, line);
            }
        }
        if (op_type == OperatorTypes.NOT && t1 == BasicType.Boolean) {
            return new BoolUniOperatorEval(v1, op_type, line);
        }
        if (t1 == BasicType.Integer) {
            return new IntegerUniOperatorEval(v1, op_type, line);
        }
        if (t1 == BasicType.Long) {
            return new LongUniOperatorEval(v1, op_type, line);
        }
        if (t1 == BasicType.Double) {
            return new DoubleUniOperatorEval(v1, op_type, line);
        }

        throw new BadOperationTypeException(line, t1, v1, op_type);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value = operon.getValue(f, main);
        return operate(value);
    }

    public abstract Object operate(Object value) throws PascalArithmeticException, InternalInterpreterException;

    @Override
    public String toString() {
        return "operator [" + operator + "] on [" + operon + ']';
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return operon.getType(f);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object value = operon.compileTimeValue(context);
        if (value == null) {
            return null;
        }
        try {
            return operate(value);
        } catch (PascalArithmeticException | InternalInterpreterException e) {
            throw new ConstantCalculationException(e);
        }
    }
}
