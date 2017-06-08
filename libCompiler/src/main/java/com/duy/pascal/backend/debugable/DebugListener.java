package com.duy.pascal.backend.debugable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.duy.pascal.backend.ast.AbstractCallableFunction;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.debug.CallStack;

/**
 * Created by Duy on 24-Mar-17.
 */
public interface DebugListener {
    void onLine(Executable executable, LineInfo lineInfo);

    void onLine(RuntimeValue executable, LineInfo lineInfo);

    void onNewMessage(String msg);

    void onClearDebug();

    void onFunctionCall(String name);

    void onEvaluatingExpr(LineInfo lineInfo, String expression);

    void onEvaluatedExpr(LineInfo lineInfo, String expr, String result);

    void onAssignValue(LineInfo lineNumber, AssignableValue left, @NonNull Object old,
                       @Nullable Object value, @NonNull VariableContext context);

    void onPreFunctionCall(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments);

    void onFunctionCalled(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments, @Nullable Object result);

    void onEvalParameterFunction(LineInfo lineInfo, @Nullable String name, @Nullable Object value);

    void onEndProgram();

    void showMessage(LineInfo pos, String msg);

    void onVariableChange(CallStack currentFrame);

    void onVariableChange(CallStack currentFrame, Pair<String, Object> value);
}
