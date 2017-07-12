package com.duy.pascal.backend.ast.runtime_value.operators.number;


import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.operator.DivisionByZeroException;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime_exception.internal.InternalInterpreterException;
import com.duy.pascal.backend.declaration.lang.types.BasicType;
import com.duy.pascal.backend.declaration.lang.types.OperatorTypes;
import com.duy.pascal.backend.declaration.lang.types.RuntimeType;

public class DoubleBiOperatorEval extends BinaryOperatorEval {

    public DoubleBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                                OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
        switch (operator_type) {
            case EQUALS:
            case GREATEREQ:
            case GREATERTHAN:
            case LESSEQ:
            case LESSTHAN:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            default:
                return new RuntimeType(BasicType.Double, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        double v1 = Double.valueOf(String.valueOf(value1));
        double v2 = Double.valueOf(String.valueOf(value2));
        switch (operator_type) {
            case DIVIDE:
                if (Math.abs(v2) == 0d) {
                    throw new DivisionByZeroException(line);
                }
                return v1 / v2;
            case EQUALS:
                return v1 == v2;
            case GREATEREQ:
                return v1 >= v2;
            case GREATERTHAN:
                return v1 > v2;
            case LESSEQ:
                return v1 <= v2;
            case LESSTHAN:
                return v1 < v2;
            case MINUS:
                return v1 - v2;
            case MULTIPLY:
                return v1 * v2;
            case NOTEQUAL:
                return v1 != v2;
            case PLUS:
                return v1 + v2;
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);

        } else {
            return new DoubleBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }

}
