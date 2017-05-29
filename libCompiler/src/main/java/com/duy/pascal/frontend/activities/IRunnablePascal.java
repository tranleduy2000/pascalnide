/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.activities;

import android.content.Context;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.lib.io.InOutListener;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;
import com.js.interpreter.VariableDeclaration;

/**
 * Created by Duy on 29-May-17.
 */

public interface IRunnablePascal extends ExecHandler, InOutListener, DebugListener, ActivityHandler {
    @Override
    String getCurrentDirectory();

    @Override
    Context getApplicationContext();

    @Override
    void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration);

    @Override
    void onLocalVariableChangeValue(VariableDeclaration variableDeclaration);

    @Override
    void onFunctionCall(FunctionDeclaration functionDeclaration);

    @Override
    void onProcedureCall(FunctionDeclaration functionDeclaration);

    @Override
    void onNewMessage(String msg);

    @Override
    void onClearDebug();

    @Override
    void onVariableChangeValue(String name, Object old, Object newValue);

    @Override
    void onFunctionCall(String name);

    @Override
    void startInput(IOLib lock);

    @Override
    void onLine(LineInfo lineInfo);


    @Override
    void print(CharSequence charSequence);

    @Override
    @Nullable
    ConsoleView getConsoleView();

    @Override
    void println(CharSequence charSequence);

    @Override
    char getKeyBuffer();

    @Override
    boolean keyPressed();
}
