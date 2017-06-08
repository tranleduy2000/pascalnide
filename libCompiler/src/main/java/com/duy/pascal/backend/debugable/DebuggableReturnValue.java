package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.UnhandledPascalException;

public abstract class DebuggableReturnValue implements RuntimeValue {

    protected RuntimeValue[] outputFormat;

    public abstract boolean canDebug();

    @Override
    public RuntimeValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            /*if (main.isDebug()) {
                if (canDebug()) {
                    main.getDebugListener().onLine(this, getLineNumber());
                    main.scriptControlCheck(getLineNumber(), true);
                } else {
                    main.scriptControlCheck(getLineNumber(), false);
                }
            } else {
                main.scriptControlCheck(getLineNumber());
            }*/
            main.scriptControlCheck(getLineNumber(), false); //disable debug
            main.incStack(getLineNumber());

            Object valueImpl = getValueImpl(f, main);

            main.decStack();
            return valueImpl;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        }
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
    }

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
