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

import android.app.Activity;
import android.content.Context;

import com.duy.pascal.ui.DLog;
import com.duy.pascal.ui.runnable.ProgramHandler;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleView;
import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.libraries.io.IOLib;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.Diagnostic;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.source.FileScriptSource;
import com.duy.pascal.interperter.source.ScriptSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Duy on 29-May-17.
 */

public class Interpreter {
    private static Scanner input;

    public static boolean runProgram(String programPath) throws Exception {
        DLog.d("Program path = " + programPath);
        final File programFile = new File(programPath);
        String pathIn = programFile.getParent() + File.separatorChar
                + programFile.getName().substring(0, programFile.getName().indexOf("."))
                + ".in";

        File fileIn = new File(pathIn);
        if (fileIn.exists()) {
            DLog.d("path in " + pathIn);
            input = new Scanner(new FileReader(fileIn));
        }
        final StringBuilder output = new StringBuilder();
        ArrayList<ScriptSource> searchPath = new ArrayList<>();
        searchPath.add(new FileScriptSource(new File(programPath).getParent()));

        DiagnosticCollector diagnosticCollector = new DiagnosticCollector();

        PascalProgramDeclaration pascalProgram = PascalCompiler.loadPascal(
                new File(programPath).getName(), new FileReader(programPath), searchPath,
                new ProgramHandler() {
                    @Override
                    public String getCurrentDirectory() {
                        return programFile.getParent();
                    }

                    @Override
                    public Context getApplicationContext() {
                        return null;
                    }

                    @Override
                    public Activity getActivity() {
                        return null;
                    }

                    @Override
                    public void startInput(final IOLib lock) {
                        if (input == null) {
                            throw new RuntimeException("can not find input reader");
                        }
                        String s = input.nextLine();
                        lock.setInputBuffer(s);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                lock.resume();
                            }
                        }).start();
                    }


                    @Override
                    public void print(CharSequence charSequence) {
                        output.append(charSequence);
                        System.out.print(charSequence);
                    }

                    @Override
                    public ConsoleView getConsoleView() {
                        return null;
                    }

                    @Override
                    public void println(CharSequence charSequence) {
                        output.append(charSequence);
                        output.append("\n");
                        System.out.println(charSequence);
                    }

                    @Override
                    public char getKeyBuffer() {
                        return 0;
                    }

                    @Override
                    public boolean keyPressed() {
                        return true;
                    }

                    @Override
                    public void clearConsole() {

                    }
                }, diagnosticCollector);
        if (diagnosticCollector.getDiagnostics().isEmpty()) {

        } else {
            for (Diagnostic diagnostic : diagnosticCollector.getDiagnostics()) {
                if (diagnostic.getCause() instanceof ParsingException) {
                    ParsingException cause = (ParsingException) diagnostic.getCause();
                    System.err.println(cause.getLineInfo() + " " + cause.getMessage());
                }
            }
            throw diagnosticCollector.getDiagnostics().get(0).getCause();
        }

        RuntimeExecutableCodeUnit<PascalProgramDeclaration> program = pascalProgram.generate();
        program.run();

        String pathOut = programFile.getParent() + File.separatorChar
                + programFile.getName().substring(0, programFile.getName().indexOf("."))
                + ".out";

        File fileOut = new File(pathOut);

        if (fileOut.exists()) {
            DLog.d("path out " + pathOut);
            try {
                String expectOutput = IOUtils.streamToString(new FileInputStream(fileOut)).toString();
                if (output.toString().replaceAll("\\s", "").equals(expectOutput.replaceAll("\\s", ""))) {
                    DLog.d("------------ RESULT --------------");
                    DLog.d("output\n" + output.toString());
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

    public static boolean checkSyntax(String programPath) {
        System.out.println(programPath);
        final StringBuilder output = new StringBuilder();
        final File programFile = new File(programPath);

        ArrayList<ScriptSource> searchPath = new ArrayList<>();
        searchPath.add(new FileScriptSource(new File(programPath).getParent()));
        try {
            PascalProgramDeclaration pascalProgram = PascalCompiler.loadPascal(
                    new File(programPath).getName(), new FileReader(programPath), searchPath,
                    new ProgramHandler() {
                        @Override
                        public String getCurrentDirectory() {
                            return programFile.getParent();
                        }

                        @Override
                        public Context getApplicationContext() {
                            return null;
                        }

                        @Override
                        public Activity getActivity() {
                            return null;
                        }

                        @Override
                        public void startInput(final IOLib lock) {
                            if (input == null) {
                                throw new RuntimeException("can not find input reader");
                            }
                            String s = input.nextLine();
                            lock.setInputBuffer(s);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    lock.resume();
                                }
                            }).start();
                        }


                        @Override
                        public void print(CharSequence charSequence) {
                            output.append(charSequence);
                            System.out.print(charSequence);
                        }

                        @Override
                        public ConsoleView getConsoleView() {
                            return null;
                        }

                        @Override
                        public void println(CharSequence charSequence) {
                            output.append(charSequence);
                            output.append("\n");
                            System.out.println(charSequence);
                        }

                        @Override
                        public char getKeyBuffer() {
                            return 0;
                        }

                        @Override
                        public boolean keyPressed() {
                            return false;
                        }

                        @Override
                        public void clearConsole() {

                        }
                    });
        } catch (ParsingException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
