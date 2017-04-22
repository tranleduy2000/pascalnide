package com.duy.pascal.backend.debugable;

import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

/**
 * DEBUG class
 * this class can check stack size
 */
public abstract class DebuggableExecutable implements Executable {

    @Override
    public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        try {
            System.out.println(getClass().getSimpleName() + " " + getLine());
            if (main != null) {
                if (main.isDebugMode()) {
                    main.getDebugListener().onLine(getLine());
                }
                main.incStack(getLine());
                main.scriptControlCheck(getLine());
            }
            ExecutionResult result = executeImpl(f, main);
            if (main != null) {
                main.decStack();
            }
            return result;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLine(), e);
        }
    }

    public abstract ExecutionResult executeImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;
}
