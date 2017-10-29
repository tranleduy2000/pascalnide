package com.duy.pascal.interperter.debugable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.config.DebugMode;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.UnhandledPascalException;

public abstract class DebuggableNode implements Node {

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
