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

package com.duy.pascal.interperter.ast.instructions.assign_statement;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * a -= b
 * Subtract b from a, and stores result in a
 */
public class MinusAssignStatement extends AssignNodeImpl {

    public MinusAssignStatement(@NonNull AssignableValue left, @NonNull RuntimeValue minusOp,
                                LineInfo line) throws Exception {
        super(left, minusOp, line);
    }

    public MinusAssignStatement(@NonNull ExpressionContext f,
                                @NonNull AssignableValue left, RuntimeValue value,
                                LineInfo line) throws Exception {
        super(f, left, OperatorTypes.MINUS, value, line);
    }

    @Override
    public AssignNode compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        return new MinusAssignStatement(mLeftNode, mOperator.compileTimeExpressionFold(c), mLine);
    }
}
