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

package com.js.interpreter.instructions.with_statement;

import com.js.interpreter.instructions.FieldReference;
import com.duy.pascal.backend.runtime.value.FieldAccess;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

import java.util.HashMap;

public class WithOnStack extends VariableContext {
    private WithStatement withStatement;
    private VariableContext parentContext;
    private RuntimeExecutableCodeUnit<?> main;
    @SuppressWarnings("rawtypes")
    private HashMap<String, FieldAccess> fieldsMap;

    @SuppressWarnings("rawtypes")
    WithOnStack(VariableContext parentContext,
                RuntimeExecutableCodeUnit<?> main, WithStatement declaration) {
        this.withStatement = declaration;
        this.parentContext = parentContext;
        this.main = main;

        fieldsMap = new HashMap<>();
        for (int i = 0; i < withStatement.variableDeclarations.size(); i++) {
            fieldsMap.put(withStatement.variableDeclarations.get(i).getName(),
                    (FieldAccess) withStatement.fields.get(i));
        }
        this.parentContext = parentContext;
        this.withStatement = declaration;
    }

    public RuntimeExecutableCodeUnit<?> getMain() {
        return main;
    }

    public void execute() throws RuntimePascalException {
        withStatement.instructions.execute(this, main);
    }

    /**
     * Global variable of function
     */
    @Override
    public Object getLocalVar(String name) throws RuntimePascalException {
        if (fieldsMap.containsKey(name)) {
            return fieldsMap.get(name).getReferenceImpl(parentContext, main);
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(String name, Object val) {
        if (fieldsMap.containsKey(name)) {
            try {
                FieldReference fieldReference = (FieldReference)
                        fieldsMap.get(name).getReferenceImpl(parentContext, main);
                fieldReference.set(val);
            } catch (RuntimePascalException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    @Override
    public VariableContext clone() {
        return null;
    }

    @Override
    public VariableContext getParentContext() {
        return parentContext;
    }
}
