package com.duy.pascal.backend.debugable;

import com.js.interpreter.ast.returnsvalue.ArrayAccess;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FieldAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.StringIndexAccess;
import com.js.interpreter.ast.returnsvalue.VariableAccess;
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
                boolean debugMode = main.isDebugMode();
                if (main.getDebugListener() != null && main.isDebugMode()) {
                    if (!(this instanceof VariableAccess
                            || this instanceof ArrayAccess
                            || this instanceof ConstantAccess
                            || this instanceof FieldAccess
                            || this instanceof StringIndexAccess)) {
                        main.getDebugListener().onLine(getLine());
                    } else {
                        main.setDebugMode(false);
                    }
                }
                main.incStack(getLine());
                main.scriptControlCheck(getLine());
                main.setDebugMode(debugMode);
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
