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

package com.duy.pascal;

import android.content.Context;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.codeunit.program.PascalProgram;
import com.js.interpreter.source_include.ScriptSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Duy on 29-May-17.
 */

public class Compiler {
    private static Scanner input;

    public static boolean runProgram(String programPath, @Nullable String pathIn)
            throws RuntimePascalException, ParsingException, java.io.FileNotFoundException {
        DLog.d("Program path = " + programPath);
        File programFile = new File(programPath);
        if (pathIn != null) {
            input = new Scanner(new FileReader(programFile));
        }
        final StringBuilder output = new StringBuilder();
        PascalProgram pascalProgram = PascalCompiler.loadPascal(
                new File(programPath).getName(),
                new FileReader(programPath),
                new ArrayList<ScriptSource>(),
                new IRunnablePascal() {
                    @Override
                    public Context getApplicationContext() {
                        return null;
                    }

                    @Override
                    public void onGlobalVariableChangeValue(VariableDeclaration variableDeclaration) {

                    }

                    @Override
                    public void onLocalVariableChangeValue(VariableDeclaration variableDeclaration) {

                    }

                    @Override
                    public void onFunctionCall(FunctionDeclaration functionDeclaration) {

                    }

                    @Override
                    public void onProcedureCall(FunctionDeclaration functionDeclaration) {

                    }

                    @Override
                    public void onNewMessage(String msg) {

                    }

                    @Override
                    public void onClearDebug() {

                    }

                    @Override
                    public void onVariableChangeValue(String name, Object old, Object newValue) {

                    }

                    @Override
                    public void onFunctionCall(String name) {

                    }

                    @Override
                    public void startInput(IOLib lock) {
                        if (input == null) {
                            throw new RuntimeException("can not find input reader");
                        }
                        String s = input.nextLine();
                        lock.setInputBuffer(s);
                    }

                    @Override
                    public void onLine(LineInfo lineInfo) {

                    }

                    @Override
                    public void print(CharSequence charSequence) {
                        System.out.println("print " + charSequence.toString());
                        output.append(charSequence);
                    }

                    @Override
                    public ConsoleView getConsoleView() {
                        return null;
                    }

                    @Override
                    public void println(CharSequence charSequence) {
                        output.append(charSequence);
                        output.append("\n");
                    }

                    @Override
                    public char getKeyBuffer() {
                        return 0;
                    }

                    @Override
                    public boolean keyPressed() {
                        return false;
                    }
                });

        RuntimeExecutableCodeUnit<PascalProgram> program = pascalProgram.run();
        program.run();

        String pathOut = programFile.getParent() + File.separatorChar
                + programFile.getName().substring(0, programFile.getName().indexOf("."))
                + ".out";
        DLog.d("path out " + pathOut);
        File fileOut = new File(pathOut);

        if (fileOut.exists()) {
            try {
                String expectOutput = IOUtils.streamToString(new FileInputStream(fileOut)).toString();
                if (output.toString().equals(expectOutput)) {
                    DLog.d("------------ RESULT --------------");
                    DLog.d("------------   OK --------------");
                    return true;
                } else {
                    DLog.d("------------ RESULT --------------");
                    DLog.d("------------ FAILED --------------");
                    throw new RuntimeException("wrong output: \n" +
                            "current: " + output.toString() + "\n" +
                            "expect: " + expectOutput);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
