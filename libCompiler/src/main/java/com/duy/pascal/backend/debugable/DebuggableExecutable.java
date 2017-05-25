package com.duy.pascal.backend.debugable;

import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.exception.UnhandledPascalException;

public abstract class DebuggableExecutable implements Executable {

    @Override
    public ExecutionResult execute(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (main != null) {
                if (main.isDebugMode()) {
                    main.getDebugListener().onLine(getLineNumber());
                }
                main.incStack(getLineNumber());
                main.scriptControlCheck(getLineNumber());
            }
            ExecutionResult result = executeImpl(context, main);
            if (main != null) {
                main.decStack();
            }
            return result;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        }
    }

    public abstract ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;
}
