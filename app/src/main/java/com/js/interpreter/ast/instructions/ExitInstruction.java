package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ExitInstruction extends DebuggableExecutable {
    LineInfo line;

    public ExitInstruction(LineInfo line) {
        this.line = line;
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
//        System.out.println(this.getClass().getSimpleName());
        return ExecutionResult.EXIT;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return this;
    }


}
