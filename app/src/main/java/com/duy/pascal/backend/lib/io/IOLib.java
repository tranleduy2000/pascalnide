/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.lib.io;

import android.util.Log;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.exceptions.InputStreamNotFoundException;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.runtime_exceptions.CanNotReadVariableException;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class IOLib implements PascalLibrary {

    public static final String TAG = IOLib.class.getSimpleName();

    private PrintStream stdout;
    private Scanner stdin;
    private ExecuteActivity activity;

    /**
     * constructor call by {@link ClassLoader} in {@link PascalCompiler}
     */
    public IOLib(ExecuteActivity activity) {
        this.activity = activity;
        if (!PascalCompiler.android) {
            this.stdout = System.out;
            this.stdin = new Scanner(System.in);
        }
    }

//    /**
//     * default constructor
//     */
//     @SuppressWarnings("unused")public IOLib() {
//        if (!PascalCompiler.android) {
//            this.stdout = System.out;
//            this.stdin = new Scanner(System.in);
//        }
//    }


    @Override
    public boolean instantiate(Map<String, Object> arguments) {
        if (!PascalCompiler.android) {
            this.stdout = (PrintStream) arguments.get("stdout");
            this.stdin = (Scanner) arguments.get("stdin");
            if (stdout == null) {
                stdout = System.out;
            }
            if (stdin == null) {
                stdin = new Scanner(System.in);
            }
        }
        return true;
    }

    private void println(Object... args) {
        print(args);
        print("\n");
    }

    private void print(Object... args) {
        if (activity == null) return;
        StringBuilder result = new StringBuilder();
        for (Object o : args) {
            result.append(o.toString());
        }
        activity.getConsoleView().commitString(result.toString());
    }

    @SuppressWarnings("unused")
    public void writeln() {
        println();
    }

    @SuppressWarnings("unused")
    public void writeln(Object values) {
        println(values);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2) {
        println(o1, o2);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3) {
        println(o1, o2, o3);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4) {
        println(o1, o2, o3, o4);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5) {
        println(o1, o2, o3, o4, o5);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        println(o1, o2, o3, o4, o5, o6);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7) {
        println(o1, o2, o3, o4, o5, o6, o7);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8) {
        println(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    @SuppressWarnings("unused")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8, Object o9, Object o10) {
        println(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }


    @SuppressWarnings("unused")
    public void write() {

    }

    @SuppressWarnings("unused")
    public void write(Object values) {
        print(values);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2) {
        print(o1, o2);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3) {
        print(o1, o2, o3);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4) {
        print(o1, o2, o3, o4);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5) {
        print(o1, o2, o3, o4, o5);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        print(o1, o2, o3, o4, o5, o6);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7) {
        print(o1, o2, o3, o4, o5, o6, o7);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8) {
        print(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8, Object o9) {
        print(o1, o2, o3, o4, o5, o6, o7, o8, o9);
    }

    @SuppressWarnings("unused")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8, Object o9, Object o10) {
        print(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }


    private void sleep() {
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }


    @SuppressWarnings("unused")
    public void read() {
        if (activity != null) {
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
        }
    }

    /**
     * read procedure
     */
    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1) throws RuntimePascalException, NumberFormatException {
        setValueForVariables(a1);
    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2, VariableBoxer<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                     VariableBoxer<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                     VariableBoxer<Object> a5, VariableBoxer<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);

    }

    @SuppressWarnings("unused")
    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                     VariableBoxer<Object> a5, VariableBoxer<Object> a6,
                     VariableBoxer<Object> a7) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6, a7);
    }


    private void readString(Scanner scanner, VariableBoxer<String> variableBoxer) {
        variableBoxer.set(scanner.nextLine());

    }

    private void readStringBuilder(Scanner scanner, VariableBoxer<StringBuilder> variableBoxer) {
        variableBoxer.set(new StringBuilder(scanner.nextLine()));
    }

    private void readInt(Scanner scanner, VariableBoxer<Object> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextInt());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readLong(Scanner scanner, VariableBoxer<Long> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextLong());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readDouble(Scanner scanner, VariableBoxer<Double> variableBoxer) throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextDouble());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readChar(Scanner scanner, VariableBoxer<Character> variableBoxer) {
        System.out.println("readchar " + scanner.hasNext());
        variableBoxer.set(scanner.next().charAt(0));
    }

    private void setValueForVariables(VariableBoxer... listVariable) throws RuntimePascalException {
        if (activity == null)
            throw new InputStreamNotFoundException();
        Scanner scanner = new Scanner("");
        for (VariableBoxer variableBoxer : listVariable) {
            Object o = variableBoxer.get();
            Log.d(TAG, "setValueForVariables: ");
            while (!scanner.hasNext()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                String input = activity.getInput();
                scanner = new Scanner(input);
            }
            if (variableBoxer.get() instanceof Character) {
                readChar(scanner, variableBoxer);
            } else if (variableBoxer.get() instanceof StringBuilder) {
                readStringBuilder(scanner, variableBoxer);
            } else if (variableBoxer.get() instanceof String) {
                readString(scanner, variableBoxer);
            } else if (variableBoxer.get() instanceof Integer) {
                readInt(scanner, variableBoxer);
            } else if (variableBoxer.get() instanceof Long) {
                readLong(scanner, variableBoxer);
            } else if (variableBoxer.get() instanceof Double) {
                readDouble(scanner, variableBoxer);
            } else {
                throw new CanNotReadVariableException(variableBoxer.get());
            }
        }
    }


    @SuppressWarnings("unused")
    public void readln() {
        if (activity != null) {
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
        }
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> variableBoxer) throws NumberFormatException, RuntimePascalException {
        setValueForVariables(variableBoxer);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2, VariableBoxer<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5, VariableBoxer<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);
    }

    @SuppressWarnings("unused")
    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5, VariableBoxer<Object> a6,
                       VariableBoxer<Object> a7) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6, a7);
    }


    @SuppressWarnings("unused")
    public void printf(String format, Object... args) {
        if (stdout != null) stdout.printf(format, args);
    }

}
