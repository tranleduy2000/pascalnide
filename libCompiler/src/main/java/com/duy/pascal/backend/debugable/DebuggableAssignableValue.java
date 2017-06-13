package com.duy.pascal.backend.debugable;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.UnhandledPascalException;

public abstract class DebuggableAssignableValue implements AssignableValue {
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

    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (canDebug() && main.isDebug()) {
                main.getDebugListener().onLine(this, getLineNumber());
                if (canDebug()) {
                    main.scriptControlCheck(getLineNumber(), true);
                } else {
                    main.scriptControlCheck(getLineNumber(), false);
                }
            } else {
                main.scriptControlCheck(getLineNumber(), false);
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

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    public Reference<?> getReference(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
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
