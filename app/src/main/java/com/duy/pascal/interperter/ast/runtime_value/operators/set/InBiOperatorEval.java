/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.ast.runtime_value.operators.set;


import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.CompileException;
import com.duy.pascal.interperter.exceptions.runtime.PascalArithmeticException;
import com.duy.pascal.interperter.exceptions.runtime.internal.InternalInterpreterException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.util.LinkedList;

/**
 * The IN operator checks to see whether an element is in an array
 */
public class InBiOperatorEval extends BinaryOperatorEval {

    public InBiOperatorEval(RuntimeValue operon1, RuntimeValue operon2,
                            OperatorTypes operator, LineInfo line) {
        super(operon1, operon2, operator, line);
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        switch (operator_type) {
            case IN:
                return new RuntimeType(BasicType.Boolean, false);
        }
        return null;
    }

    @Override
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, InternalInterpreterException, CompileException {
        //if the type of value2 is enum or set
        if (value2 instanceof LinkedList) {
            LinkedList v2 = (LinkedList) value2;
            for (Object o : v2) {
                if (o instanceof Number) {
                    if (value1 instanceof Number) {
                        if (((Number) o).longValue() == ((Number) value1).longValue()){
                            return true;
                        }
                    }
                } else {
                    if (o.equals(value1)) {
                        return true;
                    }
                }
            }
            return false;

        }
        //array type
        else if (value2 instanceof Object[]) {
            Object[] objects = (Object[]) value2;
            for (Object object : objects) {
                if (value1.equals(object)) return true;
            }
        } else {
            throw new CompileException();
        }
        return false;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);
        } else {
            return new InBiOperatorEval(
                    operon1.compileTimeExpressionFold(context),
                    operon2.compileTimeExpressionFold(context), operator_type,
                    line);
        }
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public boolean canDebug() {
        return true;
    }
}
