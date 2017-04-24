package com.js.interpreter.ast.expressioncontext;


import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.ConstantDefinition;

public interface CompileTimeContext {
    ConstantDefinition getConstantDefinition(String ident);

    DeclaredType getTypedefType(String ident);
}
