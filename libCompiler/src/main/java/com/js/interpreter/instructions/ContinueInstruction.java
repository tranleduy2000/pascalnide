package com.js.interpreter.instructions;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class ContinueInstruction extends DebuggableExecutable {
    private LineInfo line;

    public ContinueInstruction(LineInfo line) {
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return ExecutionResult.CONTINUE;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c) {
        return this;
    }

}
