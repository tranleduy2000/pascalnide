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

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.debug.CallStack;

/**
 * Created by Duy on 10/29/2017.
 */

public abstract class AssignNodeImpl<T extends OperatorTypes> extends DebuggableNode implements AssignNode {
    protected AssignableValue mLeftNode;
    protected RuntimeValue mOperator;
    protected LineInfo mLine;

    public AssignNodeImpl(@NonNull AssignableValue left, @NonNull RuntimeValue operator,
                          LineInfo line) throws Exception {
        this.mLeftNode = left;
        this.mOperator = operator;
        this.mLine = line;
    }

    public AssignNodeImpl(@NonNull ExpressionContext f,
                          @NonNull AssignableValue left,
                          @NonNull T operator,
                          @NonNull RuntimeValue value,
                          @NonNull LineInfo line) throws Exception {
        this.mLeftNode = left;
        this.mLine = line;
        this.mOperator = BinaryOperatorEval.generateOp(f, left, value, operator, line);
    }


    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit main) throws RuntimePascalException {

        Reference ref = mLeftNode.getReference(context, main);
        Object v = this.mOperator.getValue(context, main);
        ref.set(v);

        if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(context));

        return ExecutionResult.NOPE;
    }

    @Override
    public String toString() {
        return mLeftNode + " := " + mOperator;
    }

    @Override
    public LineInfo getLineNumber() {
        return this.mLine;
    }
}
