package com.duy.pascal.backend.debugable;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.AbstractCallableFunction;
import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 24-Mar-17.
 */
public interface DebugListener {
    void onLine(Executable executable, LineInfo lineInfo);

    void onLine(RuntimeValue executable, LineInfo lineInfo);

    void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration);

    void onLocalVariableChangeValue(VariableDeclaration variableDeclaration);

    void onFunctionCall(FunctionDeclaration functionDeclaration);

    void onProcedureCall(FunctionDeclaration functionDeclaration);

    void onNewMessage(String msg);

    void onClearDebug();

    void onVariableChangeValue(String name, Object old, Object newValue);

    void onFunctionCall(String name);

    void onEvaluatingExpr(LineInfo lineInfo, String expression);

    void onEvaluatedExpr(LineInfo lineInfo, String expr, String result);

    void onAssignValue(LineInfo lineNumber, AssignableValue left,@Nullable RuntimeValue value);

    void onPreFunctionCall(AbstractCallableFunction function, @Nullable RuntimeValue[] arguments);

    void onFunctionCalled(AbstractCallableFunction function,@Nullable RuntimeValue[] arguments,@Nullable Object result);

    void onEvalParameterFunction(LineInfo lineInfo,@Nullable String name,@Nullable Object value);

    void onEndProgram();
}
