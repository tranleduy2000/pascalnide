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

package com.duy.pascal.backend.ast.runtime_value;

import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionOnStack extends VariableContext {

    private HashMap<String, Object> mapVars = new HashMap<>();

    private ArrayList<String> localVarsName = new ArrayList<>();
    private ArrayList<String> paramsName = new ArrayList<>();
    /**
     * prototype method
     */
    private FunctionDeclaration prototype;
    private VariableContext parentContext;
    private RuntimeExecutableCodeUnit<?> main;
    @SuppressWarnings("rawtypes")
    private HashMap<String, PascalReference> referenceVariables;

    @SuppressWarnings("rawtypes")
    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutableCodeUnit<?> main, FunctionDeclaration declaration,
                           Object[] arguments) {
        this.prototype = declaration;
        this.parentContext = parentContext;
        this.main = main;
        for (VariableDeclaration v : prototype.declarations.variables) {
            v.initialize(mapVars);
            localVarsName.add(v.getName());
        }
        referenceVariables = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (prototype.argumentTypes[i].writable) {
                referenceVariables.put(prototype.argumentNames[i], (PascalReference) arguments[i]);
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

    public FunctionDeclaration getCurrentFunction() {
        return prototype;
    }

    public RuntimeExecutableCodeUnit<?> getMain() {
        return main;
    }

    public ArrayList<String> getLocalVarsName() {
        return localVarsName;
    }

    public Object execute() throws RuntimePascalException {
        prototype.instructions.execute(this, main);
        //indexOf result of prototype, name of variable is name of prototype
        return mapVars.get(prototype.name);
    }

    /**
     * Global variable of function
     */
    @Override
    public Object getLocalVar(String name) throws RuntimePascalException {
        if (mapVars.containsKey(name)) {
            return mapVars.get(name);
        } else if (referenceVariables.containsKey(name)) {
            return referenceVariables.get(name).get();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(String name, Object val) {
        if (mapVars.containsKey(name)) {
            mapVars.put(name, val);
        } else if (referenceVariables.containsKey(name)) {
            referenceVariables.get(name).set(val);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getUserDefineVariableNames() {
        List<String> vars = new ArrayList<>();
        vars.addAll(paramsName);
        vars.addAll(localVarsName);
        return vars;
    }

    @Override
    public List<String> getAllVariableNames() {
        return null;
    }

    @Override
    public HashMap<String, ? extends Object> getMapVars() {
        HashMap<String, Object> hashMap = new HashMap<>(mapVars);
        hashMap.putAll(referenceVariables);
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
        return prototype.getName();
    }
}
