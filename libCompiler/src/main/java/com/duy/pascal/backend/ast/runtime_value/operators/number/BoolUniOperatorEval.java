package com.duy.pascal.backend.ast.runtime_value.operators.number;

import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.operators.UnaryOperatorEval;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.data_types.BasicType;
import com.duy.pascal.backend.data_types.OperatorTypes;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime_exception.internal.InternalInterpreterException;

public class BoolUniOperatorEval extends UnaryOperatorEval {

    public BoolUniOperatorEval(RuntimeValue operon, OperatorTypes operator, LineInfo line) {
        super(operon, operator, line);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Boolean, false);
    }

    @Override
    public Object operate(Object value) throws PascalArithmeticException, InternalInterpreterException {
        switch (operator) {
            case NOT:
                return !(boolean) value;
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
            return new BoolUniOperatorEval(operon.compileTimeExpressionFold(context), operator,
                    line);
        }
    }
    @Override
    public boolean canDebug() {
        return true;
    }
}
