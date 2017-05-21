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
import com.duy.pascal.backend.exceptions.io.InputStreamNotFoundException;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.runtime_exceptions.CanNotReadVariableException;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.runtime.references.PascalReference;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class IOLib implements PascalLibrary {

    public static final String TAG = IOLib.class.getSimpleName();

    private PrintStream stdout;
    private Scanner stdin;
    private InOutListener listener;
    private RuntimeExecutableCodeUnit.ControlMode state = RuntimeExecutableCodeUnit.ControlMode.PAUSED;

    /**
     * default constructor
     */
    public IOLib() {
        if (!PascalCompiler.android) {
            this.stdout = System.out;
            this.stdin = new Scanner(System.in);
        }
    }


    /**
     * constructor call by {@link ClassLoader} in {@link PascalCompiler}
     */
    public IOLib(InOutListener listener) {
        this.listener = listener;
        if (!PascalCompiler.android) {
            this.stdout = System.out;
            this.stdin = new Scanner(System.in);
        }
    }

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

    @Override
    public void shutdown() {

    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    private void println(Object... args) {
        print(args);
        print("\n");
    }

    private void print(Object... args) {
        if (listener == null) return;
        StringBuilder result = new StringBuilder();
        for (Object o : args) {
            result.append(o.toString());
        }
        listener.print(result.toString());
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln() {
        println();
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object values) {
        println(values);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2) {
        println(o1, o2);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3) {
        println(o1, o2, o3);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4) {
        println(o1, o2, o3, o4);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5) {
        println(o1, o2, o3, o4, o5);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        println(o1, o2, o3, o4, o5, o6);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7) {
        println(o1, o2, o3, o4, o5, o6, o7);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8) {
        println(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8, Object o9, Object o10) {
        println(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write() {

    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object values) {
        print(values);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2) {
        print(o1, o2);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3) {
        print(o1, o2, o3);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4) {
        print(o1, o2, o3, o4);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5) {
        print(o1, o2, o3, o4, o5);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        print(o1, o2, o3, o4, o5, o6);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7) {
        print(o1, o2, o3, o4, o5, o6, o7);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8) {
        print(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8, Object o9) {
        print(o1, o2, o3, o4, o5, o6, o7, o8, o9);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8, Object o9, Object o10) {
        print(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read() {
        if (listener != null) {
            listener.startInput(this);
            pause(); //wait for press enter
        }
    }

    /**
     * read procedure
     */
    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1) throws RuntimePascalException, NumberFormatException {
        setValueForVariables(a1);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference<Object> a2, PascalReference<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference<Object> a2,
                     PascalReference<Object> a3, PascalReference<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference<Object> a2,
                     PascalReference<Object> a3, PascalReference<Object> a4,
                     PascalReference<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference<Object> a2,
                     PascalReference<Object> a3, PascalReference<Object> a4,
                     PascalReference<Object> a5, PascalReference<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);

    }

    @PascalMethod(description = "system library", returns = "void")
    public void read(PascalReference<Object> a1, PascalReference<Object> a2,
                     PascalReference<Object> a3, PascalReference<Object> a4,
                     PascalReference<Object> a5, PascalReference<Object> a6,
                     PascalReference<Object> a7) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6, a7);
    }

    private void readString(Scanner scanner, PascalReference<String> variableBoxer) {
        variableBoxer.set(scanner.nextLine());

    }

    private void readStringBuilder(Scanner scanner, PascalReference<StringBuilder> variableBoxer) {
        variableBoxer.set(new StringBuilder(scanner.nextLine()));
    }

    private void readInt(Scanner scanner, PascalReference<Object> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextInt());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readLong(Scanner scanner, PascalReference<Long> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextLong());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readDouble(Scanner scanner, PascalReference<Double> variableBoxer) throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextDouble());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read variable");
        }
    }

    private void readChar(Scanner scanner, PascalReference<Character> variableBoxer) {
        System.out.println("readchar " + scanner.hasNext());
        variableBoxer.set(scanner.next().charAt(0));
    }

    @SuppressWarnings("unchecked")
    private void setValueForVariables(PascalReference... listVariable) throws RuntimePascalException {
        if (listener == null)
            throw new InputStreamNotFoundException();
        Scanner scanner = new Scanner("");
        for (PascalReference variableBoxer : listVariable) {
            Log.d(TAG, "setValueForVariables: ");
            while (!scanner.hasNext()) {

                if (scanner.hasNextLine() &&
                        (variableBoxer.get() instanceof Character
                                || variableBoxer.get() instanceof StringBuilder)) {
                    break;
                }

                listener.startInput(this);
                pause();
                String input = listener.getInput();
                scanner = new Scanner(input);
                scanner.useLocale(Locale.ENGLISH);
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

    @PascalMethod(description = "system library", returns = "void")
    public void readln() {
        if (listener != null) {
            listener.startInput(this);
            pause();
        }
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> variableBoxer) throws NumberFormatException, RuntimePascalException {
        setValueForVariables(variableBoxer);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference<Object> a2, PascalReference<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference<Object> a2,
                       PascalReference<Object> a3, PascalReference<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference<Object> a2,
                       PascalReference<Object> a3, PascalReference<Object> a4,
                       PascalReference<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference<Object> a2,
                       PascalReference<Object> a3, PascalReference<Object> a4,
                       PascalReference<Object> a5, PascalReference<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void readln(PascalReference<Object> a1, PascalReference<Object> a2,
                       PascalReference<Object> a3, PascalReference<Object> a4,
                       PascalReference<Object> a5, PascalReference<Object> a6,
                       PascalReference<Object> a7) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6, a7);
    }

    @PascalMethod(description = "system library", returns = "void")
    public void printf(String format, Object... args) {
        if (stdout != null) stdout.printf(format, args);
    }

    /**
     * resume when input user press enter key
     */
    public void resume() {
        this.state = RuntimeExecutableCodeUnit.ControlMode.PAUSED;
        synchronized (this) {
            notify();
        }
    }

    /**
     * pause this object, wait for user input data
     */
    public void pause() {
        this.state = RuntimeExecutableCodeUnit.ControlMode.RUNNING;
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public RuntimeExecutableCodeUnit.ControlMode getState() {
        return state;
    }

    public boolean isRunning() {
        return state == RuntimeExecutableCodeUnit.ControlMode.RUNNING;
    }

    @PascalMethod(description = "system library", returns = "void")
    public char readKey() {
        Log.d(TAG, "readKey: ");
        if (listener != null) {
            return listener.getKeyBuffer();
        }
        return (char) 0;
    }

    @PascalMethod(description = "system library", returns = "void")
    public boolean keyPressed() {
        return listener != null && listener.keyPressed();
    }

}
