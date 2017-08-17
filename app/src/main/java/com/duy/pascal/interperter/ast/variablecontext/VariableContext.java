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

package com.duy.pascal.interperter.ast.variablecontext;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.duy.pascal.interperter.utils.NullSafety.isNullValue;


public abstract class VariableContext implements ContainsVariables {

    @NonNull
    public abstract Object getLocalVar(Name name)
            throws RuntimePascalException;

    public abstract boolean setLocalVar(Name name, Object val);

    public abstract ArrayList<Name> getUserDefineVariableNames();

    public abstract List<String> getAllVariableNames();

    public abstract HashMap<Name, ?> getMapVars();

    @NonNull
    @Override
    public Object getVar(Name name) throws RuntimePascalException {
        Object result = this.getLocalVar(name);
        VariableContext parentContext = getParentContext();
        if (isNullValue(result) && parentContext != null) {
            result = parentContext.getVar(name);
        }
        return result;
    }

    @Override
    public void setVar(Name name, Object val) {
        if (val instanceof NullValue) {
            System.err.println("Warning!  Setting null variable!");
        }
        if (setLocalVar(name, val)) {
            return;
        }
        VariableContext parentContext = getParentContext();
        if (parentContext != null) {
            parentContext.setVar(name, val);
        }
    }

    public abstract VariableContext getParentContext();

    @Nullable
    @Override
    public VariableContext clone() {
        return null;
    }

}
