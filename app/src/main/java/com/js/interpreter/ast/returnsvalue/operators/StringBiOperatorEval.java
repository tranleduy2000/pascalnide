package com.js.interpreter.ast.returnsvalue.operators;


import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.BasicType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.duy.interpreter.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.internal.InternalInterpreterException;

public class StringBiOperatorEval extends BinaryOperatorEvaluation {

    public StringBiOperatorEval(ReturnsValue operon1, ReturnsValue operon2,
                                OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        switch (operator_type) {
            case EQUALS:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            default:
                return new RuntimeType(BasicType.StringBuilder, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        CharSequence v1 = (CharSequence) value1;
        CharSequence v2 = (CharSequence) value2;
        switch (operator_type) {
            case EQUALS:
                System.out.println(v1 + " " + v2 + " " + v1.equals(v2));
                return v1.toString().equals(v2.toString());
            case NOTEQUAL:
                return !v1.toString().equals(v2.toString());
            case PLUS:
                return new StringBuilder(v1).append(v2);
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new StringBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }
}
