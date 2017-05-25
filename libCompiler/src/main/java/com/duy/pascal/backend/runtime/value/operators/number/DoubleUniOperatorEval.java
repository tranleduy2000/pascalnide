package com.duy.pascal.backend.runtime.value.operators.number;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.UnaryOperatorEvaluation;
import com.duy.pascal.backend.runtime.exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime.exception.internal.InternalInterpreterException;

public class DoubleUniOperatorEval extends UnaryOperatorEvaluation {

    public DoubleUniOperatorEval(RuntimeValue operon, OperatorTypes operator, LineInfo line) {
        super(operon, operator, line);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Double, false);
    }

    @Override
    public Object operate(Object value) throws PascalArithmeticException, InternalInterpreterException {
        switch (operator) {
            case PLUS:
                return +(double) value;
            case MINUS:
                return -(double) value;
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
            return new DoubleUniOperatorEval(operon.compileTimeExpressionFold(context), operator,
                    line);
        }
    }
}
