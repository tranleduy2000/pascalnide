package com.js.interpreter.runtime;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionOnStack extends VariableContext {
    /**
     * map variable
     */
    private HashMap<String, Object> localVariables = new HashMap<>();
    /**
     * list name of map variable, if you want get all variable, map can not do
     */
    private ArrayList<String> listNameLocalVariable = new ArrayList<>();
    /**
     * prototype method
     */
    private FunctionDeclaration prototype;
    private VariableContext parentContext;
    private RuntimeExecutable<?> main;
    @SuppressWarnings("rawtypes")
    private HashMap<String, PascalReference> referenceVariables;

    @SuppressWarnings("rawtypes")
    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutable<?> main, FunctionDeclaration declaration,
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

    public RuntimeExecutable<?> getMain() {
        return main;
    }

    public ArrayList<String> getListNameLocalVariable() {
        return listNameLocalVariable;
    }

    public Object execute() throws RuntimePascalException {
        System.out.println("Function call: " + prototype.getName());
        prototype.instructions.execute(this, main);
        //get result of prototype, name of variable is name of prototype
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
