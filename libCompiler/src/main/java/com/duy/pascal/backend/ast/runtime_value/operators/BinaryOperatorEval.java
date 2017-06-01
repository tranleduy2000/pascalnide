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


import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.DeclaredType;
import com.duy.pascal.backend.data_types.JavaClassBasedType;
import com.duy.pascal.backend.data_types.OperatorTypes;
import com.duy.pascal.backend.data_types.set.EnumGroupType;
import com.duy.pascal.backend.data_types.set.SetType;
import com.duy.pascal.backend.data_types.converter.AnyToStringType;
import com.duy.pascal.backend.data_types.converter.TypeConverter;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.internal.InternalInterpreterException;
import com.duy.pascal.backend.ast.runtime_value.operators.number.BoolBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.CharBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.DoubleBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.IntegerBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.JavaBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.LongBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.number.StringBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.set.EnumBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.set.InBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.set.SetBiOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;


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

    @SuppressWarnings("unchecked")
    public static BinaryOperatorEval generateOp(ExpressionContext context,
                                                RuntimeValue v1, RuntimeValue v2,
                                                OperatorTypes operatorTypes,
                                                LineInfo line) throws ParsingException {
        DeclaredType t1 = v1.getType(context).declType;
        DeclaredType t2 = v2.getType(context).declType;

       /* if (!(t1 instanceof BasicType || t1 instanceof JavaClassBasedType)) {
            throw new BadOperationTypeException(lineInfo, t1, t2, v1, v2, operatorTypes);
        }
*/
       /* if (!(t2 instanceof BasicType || t2 instanceof JavaClassBasedType
                || t2 instanceof ArrayType)) {
            throw new BadOperationTypeException(lineInfo, t1, t2, v1, v2, operatorTypes);
        }*/

        if (t1 instanceof EnumGroupType
                && t2 instanceof SetType) {
            RuntimeValue converted = ((SetType) t2).getElementType().convert(v1, context);
            if (converted != null) {
                if (operatorTypes == OperatorTypes.IN) {
                    return new InBiOperatorEval(converted, v2, operatorTypes, line);
                }
            }
        } else if (t1 instanceof EnumGroupType && t2 instanceof EnumGroupType) {
            if (t1.equals(t2)) {
                return new EnumBiOperatorEval(v1, v2, operatorTypes, line);
            }
        } else if (t1 instanceof EnumGroupType && t2 == BasicType.Integer) {
            return new EnumBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 instanceof SetType && t2 instanceof SetType) {

            if (((SetType) t1).getElementType().equals(((SetType) t2).getElementType())) {
                return new SetBiOperatorEval(v1, v2, operatorTypes, line);
            }

        } else if (t2 instanceof SetType) {
            if (operatorTypes == OperatorTypes.IN) {
                if (t1.equals(((SetType) t2).getElementType())) {
                    return new InBiOperatorEval(v1, v2, operatorTypes, line);
                }
            }

        } else if (t1 == BasicType.StringBuilder
                || t2 == BasicType.StringBuilder) {
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
        } else if (t1 == BasicType.Double || t2 == BasicType.Double) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Double,
                    v1, (BasicType) t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Double,
                    v2, (BasicType) t2, context);
            return new DoubleBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 == BasicType.Long || t2 == BasicType.Long) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Long,
                    v1, (BasicType) t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Long,
                    v2, (BasicType) t2, context);
            return new LongBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 == BasicType.Character
                || t2 == BasicType.Character) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Character,
                    v1, (BasicType) t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Character,
                    v2, (BasicType) t2, context);
            return new CharBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 == BasicType.Integer
                || t2 == BasicType.Integer) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Integer, v1, (BasicType) t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Integer, v2, (BasicType) t2, context);
            return new IntegerBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 == BasicType.Boolean
                || t2 == BasicType.Boolean) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Boolean,
                    v1, (BasicType) t1, context);
            v2 = TypeConverter.forceConvertRequired(BasicType.Boolean,
                    v2, (BasicType) t2, context);
            return new BoolBiOperatorEval(v1, v2, operatorTypes, line);
        } else if (t1 instanceof JavaClassBasedType
                || t2 instanceof JavaClassBasedType) {
            if (operatorTypes == OperatorTypes.EQUALS
                    || operatorTypes == OperatorTypes.NOTEQUAL) {
                return new JavaBiOperatorEval(v1, v2, operatorTypes, line);
            }
        }
        throw new BadOperationTypeException(line, t1, t2, v1, v2, operatorTypes);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value1 = operon1.getValue(f, main);
        Object value2 = operon2.getValue(f, main);
        return operate(value1, value2);
    }

    public abstract Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException;

    @Override
    public String toString() {
        return "(" + operon1 + ") " + operator_type + " (" + operon2 + ')';
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
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
}
