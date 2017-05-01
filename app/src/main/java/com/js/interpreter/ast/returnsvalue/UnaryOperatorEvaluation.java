package com.js.interpreter.ast.returnsvalue;


import com.duy.pascal.backend.debugable.DebuggableRValue;
import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.operators.AddressEval;
import com.js.interpreter.ast.returnsvalue.operators.BoolUniOperatorEval;
import com.js.interpreter.ast.returnsvalue.operators.DerefEval;
import com.js.interpreter.ast.returnsvalue.operators.DoubleUniOperatorEval;
import com.js.interpreter.ast.returnsvalue.operators.IntUniOperatorEval;
import com.js.interpreter.ast.returnsvalue.operators.LongUniOperatorEval;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

public abstract class UnaryOperatorEvaluation extends DebuggableRValue {
    public OperatorTypes operator;
    public RuntimeType type;
    public RValue operon;
    public LineInfo line;

    protected UnaryOperatorEvaluation(RValue operon, OperatorTypes operator,
                                      LineInfo line) {
        this.operator = operator;
        this.line = line;
        this.operon = operon;
    }

    public static RValue generateOp(ExpressionContext f,
                                    RValue v1, OperatorTypes op_type,
                                    LineInfo line) throws ParsingException {
        DeclaredType t1 = v1.get_type(f).declType;

        if (!op_type.can_be_unary) {
            throw new BadOperationTypeException(line, t1, v1, op_type);
        }
        if (op_type == OperatorTypes.ADDRESS) {
            LValue target = v1.asLValue(f);
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
            return new IntUniOperatorEval(v1, op_type, line);
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
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
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
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return operon.get_type(f);
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
