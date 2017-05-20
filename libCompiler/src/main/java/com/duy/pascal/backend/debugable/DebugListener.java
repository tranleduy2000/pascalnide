package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.js.interpreter.VariableDeclaration;

/**
 * Created by Duy on 24-Mar-17.
 */
public interface DebugListener {
    void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration);

    void onLocalVariableChangeValue(VariableDeclaration variableDeclaration);

    void onFunctionCall(FunctionDeclaration functionDeclaration);

    void onProcedureCall(FunctionDeclaration functionDeclaration);

    void onNewMessage(String msg);

    void onClearDebug();

    void onVariableChangeValue(String name, Object old, Object newValue);

    void onFunctionCall(String name);

    void onLine(LineInfo lineInfo);
}
