package com.duy.pascal.interperter.ast.runtime.operators.number;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.UnaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class DoubleUniOperatorEval extends UnaryOperatorEval {

    public DoubleUniOperatorEval(RuntimeValue operon, OperatorTypes operator, LineInfo line) {
        super(operon, operator, line);
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
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
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);

        } else {
            return new DoubleUniOperatorEval(operon.compileTimeExpressionFold(context), operator, line);
        }
    }

}
