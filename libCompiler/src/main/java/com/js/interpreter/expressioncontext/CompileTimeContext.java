package com.js.interpreter.expressioncontext;


import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ConstantDefinition;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String ident);

    DeclaredType getTypedefType(String ident);
}
