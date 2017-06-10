package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.ast.codeunit.DebugMode;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.UnhandledPascalException;

public abstract class DebuggableExecutable implements Executable {
    @Override
    public ExecutionResult execute(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (main.isDebug()) {
                main.getDebugListener().onLine(this, getLineNumber());
            }
            main.scriptControlCheck(getLineNumber());
            //backup mode
            boolean last = main.isDebug();
            if (main.isDebug()) {
                if (main.getDebugMode().equals(DebugMode.STEP_OVER)) {
                    main.setDebug(false);
                }
            }

            main.incStack(getLineNumber());

            //execute code
            ExecutionResult result = executeImpl(context, main);

            //restore mode
            main.setDebug(last);
            main.decStack();
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
