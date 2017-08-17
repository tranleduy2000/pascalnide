package com.duy.pascal.interperter.ast.instructions;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public class ExitInstruction extends DebuggableExecutable {
    LineInfo line;

    public ExitInstruction(LineInfo line) {
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return ExecutionResult.EXIT;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        return this;
    }


}
