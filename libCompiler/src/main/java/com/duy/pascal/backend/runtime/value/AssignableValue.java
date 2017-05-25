package com.duy.pascal.backend.runtime.value;

import com.duy.pascal.backend.runtime.references.Reference;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public interface AssignableValue extends RuntimeValue {

    Reference getReference(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
