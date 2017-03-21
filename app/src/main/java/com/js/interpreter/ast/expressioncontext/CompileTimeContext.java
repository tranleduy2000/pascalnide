package com.js.interpreter.ast.expressioncontext;


import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.ConstantDefinition;

public interface CompileTimeContext {
    public ConstantDefinition getConstantDefinition(String ident);

    public DeclaredType getTypedefType(String ident);
}
