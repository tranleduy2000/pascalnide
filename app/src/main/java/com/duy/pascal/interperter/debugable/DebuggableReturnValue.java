package com.duy.pascal.interperter.debugable;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.UnhandledPascalException;
import com.duy.pascal.interperter.utils.NullSafety;

public abstract class DebuggableReturnValue implements RuntimeValue {
    private LineInfo lineNumber;
    public abstract boolean canDebug();

    @NonNull
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            return NullSafety.zReturn(getValueImpl(context, main));
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        } catch (Throwable e) {
            throw new UnhandledPascalException(getLineNumber(), e);
        }
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
        this.lineNumber = lineNumber;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineNumber;
    }

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
