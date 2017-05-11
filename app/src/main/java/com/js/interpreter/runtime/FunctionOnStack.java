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
    private HashMap<String, Object> local_variables = new HashMap<>();
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
    private HashMap<String, PascalReference> reference_variables;
    private boolean procedure = false;

    @SuppressWarnings("rawtypes")
    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutable<?> main, FunctionDeclaration declaration,
                           Object[] arguments) {
        this.procedure = declaration.isProcedure();
        this.prototype = declaration;
        this.parentContext = parentContext;
        this.main = main;
        for (VariableDeclaration v : prototype.declarations.variables) {
            v.initialize(local_variables);
            listNameLocalVariable.add(v.get_name());
        }
        reference_variables = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (prototype.argument_types[i].writable) {
                reference_variables.put(prototype.argumentNames[i],
                        (PascalReference) arguments[i]);
            } else {
                local_variables.put(prototype.argumentNames[i], arguments[i]);
            }
        }
        this.parentContext = parentContext;
        this.prototype = declaration;
    }

    public HashMap<String, Object> getLocal_variables() {
        return local_variables;
    }

    public FunctionDeclaration getCurrentFunction() {
        return prototype;
    }

    public RuntimeExecutable<?> getMain() {
        return main;
    }

    public HashMap<String, PascalReference> getReference_variables() {
        return reference_variables;
    }

    public ArrayList<String> getListNameLocalVariable() {
        return listNameLocalVariable;
    }

    public Object execute() throws RuntimePascalException {
        System.out.println("Function call: " + prototype.getName());
        prototype.instructions.execute(this, main);
        //get result of prototype, name of variable is name of prototype
        return local_variables.get(prototype.name);
    }

    /**
     * Global variable of function
     */
    @Override
    public Object getLocalVar(String name) throws RuntimePascalException {
        if (local_variables.containsKey(name)) {
            return local_variables.get(name);
        } else if (reference_variables.containsKey(name)) {
            return reference_variables.get(name).get();
        } else {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setLocalVar(String name, Object val) {
        if (local_variables.containsKey(name)) {
            local_variables.put(name, val);
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

    public boolean isProcedure() {
        return procedure;
    }
}
