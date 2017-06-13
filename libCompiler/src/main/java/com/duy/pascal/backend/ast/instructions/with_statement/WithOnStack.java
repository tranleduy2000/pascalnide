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

package com.duy.pascal.backend.ast.instructions.with_statement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.instructions.FieldReference;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.access.FieldAccess;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.debug.CallStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WithOnStack extends VariableContext {
    private WithStatement declaration;
    @NonNull
    private VariableContext parentContext;
    private RuntimeExecutableCodeUnit<?> main;
    @SuppressWarnings("rawtypes")
    private HashMap<String, FieldAccess> fieldsMap;

    @SuppressWarnings("rawtypes")
    WithOnStack(@NonNull VariableContext parentContext,
                RuntimeExecutableCodeUnit<?> main, WithStatement declaration) {
        this.declaration = declaration;
        this.parentContext = parentContext;
        this.main = main;

        fieldsMap = new HashMap<>();
        for (FieldAccess fieldAccess : declaration.getFields()) {
            fieldsMap.put(fieldAccess.getName().toLowerCase(), fieldAccess);
        }
        this.parentContext = parentContext;
        this.declaration = declaration;
    }

    public WithStatement getDeclaration() {
        return declaration;
    }

    public HashMap<String, FieldAccess> getFieldsMap() {
        return fieldsMap;
    }

    public RuntimeExecutableCodeUnit<?> getMain() {
        return main;
    }

    public void execute() throws RuntimePascalException {
        if (main.isDebug()) main.getDebugListener().onVariableChange(new CallStack(this));
        declaration.instructions.execute(this, main);
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
    public List<String> getUserDefineVariableNames() {
        Set<String> names = fieldsMap.keySet();
        return new ArrayList<>(names);
    }

    @Override
    public List<String> getAllVariableNames() {
        return null;
    }

    @Override
    public HashMap<String, ? extends Object> getMapVars() {
        return fieldsMap;
    }

    @Nullable
    @Override
    public VariableContext clone() {
        super.clone();
        return null;
    }

    @NonNull
    @Override
    public VariableContext getParentContext() {
        return parentContext;
    }

    @Override
    public String toString() {
        return "with statement";
    }
}
