package com.duy.pascal.backend.ast.runtime_value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.runtime_value.value.NullValue;
import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.HashMap;
import java.util.List;

public abstract class VariableContext implements ContainsVariables {

    public abstract Object getLocalVar(String name)
            throws RuntimePascalException;

    public abstract boolean setLocalVar(String name, Object val);

    public abstract List<String> getUserDefineVariableNames();

    public abstract List<String> getAllVariableNames();

    public abstract HashMap<String, ? extends Object> getMapVars();

    @NonNull
    @Override
    public Object getVar(String name) throws RuntimePascalException {
        Object result = this.getLocalVar(name);
        VariableContext parentcontext = getParentContext();
        if (result instanceof NullValue  &&parentcontext != null){
            result = parentcontext.getVar(name);
        }
        if (result instanceof NullValue) {
            System.err.println("Warning!  Fetched null variable!");
        }
        return result;
    }

    @Override
    public void setVar(String name, Object val) {
        if (val instanceof NullValue) {
            System.err.println("Warning!  Setting null variable!");
        }
        if (setLocalVar(name, val)) {
            return;
        }
        VariableContext parentcontext = getParentContext();
        if (parentcontext != null) {
            parentcontext.setVar(name, val);
        }
    }

    public abstract VariableContext getParentContext();

    @Nullable
    @Override
    public VariableContext clone() {
        return null;
    }

}
