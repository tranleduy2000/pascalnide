package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.runtime_value.ConstantAccess;
import com.js.interpreter.ast.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RepeatInstruction extends DebuggableExecutable {
    Executable command;

    RuntimeValue condition;
    LineInfo line;

    public RepeatInstruction(Executable command, RuntimeValue condition,
                             LineInfo line) {
        this.command = command;
        this.condition = condition;
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        do_loop:
        do {
            switch (command.execute(context, main)) {
                case CONTINUE:
                    continue do_loop;
                case BREAK:
                    break do_loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        } while (!((Boolean) condition.getValue(context, main)));
        return ExecutionResult.NONE;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Object o = condition.compileTimeValue(c);
        if (o != null) {
            Boolean b = (Boolean) o;
            if (!b) {
                return command.compileTimeConstantTransform(c);
            } else {
                return new RepeatInstruction(
                        command.compileTimeConstantTransform(c),
                        new ConstantAccess(true, condition.getLineNumber()), line);
            }

        }
        return new RepeatInstruction(command.compileTimeConstantTransform(c),
                condition, line);
    }
}
