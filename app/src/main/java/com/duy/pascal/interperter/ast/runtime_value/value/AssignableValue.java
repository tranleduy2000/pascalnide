package com.duy.pascal.interperter.ast.runtime_value.value;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime_value.references.Reference;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public interface AssignableValue extends RuntimeValue {

    Reference getReference(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
