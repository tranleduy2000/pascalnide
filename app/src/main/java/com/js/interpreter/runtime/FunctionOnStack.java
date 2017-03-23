package com.js.interpreter.runtime;

import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.HashMap;

public class FunctionOnStack extends VariableContext {
    /**
     * list variable
     */
    private HashMap<String, Object> local_variables = new HashMap<>();

    /**
     * function method
     */
    private FunctionDeclaration function;

    private VariableContext parentContext;

    private RuntimeExecutable<?> main;
    @SuppressWarnings("rawtypes")
    private
    HashMap<String, VariableBoxer> reference_variables;

    @SuppressWarnings("rawtypes")
    public FunctionOnStack(VariableContext parentContext,
                           RuntimeExecutable<?> main, FunctionDeclaration declaration,
                           Object[] arguments) {
        this.function = declaration;
        this.parentContext = parentContext;
        this.main = main;
        for (VariableDeclaration v : function.declarations.variables) {
            v.initialize(local_variables);
        }
        reference_variables = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            if (function.argument_types[i].writable) {
                reference_variables.put(function.argument_names[i], (VariableBoxer) arguments[i]);
            } else {
                local_variables.put(function.argument_names[i], arguments[i]);
            }
        }
        this.parentContext = parentContext;
        this.function = declaration;
    }

    public Object execute() throws RuntimePascalException {
        function.instructions.execute(this, main);
//		return local_variables.get("result");
        //get result of function, name of variable is name of function
        return local_variables.get(function.name);
    }

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

}
