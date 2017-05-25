package com.js.interpreter.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public interface Executable {
    LineInfo getLineNumber();

    /*
     * This should not modify the state of the object, unless it is a plugin
     * call.
     */
    ExecutionResult execute(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException;
}
