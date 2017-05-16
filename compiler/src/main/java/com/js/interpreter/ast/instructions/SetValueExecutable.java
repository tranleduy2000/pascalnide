package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

public interface SetValueExecutable extends Executable {
    void setAssignedValue(ReturnValue value);

    @Override
    SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException;
}
