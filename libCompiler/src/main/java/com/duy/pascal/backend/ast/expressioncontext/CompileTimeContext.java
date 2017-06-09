package com.duy.pascal.backend.ast.expressioncontext;


import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.ast.ConstantDefinition;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String ident);

    DeclaredType getTypedefType(String ident);
}
