package com.duy.pascal.backend.debugable;

import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.ConstantAccess;
import com.js.interpreter.runtime_value.AssignableValue;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.boxing.ArrayBoxer;
import com.js.interpreter.runtime_value.boxing.CharacterBoxer;
import com.js.interpreter.runtime_value.boxing.StringBuilderBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

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
