package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;

public interface SetValueExecutable extends Executable {
    void setAssignedValue(RuntimeValue value);

    @Override
    SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException;
}
