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
import com.duy.pascal.interperter.ast.runtime.operators.number.BoolBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.CharBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.DoubleBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.IntegerBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.JavaBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.LongBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.number.StringBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.set.EnumBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.set.InBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.operators.set.SetBiOperatorEval;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.StringLimitType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.converter.AnyToStringType;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;
import com.duy.pascal.interperter.exceptions.parsing.operator.BadOperationTypeException;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.runtime.CompileException;
import com.duy.pascal.interperter.exceptions.runtime.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineInfo;


public abstract class BinaryOperatorEval extends DebuggableReturnValue {
    protected OperatorTypes operator_type;

    protected RuntimeValue operon1;
    protected RuntimeValue operon2;
    protected LineInfo line;

    public BinaryOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                              OperatorTypes operator, LineInfo line) {
        this.operator_type = operator;
        this.operon1 = operon1;
        this.operon2 = operon2;
        this.line = line;
    }

    public static BinaryOperatorEval generateOp(@NonNull ExpressionContext context,
                                                @NonNull RuntimeValue v1, @NonNull RuntimeValue v2,
                                                @NonNull OperatorTypes operatorTypes,
                                                @NonNull LineInfo line) throws Exception {
        Type t1 = v1.getRuntimeType(context).declType;
        Type t2 = v2.getRuntimeType(context).declType;
        if (t1 instanceof JavaClassBasedType
                || t2 instanceof JavaClassBasedType) {
            if (operatorTypes == OperatorTypes.EQUALS
                    || operatorTypes == OperatorTypes.NOTEQUAL) {
                return new JavaBiOperatorEval(v1, v2, operatorTypes, line);
            }
        }
        if (t1 instanceof EnumGroupType
                && t2 instanceof SetType) {
            RuntimeValue converted = ((SetType) t2).getElementType().convert(v1, context);
            if (converted != null) {
                if (operatorTypes == OperatorTypes.IN) {
                    return new InBiOperatorEval(converted, v2, operatorTypes, line);
                }
            }
        }
        if (t1 instanceof EnumGroupType && t2 instanceof EnumGroupType) {
            if (t1.equals(t2)) {
                return new EnumBiOperatorEval(v1, v2, operatorTypes, line);
            }
        }
        if (t1 instanceof EnumGroupType && t2.equals(BasicType.Integer)) {
            return new EnumBiOperatorEval(v1, v2, operatorTypes, line);
        }
        if (t1 instanceof SetType && t2 instanceof SetType) {
            if (((SetType) t1).getElementType().equals(((SetType) t2).getElementType())) {
                return new SetBiOperatorEval(v1, v2, operatorTypes, line);
            }

        }
        if (t2 instanceof SetType) {
            if (operatorTypes == OperatorTypes.IN) {
                if (t1.equals(((SetType) t2).getElementType())) {
                    return new InBiOperatorEval(v1, v2, operatorTypes, line);
                } else {
                    Class<?> left = t1.getStorageClass();
                    Class<?> right = ((SetType) t2).getElementType().getStorageClass();
                    if (TypeConverter.isPrimitive(left) && TypeConverter.isPrimitive(right)
                            && TypeConverter.isLowerThanPrecedence(left, right)) {
                        return new InBiOperatorEval(v1, v2, operatorTypes, line);
                    }
                }
            }
        }
        if (t1.equals(BasicType.StringBuilder) || t2.equals(BasicType.StringBuilder)
                || t1 instanceof StringLimitType || t2 instanceof StringLimitType) {
            if (operatorTypes == OperatorTypes.PLUS) {
                v1 = new AnyToStringType(v1);
                v2 = new AnyToStringType(v2);
                return new StringBiOperatorEval(v1, v2, operatorTypes, line);
            } else {
                v1 = BasicType.StringBuilder.convert(v1, context);
                v2 = BasicType.StringBuilder.convert(v2, context);
                if (v1 != null && v2 != null) {
                    return new StringBiOperatorEval(v1, v2, operatorTypes, line);
                } else {
                    throw new BadOperationTypeException(line, t1, t2, v1, v2,
                            operatorTypes);
                }
            }
        }

        if (t1.equals(BasicType.Double) || t2.equals(BasicType.Double)) {

            v1 = TypeConverter.forceConvertRequired(BasicType.Double, v1, t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Double, v2, t2, context);
            return new DoubleBiOperatorEval(v1, v2, operatorTypes, line);

        }
        if (t1.equals(BasicType.Long) || t2.equals(BasicType.Long)) {

            v1 = TypeConverter.forceConvertRequired(BasicType.Long, v1, t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Long, v2, t2, context);
            return new LongBiOperatorEval(v1, v2, operatorTypes, line);

        }
        if (t1.equals(BasicType.Integer) || t2.equals(BasicType.Integer)) {

            v1 = TypeConverter.forceConvertRequired(BasicType.Integer, v1, t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Integer, v2, t2, context);
            return new IntegerBiOperatorEval(v1, v2, operatorTypes, line);

        }
        if (t1.equals(BasicType.Character) || t2.equals(BasicType.Character)) {

            v1 = TypeConverter.forceConvertRequired(BasicType.Character, v1, t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Character, v2, t2, context);

            return new CharBiOperatorEval(v1, v2, operatorTypes, line);

        }

        if (t1.equals(BasicType.Boolean) || t2.equals(BasicType.Boolean)) {

            v1 = TypeConverter.forceConvertRequired(BasicType.Boolean, v1, t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Boolean, v2, t2, context);

            return new BoolBiOperatorEval(v1, v2, operatorTypes, line);

        }
        throw new BadOperationTypeException(line, t1, t2, v1, v2, operatorTypes);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
        this.line = lineNumber;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {

        Object value1 = operon1.getValue(f, main); //get left value
        Object value2 = operon2.getValue(f, main); //get right value
        Object result = operate(value1, value2); //operate

        if (main.isDebug()) {
            main.getDebugListener().onEvaluatedExpr(line, toString(), result.toString());
            main.scriptControlCheck(getLineNumber());
        }

        return result;

    }

    public abstract Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException, CompileException;

    @Override
    public String toString() {
        return operon1 + " " + operator_type + " " + operon2;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object value1 = operon1.compileTimeValue(context);
        Object value2 = operon2.compileTimeValue(context);
        if (value1 != null && value2 != null) {
            try {
                return operate(value1, value2);
            } catch (PascalArithmeticException e) {
                throw new ConstantCalculationException(e);
            } catch (InternalInterpreterException e) {
                throw new ConstantCalculationException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean canDebug() {
        return true;
    }
}
