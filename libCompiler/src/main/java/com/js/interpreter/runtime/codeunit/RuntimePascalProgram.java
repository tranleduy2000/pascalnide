package com.js.interpreter.runtime.codeunit;

import com.duy.pascal.backend.debugable.DebugListener;
import com.js.interpreter.codeunit.PascalProgram;
import com.js.interpreter.codeunit.RunMode;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RuntimePascalProgram extends RuntimeExecutableCodeUnit<PascalProgram> {

    VariableContext main;

    public RuntimePascalProgram(PascalProgram p) {
        super(p);
    }

    public RuntimePascalProgram(PascalProgram p, DebugListener debugListener) {
        super(p, debugListener);
    }

    @Override
    public void runImpl() throws RuntimePascalException {
        this.mode = RunMode.RUNNING;
        definition.main.execute(this, this);
    }

    @Override
    public VariableContext getParentContext() {
        return null;
    }
}
