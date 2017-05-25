package com.js.interpreter.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.runtime.value.RuntimeValue;

public interface SetValueExecutable extends Executable {
    void setAssignedValue(RuntimeValue value);

    @Override
    SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException;
}
