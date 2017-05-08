package com.js.interpreter.ast.returnsvalue.operators;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.UnaryOperatorEvaluation;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

public class DoubleUniOperatorEval extends UnaryOperatorEvaluation {

    public DoubleUniOperatorEval(ReturnValue operon, OperatorTypes operator, LineInfo line) {
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
    public ReturnValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new DoubleUniOperatorEval(operon.compileTimeExpressionFold(context), operator,
                    line);
        }
    }
}
