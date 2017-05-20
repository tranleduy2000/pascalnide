package com.js.interpreter.runtime_value;

import com.js.interpreter.runtime.references.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface AssignableValue extends RuntimeValue {

    Reference getReference(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
