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

package com.duy.pascal.interperter.ast.node.assign_statement;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * a /= b
 * Divides a through b, and stores the result in a
 */
public class DivAssignStatement extends AssignNodeImpl {

    public DivAssignStatement(@NonNull AssignableValue left, @NonNull RuntimeValue operator,
                              LineNumber line) throws Exception {
        super(left, operator, line);
    }

    public DivAssignStatement(@NonNull ExpressionContext f,
                              @NonNull AssignableValue left, RuntimeValue value,
                              LineNumber line) throws Exception {
        super(f, left, OperatorTypes.DIVIDE, value, line);
        Type leftType = left.getRuntimeType(f).getRawType();
        if (!(leftType.equals(BasicType.Double) || leftType.equals(BasicType.Float))) {
            throw new UnConvertibleTypeException(left, BasicType.Double, leftType, f);
        }
    }


    @Override
    public String toString() {
        return mLeftNode + " := " + mOperator;
    }


    @Override
    public AssignNode compileTimeConstantTransform(CompileTimeContext c) throws Exception {
        return new DivAssignStatement(mLeftNode, mOperator.compileTimeExpressionFold(c), mLine);
    }
}
