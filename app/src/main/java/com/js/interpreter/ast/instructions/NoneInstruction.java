package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class NoneInstruction extends DebuggableExecutable {
    LineInfo line;

    public NoneInstruction(LineInfo line) {
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return this;
    }

}
