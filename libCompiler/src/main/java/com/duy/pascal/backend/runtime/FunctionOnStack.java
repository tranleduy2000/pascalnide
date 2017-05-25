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

package com.duy.pascal.backend.runtime;

import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.references.PascalReference;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionOnStack extends VariableContext {
    /**
     * map variable
     */
    private HashMap<String, Object> localVariables = new HashMap<>();
    /**
     * list name of map variable, if you want indexOf all variable, map can not do
     */
    private ArrayList<String> listNameLocalVariable = new ArrayList<>();

    public FunctionDeclaration getPrototype() {
        return prototype;
    }

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
            v.initialize(localVariables);
            listNameLocalVariable.add(v.getName());
        }
        referenceVariables = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (prototype.argumentTypes[i].writable) {
                referenceVariables.put(prototype.argumentNames[i], (PascalReference) arguments[i]);
            } else {
                localVariables.put(prototype.argumentNames[i], arguments[i]);
            }
        }
        this.parentContext = parentContext;
        this.prototype = declaration;
    }

    public FunctionDeclaration getCurrentFunction() {
        return prototype;
    }

    public RuntimeExecutableCodeUnit<?> getMain() {
        return main;
    }

    public ArrayList<String> getListNameLocalVariable() {
        return listNameLocalVariable;
    }

    public Object execute() throws RuntimePascalException {
        prototype.instructions.execute(this, main);
        //indexOf result of prototype, name of variable is name of prototype
        return localVariables.get(prototype.name);
    }

    /**
     * Global variable of function
     */
    @Override
    public Object getLocalVar(String name) throws RuntimePascalException {
        if (localVariables.containsKey(name)) {
            return localVariables.get(name);
        } else if (referenceVariables.containsKey(name)) {
            return referenceVariables.get(name).get();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(String name, Object val) {
        if (localVariables.containsKey(name)) {
            localVariables.put(name, val);
        } else if (referenceVariables.containsKey(name)) {
            referenceVariables.get(name).set(val);
        } else {
            return false;
        }
        return true;
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
}
