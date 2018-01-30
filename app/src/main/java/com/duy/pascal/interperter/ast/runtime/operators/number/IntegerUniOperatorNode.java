package com.duy.pascal.interperter.ast.runtime.operators.number;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.UnaryOperatorNode;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class IntegerUniOperatorNode extends UnaryOperatorNode {

    public IntegerUniOperatorNode(RuntimeValue operon, OperatorTypes operator, LineNumber line) {
        super(operon, operator, line);
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
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
                throw new InternalInterpreterException(lineNumber);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, lineNumber);
        } else {
            return new IntegerUniOperatorNode(childNode.compileTimeExpressionFold(context), operator,
                    lineNumber);
        }
    }

}
