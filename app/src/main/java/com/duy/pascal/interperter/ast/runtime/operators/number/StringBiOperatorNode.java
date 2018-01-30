package com.duy.pascal.interperter.ast.runtime.operators.number;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.BinaryOperatorNode;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class StringBiOperatorNode extends BinaryOperatorNode {

    public StringBiOperatorNode(RuntimeValue operon1, RuntimeValue operon2,
                                OperatorTypes operator, LineNumber line) {
        super(operon1, operon2, operator, line);
    }


    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        switch (operatorType) {
            case EQUALS:
            case NOTEQUAL:
            case LESSEQ:
            case LESSTHAN:
            case GREATEREQ:
            case GREATERTHAN:
                return new RuntimeType(BasicType.Boolean, false);
            default:
                return new RuntimeType(BasicType.StringBuilder, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        String left = value1.toString();
        String right = value2.toString();
        switch (operatorType) {
            case EQUALS:
                return left.equals(right); //left == right
            case NOTEQUAL:
                return !left.equals(right); //left != right
            case LESSTHAN:
                return left.compareTo(right) < 0; // left < right
            case LESSEQ:
                return left.compareTo(right) <= 0; //left <= right
            case GREATEREQ:
                return left.compareTo(right) >= 0; //left >= right
            case GREATERTHAN:
                return left.compareTo(right) > 0; //left > right
            case PLUS:
                return new StringBuilder(left).append(right);
            default:
                throw new InternalInterpreterException(line);
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);
        } else {
            return new StringBiOperatorNode(
                    leftNode.compileTimeExpressionFold(context),
                    rightNode.compileTimeExpressionFold(context),
                    operatorType,
                    line);
        }
    }

}
