package com.duy.pascal.interperter.debugable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.function.AbstractCallableFunction;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.debug.CallStack;

/**
 * Created by Duy on 24-Mar-17.
 */
public interface IDebugListener {
    @WorkerThread
    void onLine(Node node, LineNumber lineNumber);

    @WorkerThread
    void onLine(RuntimeValue executable, LineNumber lineNumber);

    @WorkerThread
    void onNewMessage(String msg);

    @WorkerThread
    void onEvaluatingExpr(LineNumber lineNumber, String expression);

    @WorkerThread
    void onEvaluatedExpr(LineNumber lineNumber, String expr, String result);

    @WorkerThread
    void onAssignValue(LineNumber lineNumber, AssignableValue left, @NonNull Object oldValue,
                       @Nullable Object newValue, @NonNull VariableContext context);

    @WorkerThread
    void onPreFunctionCall(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments);

    @WorkerThread
    void onFunctionCalled(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments, @Nullable Object result);

    @WorkerThread
    void onEvalParameterFunction(LineNumber lineNumber, @Nullable String name, @Nullable Object value);

    @WorkerThread
    void onFinish();

    @WorkerThread
    void showMessage(LineNumber pos, String msg);

    @WorkerThread
    void onValueVariableChanged(CallStack currentFrame);

}
