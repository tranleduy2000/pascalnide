package com.duy.pascal.interperter.ast.runtime.operators.number;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.BinaryOperatorNode;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.parsing.operator.DivisionByZeroException;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class IntegerBinaryOperatorNode extends BinaryOperatorNode {

    public IntegerBinaryOperatorNode(RuntimeValue operon1, RuntimeValue operon2,
                                     OperatorTypes operator, LineNumber line) {
        super(operon1, operon2, operator, line);
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        switch (operatorType) {
            case EQUALS:
            case GREATEREQ:
            case GREATERTHAN:
            case LESSEQ:
            case LESSTHAN:
            case NOTEQUAL:
                return new RuntimeType(BasicType.Boolean, false);
            case DIVIDE:
                return new RuntimeType(BasicType.Double, false);
            default:
                return new RuntimeType(BasicType.Integer, false);
        }
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        int left = Integer.parseInt(String.valueOf(value1));
        int right = Integer.parseInt(String.valueOf(value2));
        switch (operatorType) {
            case AND:
                return left & right;
            case DIV:
                if (right == 0) {
                    throw new DivisionByZeroException(line);
                }
                return left / right;
            case DIVIDE:
                if (right == 0) {

                    throw new DivisionByZeroException(line);
                }
                return (double) left / (double) right;
            case EQUALS:
                return left == right;
            case GREATEREQ:
                return left >= right;
            case GREATERTHAN:
                return left > right;
            case LESSEQ:
                return left <= right;
            case LESSTHAN:
                return left < right;
            case MINUS:
                return left - right;
            case MOD:
                return left % right;
            case MULTIPLY:
                return left * right;
            case NOTEQUAL:
                return left != right;
            case OR:
                return left | right;
            case PLUS:
                return left + right;
            case SHIFTLEFT:
                return left << right;
            case SHIFTRIGHT:
                return left >> right;
            case XOR:
                return left ^ right;
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
            return new IntegerBinaryOperatorNode(
                    leftNode.compileTimeExpressionFold(context),
                    rightNode.compileTimeExpressionFold(context), operatorType,
                    line);
        }
    }


}
