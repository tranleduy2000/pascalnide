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

package com.duy.pascal.interperter.ast.instructions.conditional.forstatement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.debug.CallStack;

import java.util.LinkedList;

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForEnumStatement extends DebuggableExecutable {
    private Executable command;
    private boolean downto;
    private AssignableValue mTempVar;
    private RuntimeValue first;
    private RuntimeValue last;
    private LineInfo line;
    private EnumGroupType mEnumGroupType;


    public ForEnumStatement(ExpressionContext f, AssignableValue mTempVar,
                            RuntimeValue first, RuntimeValue last, Executable command,
                            EnumGroupType enumGroupType,
                            LineInfo line, boolean downto) throws ParsingException {
        this.mTempVar = mTempVar;
        this.first = first;
        this.last = last;
        this.line = line;
        this.mEnumGroupType = enumGroupType;
        this.command = command;
        this.downto = downto;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        LinkedList<EnumElementValue> list = mEnumGroupType.getList();
        Reference<EnumElementValue> reference = mTempVar.getReference(f, main);
        Integer start = ((EnumElementValue) this.first.getValue(f, main)).getIndex();
        Integer end = ((EnumElementValue) this.last.getValue(f, main)).getIndex();
        if (downto) {
            forLoop:
            for (int i = end; i >= start; i--) {
                reference.set(list.get(i));
                if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));

                ExecutionResult result = command.execute(f, main);
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
                if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(f));

                ExecutionResult result = command.execute(f, main);
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
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Executable comm = command.compileTimeConstantTransform(c);
//        return new ForDowntoStatement(first, comp, inc, comm, line);
        return null;
    }
}
