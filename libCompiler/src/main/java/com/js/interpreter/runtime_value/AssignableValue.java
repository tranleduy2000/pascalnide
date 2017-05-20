package com.js.interpreter.runtime_value;

import com.js.interpreter.runtime.references.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface AssignableValue extends RuntimeValue {

    Reference getReference(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

}
