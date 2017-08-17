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
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;

import java.util.LinkedList;
import java.util.List;

/**
 * For ... in ... do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu59.html#x165-18700013.2.5
 * <p>
 * Created by Duy on 14-May-17.
 */
public class ForInStatement extends DebuggableExecutable {
    /**
     * the statement of loop
     */
    private Executable command;

    /**
     * variable identifier
     */
    private AssignableValue item;

    /**
     * enum list or expression with return type is enum
     */
    private RuntimeValue list;

    /**
     * the lineInfo in code
     */
    private LineInfo line;

    /**
     * @param item     - variable identifier
     * @param enumList - enum type or expression with type is enum
     * @param command  - command for execute
     */
    public ForInStatement(AssignableValue item,
                          RuntimeValue enumList, //enum
                          Executable command,
                          LineInfo line) {
        this.item = item;
        this.list = enumList;
        this.line = line;
        this.command = command;
    }

    /**
     * Execute for statement
     * I specified the enum by a {@link LinkedList}, see {@link EnumGroupType}
     */
    @Override
    @SuppressWarnings("unchecked")
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {

        //get value of enum
        Object value = this.list.getValue(context, main);
        if (value instanceof List) {
            List list = (List) value;
            //get reference if variable
            Reference reference = this.item.getReference(context, main);
            //for each all item in list
            for (Object item : list) {
                reference.set(item); //set value for variable identifier
                //execute command of for loop and receive a result
                ExecutionResult result = command.execute(context, main);
                //check exit, break, continue command
                switch (result) {
                    case CONTINUE:
                        continue;
                    case BREAK:
                        break;
                    case EXIT:
                        return ExecutionResult.EXIT;
                }
            }
        } else { //array
            Object[] list = (Object[]) value;
            //get reference if variable
            Reference reference = this.item.getReference(context, main);
            //for each all item in list
            for (Object item : list) {
                reference.set(item); //set value for variable identifier
                //execute command of for loop and receive a result
                ExecutionResult result = command.execute(context, main);
                //check exit, break, continue command
                switch (result) {
                    case CONTINUE:
                        continue;
                    case BREAK:
                        break;
                    case EXIT:
                        return ExecutionResult.EXIT;

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
            throws Exception {
        return new ForInStatement(item, list, command, line);
    }
}
