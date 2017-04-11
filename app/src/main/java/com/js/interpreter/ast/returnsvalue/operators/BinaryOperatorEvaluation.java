package com.js.interpreter.ast.returnsvalue.operators;


import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.typeconversion.AnyToStringType;
import com.duy.pascal.backend.pascaltypes.typeconversion.TypeConverter;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
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
    public static BinaryOperatorEvaluation generateOp(ExpressionContext main,
                                                      ReturnsValue value1,
                                                      ReturnsValue value2, OperatorTypes operateType,
                                                      LineInfo line) throws ParsingException {
        DeclaredType type1 = value1.getType(main).declaredType;
        DeclaredType type2 = value2.getType(main).declaredType;

        if (!(type1 instanceof BasicType || type1 instanceof JavaClassBasedType)) {
            throw new BadOperationTypeException(line, type1, type2, value1, value2, operateType);
        }

        if (!(type2 instanceof BasicType || type2 instanceof JavaClassBasedType)) {
            throw new BadOperationTypeException(line, type1, type2, value1, value2, operateType);
        }

        if (type1 == BasicType.StringBuilder || type2 == BasicType.StringBuilder) {
            if (operateType == OperatorTypes.PLUS) {
                value1 = new AnyToStringType(value1);
                value2 = new AnyToStringType(value2);
                return new StringBiOperatorEval(value1, value2, operateType, line);
            } else {
                value1 = BasicType.StringBuilder.convert(value1, main);
                value2 = BasicType.StringBuilder.convert(value2, main);
                if (value1 != null && value2 != null) {
                    return new StringBiOperatorEval(value1, value2, operateType, line);
                } else {
                    throw new BadOperationTypeException(line, type1, type2, value1, value2, operateType);
                }
            }
        }
        if (type1 == BasicType.Double || type2 == BasicType.Double) {
            value1 = TypeConverter.forceConvertRequired(BasicType.Double, value1, (BasicType) type1);
            value2 = TypeConverter.forceConvertRequired(BasicType.Double, value2, (BasicType) type2);
            return new DoubleBiOperatorEval(value1, value2, operateType, line);
        }
        if (type1 == BasicType.Long || type2 == BasicType.Long) {
            value1 = TypeConverter.forceConvertRequired(BasicType.Long, value1, (BasicType) type1);
            value2 = TypeConverter.forceConvertRequired(BasicType.Long, value2, (BasicType) type2);
            return new LongBiOperatorEval(value1, value2, operateType, line);
        }
        if (type1 == BasicType.Integer || type2 == BasicType.Integer) {
            value1 = TypeConverter.forceConvertRequired(BasicType.Integer, value1, (BasicType) type1);
            value2 = TypeConverter.forceConvertRequired(BasicType.Integer, value2, (BasicType) type2);
            return new IntBiOperatorEval(value1, value2, operateType, line);
        }
        if (type1 == BasicType.Character || type2 == BasicType.Character) {
            value1 = TypeConverter.forceConvertRequired(BasicType.Character, value1, (BasicType) type1);
            value2 = TypeConverter.forceConvertRequired(BasicType.Character, value2, (BasicType) type2);
            return new CharBiOperatorEval(value1, value2, operateType, line);
        }
        if (type1 == BasicType.Boolean || type2 == BasicType.Boolean) {
            value1 = TypeConverter.forceConvertRequired(BasicType.Boolean, value1, (BasicType) type1);
            value2 = TypeConverter.forceConvertRequired(BasicType.Boolean, value2, (BasicType) type2);
            return new BoolBiOperatorEval(value1, value2, operateType, line);
        }
        throw new BadOperationTypeException(line, type1, type2, value1, value2, operateType);
    }



    @Override
    public LineInfo getLine() {
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
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(r);
    }
}
