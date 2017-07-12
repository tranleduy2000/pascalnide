package com.duy.pascal.backend.ast.expressioncontext;


import com.duy.pascal.backend.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.backend.declaration.lang.types.Type;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String identifier);

    Type getTypeDef(String identifier);
}
