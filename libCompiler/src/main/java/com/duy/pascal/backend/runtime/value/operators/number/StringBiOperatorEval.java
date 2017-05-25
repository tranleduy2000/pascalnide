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
import com.duy.pascal.backend.runtime.exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime.exception.internal.InternalInterpreterException;

public class StringBiOperatorEval extends BinaryOperatorEvaluation {

    public StringBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                                OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
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
        String v1 = value1.toString();
        String v2 = value2.toString();
        switch (operator_type) {
            case EQUALS:
                return v1.equals(v2);
            case NOTEQUAL:
                return !v1.equals(v2);
            case LESSTHAN:
                return v1.compareTo(v2) < 0;
            case LESSEQ:
                return v1.compareTo(v2) <= 0;
            case GREATEREQ:
                return v1.compareTo(v2) >= 0;
            case GREATERTHAN:
                return v1.compareTo(v2) > 0;
            case PLUS:
                return new StringBuilder(v1).append(v2);
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new StringBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context),
                    operator_type,
                    line);
        }
    }
}
