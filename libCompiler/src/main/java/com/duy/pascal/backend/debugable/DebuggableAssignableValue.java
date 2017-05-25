package com.duy.pascal.backend.debugable;

import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.AssignableValue;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.references.Reference;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.exception.UnhandledPascalException;

public abstract class DebuggableAssignableValue implements AssignableValue {
    protected RuntimeValue[] outputFormat;

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
            if (main != null) {
                main.scriptControlCheck(getLineNumber());
            }
            return getValueImpl(f, main);
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        }
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return this;
    }

    public Reference<?> getReference(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (main != null) {
                main.scriptControlCheck(getLineNumber());
            }
            return getReferenceImpl(f, main);
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        }
    }

    public abstract Object getValueImpl(VariableContext f,
                                        RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException;

    public abstract Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
