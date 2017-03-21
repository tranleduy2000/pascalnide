package com.js.interpreter.ast.returnsvalue.operators;


import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnassignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.typeconversion.TypeConverter;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.DebuggableReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;


public abstract class BinaryOperatorEvaluation extends DebuggableReturnsValue {
    OperatorTypes operator_type;

    ReturnsValue operon1;

    ReturnsValue operon2;
    LineInfo line;

    public BinaryOperatorEvaluation(ReturnsValue operon1, ReturnsValue operon2,
                                    OperatorTypes operator, LineInfo line) {
        this.operator_type = operator;
        this.operon1 = operon1;
        this.operon2 = operon2;
        this.line = line;
    }

    /* Boy, templates or macros like C++ sure would be useful now... */
    public static BinaryOperatorEvaluation generateOp(ExpressionContext f,
                                                      ReturnsValue v1, ReturnsValue v2, OperatorTypes op_type,
                                                      LineInfo line) throws ParsingException {
        DeclaredType t1 = v1.get_type(f).declType;
        DeclaredType t2 = v2.get_type(f).declType;
        if (!(t1 instanceof BasicType || t1 instanceof JavaClassBasedType)) {
            throw new BadOperationTypeException(line, t1, t2, v1, v2, op_type);
        }
        if (!(t2 instanceof BasicType || t2 instanceof JavaClassBasedType)) {
            throw new BadOperationTypeException(line, t1, t2, v1, v2, op_type);
        }
        if (t1 == BasicType.StringBuilder
                || t2 == BasicType.StringBuilder) {
            if (op_type == OperatorTypes.PLUS) {
                v1 = new TypeConverter.AnyToString(v1);
                v2 = new TypeConverter.AnyToString(v2);
                return new StringBiOperatorEval(v1, v2, op_type, line);
            } else {
                v1 = BasicType.StringBuilder.convert(v1, f);
                v2 = BasicType.StringBuilder.convert(v2, f);
                if (v1 != null && v2 != null) {
                    return new StringBiOperatorEval(v1, v2, op_type, line);
                } else {
                    throw new BadOperationTypeException(line, t1, t2, v1, v2,
                            op_type);
                }
            }
        }
        if (t1 == BasicType.Double || t2 == BasicType.Double) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Double,
                    v1, (BasicType) t1);
            v2 = TypeConverter.forceConvertRequired(BasicType.Double,
                    v2, (BasicType) t2);
            return new DoubleBiOperatorEval(v1, v2, op_type, line);
        }
        if (t1 == BasicType.Long || t2 == BasicType.Long) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Long,
                    v1, (BasicType) t1);
            v2 = TypeConverter.forceConvertRequired(BasicType.Long,
                    v2, (BasicType) t2);
            return new LongBiOperatorEval(v1, v2, op_type, line);
        }
        if (t1 == BasicType.Integer
                || t2 == BasicType.Integer) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Integer,
                    v1, (BasicType) t1);
            v2 = TypeConverter.forceConvertRequired(BasicType.Integer,
                    v2, (BasicType) t2);
            return new IntBiOperatorEval(v1, v2, op_type, line);
        }
        if (t1 == BasicType.Character
                || t2 == BasicType.Character) {
            v1 = TypeConverter.forceConvertRequired(
                    BasicType.Character, v1, (BasicType) t1);
            v2 = TypeConverter.forceConvertRequired(
                    BasicType.Character, v2, (BasicType) t2);
            return new CharBiOperatorEval(v1, v2, op_type, line);
        }
        if (t1 == BasicType.Boolean
                || t2 == BasicType.Boolean) {
            v1 = TypeConverter.forceConvertRequired(BasicType.Boolean,
                    v1, (BasicType) t1);
            v2 = TypeConverter.forceConvertRequired(BasicType.Boolean,
                    v2, (BasicType) t2);
            return new BoolBiOperatorEval(v1, v2, op_type, line);
        }
        throw new BadOperationTypeException(line, t1, t2, v1, v2, op_type);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
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

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnassignableTypeException {
        throw new UnassignableTypeException(r);
    }
}
