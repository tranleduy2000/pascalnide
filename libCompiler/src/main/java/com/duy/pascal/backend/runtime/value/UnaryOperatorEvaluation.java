package com.duy.pascal.backend.runtime.value;


import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.operator.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.operator.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.operators.pointer.AddressEval;
import com.duy.pascal.backend.runtime.value.operators.number.BoolUniOperatorEval;
import com.duy.pascal.backend.runtime.value.operators.pointer.DerefEval;
import com.duy.pascal.backend.runtime.value.operators.number.DoubleUniOperatorEval;
import com.duy.pascal.backend.runtime.value.operators.number.IntegerUniOperatorEval;
import com.duy.pascal.backend.runtime.value.operators.number.LongUniOperatorEval;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.exception.internal.InternalInterpreterException;

public abstract class UnaryOperatorEvaluation extends DebuggableReturnValue {
    public OperatorTypes operator;
    public RuntimeType type;
    public RuntimeValue operon;
    public LineInfo line;

    protected UnaryOperatorEvaluation(RuntimeValue operon, OperatorTypes operator,
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
