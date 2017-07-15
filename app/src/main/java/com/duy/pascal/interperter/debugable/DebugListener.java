package com.duy.pascal.interperter.debugable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.Pair;

import com.duy.pascal.interperter.declaration.lang.function.AbstractCallableFunction;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.frontend.debug.CallStack;

/**
 * Created by Duy on 24-Mar-17.
 */
public interface DebugListener {
    @WorkerThread
    void onLine(Executable executable, LineInfo lineInfo);

    @WorkerThread
    void onLine(RuntimeValue executable, LineInfo lineInfo);

    @WorkerThread
    void onNewMessage(String msg);

    @WorkerThread
    void onClearDebug();

    @WorkerThread
    void onFunctionCall(String name);

    @WorkerThread
    void onEvaluatingExpr(LineInfo lineInfo, String expression);

    @WorkerThread
    void onEvaluatedExpr(LineInfo lineInfo, String expr, String result);

    @WorkerThread
    void onAssignValue(LineInfo lineNumber, AssignableValue left, @NonNull Object old,
                       @Nullable Object value, @NonNull VariableContext context);

    @WorkerThread
    void onPreFunctionCall(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments);

    @WorkerThread
    void onFunctionCalled(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments, @Nullable Object result);

    @WorkerThread
    void onEvalParameterFunction(LineInfo lineInfo, @Nullable String name, @Nullable Object value);

    @WorkerThread
    void onEndProgram();

    @WorkerThread
    void showMessage(LineInfo pos, String msg);

    @WorkerThread
    void onVariableChange(CallStack currentFrame);

    @WorkerThread
    void onVariableChange(CallStack currentFrame, Pair<String, Object> value);
}
