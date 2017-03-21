package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.NopInstruction;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class WhileStatement extends DebuggableExecutable {
    ReturnsValue condition;

    Executable command;
    LineInfo line;

    public WhileStatement(ReturnsValue condition, Executable command,
                          LineInfo line) {
        this.condition = condition;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        while_loop:
        while ((Boolean) condition.getValue(f, main)) {
            switch (command.execute(f, main)) {
                case BREAK:
                    break while_loop;
                case RETURN:
                    return ExecutionResult.RETURN;
            }
        }
        return ExecutionResult.NONE;
    }

    @Override
    public String toString() {
        return "while [" + condition + "] do [" + command + ']';
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Executable comm = command.compileTimeConstantTransform(c);
        Object cond = condition.compileTimeValue(c);
        if (cond != null) {
            if (!((Boolean) cond)) {
                return new NopInstruction(line);
            } else {
                return new WhileStatement(new ConstantAccess(cond,
                        condition.getLineNumber()), comm, line);
            }
        }
        return new WhileStatement(condition, comm, line);
    }
}
