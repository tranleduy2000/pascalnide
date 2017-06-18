package com.duy.pascal.backend.ast.expressioncontext;


import com.duy.pascal.backend.declaration.value.ConstantDefinition;
import com.duy.pascal.backend.declaration.types.DeclaredType;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String ident);

    DeclaredType getTypedefType(String ident);
}
