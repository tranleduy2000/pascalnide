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

package com.duy.pascal.interperter.ast.node.forstatement;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.references.Reference;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.debug.CallStack;

import java.util.LinkedList;

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForEnumNode extends DebuggableNode {
    @NonNull
    private Node mCommand;
    private boolean mIsDownto;
    @NonNull
    private AssignableValue mTempVar;
    @NonNull
    private RuntimeValue mFirst;
    @NonNull
    private RuntimeValue mLast;
    @NonNull
    private LineNumber mLine;
    @NonNull
    private EnumGroupType mEnumGroupType;


    public ForEnumNode(@NonNull AssignableValue mTempVar,
                       @NonNull RuntimeValue first, @NonNull RuntimeValue last, @NonNull Node command,
                       @NonNull EnumGroupType enumGroupType,
                       @NonNull LineNumber line, boolean downto) throws Exception {
        this.mTempVar = mTempVar;
        this.mFirst = first;
        this.mLast = last;
        this.mLine = line;
        this.mEnumGroupType = enumGroupType;
        this.mCommand = command;
        this.mIsDownto = downto;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        LinkedList<EnumElementValue> list = mEnumGroupType.getList();
        Reference<EnumElementValue> reference = mTempVar.getReference(f, main);
        Integer start = ((EnumElementValue) this.mFirst.getValue(f, main)).getIndex();
        Integer end = ((EnumElementValue) this.mLast.getValue(f, main)).getIndex();
        if (mIsDownto) {
            forLoop:
            for (int i = end; i >= start; i--) {
                reference.set(list.get(i));
                if (main.isDebug()) main.getDebugListener().onValueVariableChanged(new CallStack(f));

                ExecutionResult result = mCommand.visit(f, main);
                switch (result) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break forLoop;
                    case CONTINUE:
                }
            }
        } else {
            forLoop:
            for (int i = start; i <= end; i++) {
                reference.set(list.get(i));
                if (main.isDebug()) main.getDebugListener().onValueVariableChanged(new CallStack(f));

                ExecutionResult result = mCommand.visit(f, main);
                switch (result) {
                    case EXIT:
                        return ExecutionResult.EXIT;
                    case BREAK:
                        break forLoop;
                    case CONTINUE:
                }
            }
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public LineNumber getLineNumber() {
        return mLine;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        Node comm = mCommand.compileTimeConstantTransform(c);
//        return new ForDowntoStatement(first, comp, inc, comm, line);
        return null;
    }
}
