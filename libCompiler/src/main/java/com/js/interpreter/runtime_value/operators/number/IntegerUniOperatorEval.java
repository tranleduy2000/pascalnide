package com.js.interpreter.runtime_value.operators.number;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.ConstantAccess;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.UnaryOperatorEvaluation;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

public class IntegerUniOperatorEval extends UnaryOperatorEvaluation {

    public IntegerUniOperatorEval(RuntimeValue operon, OperatorTypes operator, LineInfo line) {
        super(operon, operator, line);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Integer, false);
    }

    @Override
    public Object operate(Object value) throws PascalArithmeticException, InternalInterpreterException {
        switch (operator) {
            case PLUS:
                return +(int) value;
            case MINUS:
                return -(int) value;
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
            return new IntegerUniOperatorEval(operon.compileTimeExpressionFold(context), operator,
                    line);
        }
    }
}
