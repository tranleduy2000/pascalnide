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
    private HashMap<String, Object> mapLocalVariable = new HashMap<>();
    /**
     * list name of map variable, if you want get all variable, map can not do
     */
    private ArrayList<String> listNameLocalVariable = new ArrayList<>();
    /**
     * currentFunction method
     */
    private FunctionDeclaration currentFunction;
    private VariableContext parentContext;
    private RuntimeExecutable<?> main;
    @SuppressWarnings("rawtypes")
    private
    HashMap<String, VariableBoxer> reference_variables;

    @SuppressWarnings("rawtypes")
    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutable<?> main, FunctionDeclaration declaration,
                           Object[] arguments) {
        this.currentFunction = declaration;
        this.parentContext = parentContext;
        this.main = main;
        for (VariableDeclaration v : currentFunction.declarations.variables) {
            v.initialize(mapLocalVariable);
            listNameLocalVariable.add(v.get_name());
        }
        reference_variables = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (currentFunction.argument_types[i].writable) {
                reference_variables.put(currentFunction.argument_names[i], (VariableBoxer) arguments[i]);
            } else {
                mapLocalVariable.put(currentFunction.argument_names[i], arguments[i]);
            }
        }
        this.parentContext = parentContext;
        this.currentFunction = declaration;
    }

    public HashMap<String, Object> getMapLocalVariable() {
        return mapLocalVariable;
    }

    public FunctionDeclaration getCurrentFunction() {
        return currentFunction;
    }

    public RuntimeExecutable<?> getMain() {
        return main;
    }

    public HashMap<String, VariableBoxer> getReference_variables() {
        return reference_variables;
    }

    public ArrayList<String> getListNameLocalVariable() {
        return listNameLocalVariable;
    }

    public Object execute() throws RuntimePascalException {
        System.out.println("Function call: " + currentFunction.getName());
        currentFunction.instructions.execute(this, main);
//		return mapLocalVariable.get("result");
        //get result of currentFunction, name of variable is name of currentFunction
        return mapLocalVariable.get(currentFunction.name);
    }

    @Override
    public Object getGlobalVariable(String name) throws RuntimePascalException {
        if (mapLocalVariable.containsKey(name)) {
            return mapLocalVariable.get(name);
        } else if (reference_variables.containsKey(name)) {
            return reference_variables.get(name).get();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(String name, Object val) {
        if (mapLocalVariable.containsKey(name)) {
            mapLocalVariable.put(name, val);
        } else if (reference_variables.containsKey(name)) {
            reference_variables.get(name).set(val);
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

}
