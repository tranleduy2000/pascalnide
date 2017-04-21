package com.duy.pascal.backend.debugable;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

public abstract class DebuggableReturnsValue implements ReturnsValue {

    protected ReturnsValue[] outputFormat;

    @Override
    public ReturnsValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(ReturnsValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        try {
            System.out.println(getClass().getSimpleName() + " " + getLine());
            if (main != null) {
                if (main.getDebugListener() != null && main.isDebugMode()) {
                    main.getDebugListener().onLine(getLine());
                }      main.incStack(getLine());
                main.scriptControlCheck(getLine());

            }
            Object valueImpl = getValueImpl(f, main);
            if (main != null) {
                main.decStack();
            }
            return valueImpl;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLine(), e);
        }
    }

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

}
