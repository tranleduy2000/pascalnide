package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.ArrayBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.CharacterBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.StringBuilderBoxer;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.UnhandledPascalException;

public abstract class DebuggableReturnValue implements RuntimeValue {

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
                if (main.isDebugMode()) {
                    if (!(this instanceof ConstantAccess
                            || this instanceof StringBuilderBoxer
                            || this instanceof ArrayBoxer
                            || this instanceof CharacterBoxer)) {
                        main.getDebugListener().onLine(getLineNumber());
                        main.scriptControlCheck(getLineNumber(), true);
                    } else {
                        main.scriptControlCheck(getLineNumber(), false);
                    }
                } else {
                    main.scriptControlCheck(getLineNumber());
                }
                main.incStack(getLineNumber());
            }
            Object valueImpl = getValueImpl(f, main);
            if (main != null) {
                main.decStack();
            }
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

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

}
