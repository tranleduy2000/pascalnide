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

package com.duy.pascal.interperter.ast.runtime.operators;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.number.BoolUniOperatorNode;
import com.duy.pascal.interperter.ast.runtime.operators.number.DoubleUniOperatorNode;
import com.duy.pascal.interperter.ast.runtime.operators.number.IntegerUniOperatorNode;
import com.duy.pascal.interperter.ast.runtime.operators.number.LongUniOperatorNode;
import com.duy.pascal.interperter.ast.runtime.operators.pointer.AddressEval;
import com.duy.pascal.interperter.ast.runtime.operators.pointer.DerefEval;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.operator.BadOperationTypeException;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public abstract class UnaryOperatorNode extends DebuggableReturnValue {
    @NonNull
    protected final RuntimeValue childNode;
    @NonNull
    protected final OperatorTypes operator;
    public LineNumber lineNumber;

    protected UnaryOperatorNode(@NonNull RuntimeValue childNode, @NonNull OperatorTypes operator,
                                LineNumber lineNumber) {
        this.operator = operator;
        this.lineNumber = lineNumber;
        this.childNode = childNode;
    }

    public static RuntimeValue generateOp(ExpressionContext f,
                                          RuntimeValue v1, OperatorTypes opType,
                                          LineNumber line) throws Exception {
        Type t1 = v1.getRuntimeType(f).declType;

        if (!opType.canBeUnary) {
            throw new BadOperationTypeException(line, t1, v1, opType);
        }
        if (opType == OperatorTypes.ADDRESS) {
            AssignableValue target = v1.asAssignableValue(f);
            if (target != null) {
                return new AddressEval(target, line);
            }
        }
        if (opType == OperatorTypes.DEREF) {
            if (t1 instanceof PointerType) {
                return new DerefEval(v1, line);
            }
        }
        if (opType == OperatorTypes.NOT && t1.equals(BasicType.Boolean)) {
            return new BoolUniOperatorNode(v1, opType, line);
        }
        if (t1 == BasicType.Double) {
            return new DoubleUniOperatorNode(v1, opType, line);
        }
        if (t1.equals(BasicType.Long)) {
            return new LongUniOperatorNode(v1, opType, line);
        }
        if (t1.equals(BasicType.Integer)) {
            return new IntegerUniOperatorNode(v1, opType, line);
        }
        throw new BadOperationTypeException(line, t1, v1, opType);
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return lineNumber;
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        boolean debug = main.isDebug();
        if (debug) {
            main.getDebugListener().onEvaluatingExpr(lineNumber, toString());
            main.setDebug(false);
        }

        Object value = childNode.getValue(f, main);
        Object result = operate(value);

        //restore mode
        main.setDebug(debug);
        if (main.isDebug()) {
            main.getDebugListener().onEvaluatedExpr(lineNumber, toString(), result.toString());
        }
        return result;
    }

    public abstract Object operate(Object value) throws PascalArithmeticException, InternalInterpreterException;

    @Override
    public String toString() {
        return operator + "" + childNode;
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return childNode.getRuntimeType(context);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object value = childNode.compileTimeValue(context);
        if (value == null) {
            return null;
        }
        try {
            return operate(value);
        } catch (PascalArithmeticException | InternalInterpreterException e) {
            throw new ConstantCalculationException(e);
        }
    }

    @Override
    public boolean canDebug() {
        return true;
    }
}
