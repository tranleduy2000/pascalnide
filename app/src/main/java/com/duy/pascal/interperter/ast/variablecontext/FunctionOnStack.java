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

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.NullValue;
import com.duy.pascal.interperter.config.ProgramMode;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration.RESULT_VAR;

public class FunctionOnStack extends VariableContext {
    private HashMap<Name, Object> mapVars = new HashMap<>();
    private HashMap<Name, PascalReference> mapReferences = new HashMap<>();

    private ArrayList<Name> localVarsName = new ArrayList<>();
    private ArrayList<Name> paramsName = new ArrayList<>();

    private FunctionDeclaration prototype;

    private VariableContext parentContext;

    private RuntimeExecutableCodeUnit<?> main;


    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutableCodeUnit<?> main, FunctionDeclaration declaration,
                           Object[] arguments) {
        this.prototype = declaration;
        this.parentContext = parentContext;
        this.main = main;
        for (VariableDeclaration v : prototype.declaration.variables) {
            v.initialize(mapVars);
            localVarsName.add(v.getName());
        }
        for (int i = 0; i < arguments.length; i++) {
            if (prototype.argumentTypes[i].writable) {
                mapReferences.put(prototype.argumentNames[i], (PascalReference) arguments[i]);
            } else {
                mapVars.put(prototype.argumentNames[i], arguments[i]);
            }
            paramsName.add(prototype.argumentNames[i]);
        }
        this.parentContext = parentContext;
        this.prototype = declaration;
    }

    public FunctionDeclaration getPrototype() {
        return prototype;
    }


    public RuntimeExecutableCodeUnit<?> getMain() {
        return main;
    }

    public ArrayList<Name> getLocalVarsName() {
        return localVarsName;
    }

    public Object visit() throws RuntimePascalException {
        prototype.instructions.visit(this, main);
        //get result of prototype, name of variable is name of prototype
        if (main.getDeclaration().getConfig().getMode() == ProgramMode.DELPHI) {
            return mapVars.get(RESULT_VAR);
        } else {
            return mapVars.get(prototype.name);
        }
    }

    /**
     * Global variable of prototype
     */
    @NonNull
    @Override
    public Object getLocalVar(Name name) throws RuntimePascalException {
        if (mapVars.containsKey(name)) {
            return mapVars.get(name);
        } else if (mapReferences.containsKey(name)) {
            return mapReferences.get(name).get();
        } else {
            return NullValue.get();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(Name name, Object val) {
        if (mapVars.containsKey(name)) {
            mapVars.put(name, val);
        } else if (mapReferences.containsKey(name)) {
            mapReferences.get(name).set(val);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Name> getUserDefineVariableNames() {
        ArrayList<Name> vars = new ArrayList<>(paramsName);
        vars.addAll(localVarsName);
        return vars;
    }

    @Override
    public List<String> getAllVariableNames() {
        return null;
    }

    @Override
    public HashMap<Name, Object> getMapVars() {
        HashMap<Name, Object> hashMap = new HashMap<>(mapVars);
        hashMap.putAll(mapReferences);
        return hashMap;
    }

    @Override
    public VariableContext clone() {
        return null;
    }

    @Override
    public VariableContext getParentContext() {
        return parentContext;
    }

    public boolean isProcedure() {
        return prototype.isProcedure();
    }

    @Override
    public String toString() {
        return prototype.getName().getOriginName();
    }
}
