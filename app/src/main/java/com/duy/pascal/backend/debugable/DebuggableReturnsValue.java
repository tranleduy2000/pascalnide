package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.UnhandledPascalException;

public abstract class DebuggableReturnsValue implements ReturnsValue {

    private void checkStack(VariableContext f) throws StackOverflowException {
        if (f instanceof FunctionOnStack) {
            StackFunction.inc(((FunctionOnStack) f).getCurrentFunction().getline());
        } else {
            StackFunction.inc(null);
        }
    }
    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        try {
            if (main != null) {
                main.scriptControlCheck(getline());
            }
            return getValueImpl(f, main);
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getline(), e);
        }
    }
   /* @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        checkStack(f);
        try {
            if (main != null) {
                main.scriptControlCheck(getline());
            }

            Object result = getValueImpl(f, main);

            StackFunction.dec();
            return result;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getline(), e);
        }
    }*/

    public abstract Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

}
