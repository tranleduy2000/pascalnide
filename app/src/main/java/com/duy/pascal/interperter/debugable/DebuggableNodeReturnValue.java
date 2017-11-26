package com.duy.pascal.interperter.debugable;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.config.DebugMode;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.UnhandledPascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.utils.NullSafety;

public abstract class DebuggableNodeReturnValue implements Node, RuntimeValue {

    private LineInfo lineNumber;

    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            return NullSafety.zReturn(getValueImpl(f, main));
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(this.getLineNumber(), e);
        } catch (Throwable throwable) {
            throw new UnhandledPascalException(getLineNumber(), throwable);
        }
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    @Override
    public LineInfo getLineNumber() {
        return lineNumber;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

        this.lineNumber = lineNumber;
    }

    public abstract Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    @Override
    public ExecutionResult visit(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {

            boolean last = main.isDebug();
            onPreExecute(main);

            ExecutionResult result = executeImpl(context, main);

            onPostExecute(main, last);
            return result;
        } catch (RuntimePascalException e) {
            throw e;
        } catch (Exception e) {
            throw new UnhandledPascalException(getLineNumber(), e);
        } catch (Throwable e) {
            throw new UnhandledPascalException(getLineNumber(), e);
        }
    }

    private void onPostExecute(RuntimeExecutableCodeUnit<?> main, boolean last) {
        main.setDebug(last);
        main.decStack();
    }

    private void onPreExecute(RuntimeExecutableCodeUnit<?> main) {
        if (main.isDebug()) {
            main.getDebugListener().onLine((Node) this, getLineNumber());
        }
        main.scriptControlCheck(getLineNumber());

        main.incStack(getLineNumber());
        if (main.isDebug()) {
            if (main.getDebugMode().equals(DebugMode.STEP_OVER)) {
                main.setDebug(false);
            }
        }
    }

    public abstract ExecutionResult executeImpl(VariableContext f,
                                                RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;
}
