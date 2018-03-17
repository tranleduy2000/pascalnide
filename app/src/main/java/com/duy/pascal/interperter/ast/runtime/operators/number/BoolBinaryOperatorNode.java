package com.duy.pascal.interperter.ast.runtime.operators.number;


import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.BinaryOperatorNode;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class BoolBinaryOperatorNode extends BinaryOperatorNode {

    public BoolBinaryOperatorNode(RuntimeValue operon1, RuntimeValue operon2,
                                  OperatorTypes operator, LineNumber line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public boolean canDebug() {
        return true;
    }

    @Override
    public Object getValueImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        boolean value1 = (boolean) leftNode.getValue(context, main);
        if ((operatorType == OperatorTypes.AND && !value1) || (operatorType == OperatorTypes.OR && value1)) {
            return value1;
        }
        boolean value2 = (boolean) rightNode.getValue(context, main);
        return operate(value1, value2);
    }


    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return new RuntimeType(BasicType.Boolean, false);
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException {
        boolean v1 = (boolean) value1;
        boolean v2 = (boolean) value2;
        switch (operatorType) {
            case AND:
                return v1 & v2;
            case EQUALS:
                return v1 == v2;
            case NOTEQUAL:
                return v1 != v2;
            case OR:
                return v1 | v2;
            case XOR:
                return v1 ^ v2;
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
            return new BoolBinaryOperatorNode(
                    leftNode.compileTimeExpressionFold(context),
                    rightNode.compileTimeExpressionFold(context), operatorType,
                    line);
        }
    }

}
