package com.js.interpreter.runtime;

import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public abstract class VariableContext implements ContainsVariables {

    /**
     * Global variable of function
     *
     * @param name - name of var
     * @return - value of variable
     * @throws RuntimePascalException
     */
    public abstract Object getLocalVariable(String name) throws RuntimePascalException;

    public abstract boolean setLocalVar(String name, Object val);

    @Override
    public Object getVariable(String name) throws RuntimePascalException {
        Object result = this.getLocalVariable(name);
        VariableContext parentcontext = getParentContext();
        if (result == null && parentcontext != null) {
            result = parentcontext.getVariable(name);
        }
        if (result == null) {
            System.err.println("Warning!  Fetched null variable!");
        }
        return result;
    }

    @Override
    public void setVariable(String name, Object val) {
        if (val == null) {
            System.err.println("Warning!  Setting null variable!");
        }
        if (setLocalVar(name, val)) {
            return;
        }
        VariableContext parentContext = getParentContext();
        if (parentContext != null) {
            parentContext.setVariable(name, val);
        }
    }

    public abstract VariableContext getParentContext();

    @Override
    public VariableContext clone() {
        return null;
    }

}
