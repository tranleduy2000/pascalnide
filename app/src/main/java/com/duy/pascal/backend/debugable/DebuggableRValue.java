package com.duy.pascal.backend.debugable;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.ast.returnsvalue.boxing.ArrayBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.StringBuilderBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

public abstract class DebuggableRValue implements RValue {

    protected RValue[] outputFormat;

    @Override
    public RValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        try {
            if (main != null) {
                if (main.isDebugMode()) {
                    if (!(this instanceof ConstantAccess
                            || this instanceof StringBuilderBoxer
                            || this instanceof ArrayBoxer
                            || this instanceof CharacterBoxer)) {
                        System.out.println(getClass().getSimpleName() + " " + getLineNumber());
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
    public LValue asLValue(ExpressionContext f) {
        return null;
    }

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

}
