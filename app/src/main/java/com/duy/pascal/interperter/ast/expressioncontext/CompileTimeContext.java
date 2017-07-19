package com.duy.pascal.interperter.ast.expressioncontext;


import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.types.Type;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String identifier);

    Type getTypeDef(String identifier);
}
