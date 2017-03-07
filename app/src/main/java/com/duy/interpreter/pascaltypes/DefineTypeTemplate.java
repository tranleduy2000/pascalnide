package com.duy.interpreter.pascaltypes;

import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.runtime.variables.ContainsVariables;

import java.util.HashMap;

/**
 * Created by Duy on 06-Mar-17.
 */

public class DefineTypeTemplate implements ContainsVariables {
    private HashMap<String, Object> variables = new HashMap<>();

    public DefineTypeTemplate(Object... objects) {
        variables.clear();
//        for (VariableDeclaration o : objects) {
//            variables.put(o.name, o.initialValue);
//        }
    }

    @Override
    public Object get_var(String name) {
        name = name.intern();
        return variables.get(name);
    }

    @Override
    public void set_var(String name, Object val) {
        if (variables.get(name) != null) {
            variables.put(name, val);
        }
    }

    @Override
    public ContainsVariables clone() {
        VariableDeclaration[] objects = new VariableDeclaration[variables.size()];
        for (int i = 0; i <objects.length; i++) {
        }
        return new DefineTypeTemplate(objects);
    }

}
