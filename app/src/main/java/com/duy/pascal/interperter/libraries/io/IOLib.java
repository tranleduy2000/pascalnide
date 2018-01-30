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

package com.duy.pascal.interperter.libraries.io;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.exceptions.parsing.io.InputStreamNotFoundException;
import com.duy.pascal.interperter.exceptions.runtime.InvalidNumericFormatException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.exceptions.CanNotReadVariableException;
import com.duy.pascal.ui.utils.DLog;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

@SuppressWarnings("unused")
public class IOLib extends PascalLibrary {

    public static final String TAG = IOLib.class.getSimpleName();

    private PrintStream stdout;
    private Scanner stdin;
    private InOutListener listener;
    private RuntimeExecutableCodeUnit.ControlMode state = RuntimeExecutableCodeUnit.ControlMode.PAUSED;
    @NonNull
    private String inputBuffer = "";

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
    public void onFinalize() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }

    public void println(Object... args) {
        print(args);
        print("\n");
    }

    public void print(Object... args) {
        if (listener == null) return;
        StringBuilder result = new StringBuilder();
        for (Object o : args) {
            result.append(o.toString());
        }
        listener.print(result.toString());
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            throw new InvalidNumericFormatException(e.getMessage());
        }
    }

    private void readLong(Scanner scanner, PascalReference<Long> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextLong());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException(/*"read variable"*/e.getMessage());
        }
    }

    private void readDouble(Scanner scanner, PascalReference<Double> variableBoxer) throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextDouble());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException(/*"read variable"*/e.getMessage());
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
            DLog.d(TAG, "setValueForVariables: ");
            while (!scanner.hasNext()) {

                if (scanner.hasNextLine() &&
                        (variableBoxer.get() instanceof Character
                                || variableBoxer.get() instanceof StringBuilder)) {
                    break;
                }

                listener.startInput(this);
                pause();
                scanner = new Scanner(getInputBuffer());
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

    @NonNull
    private String getInputBuffer() {
        return inputBuffer;
    }

    public void setInputBuffer(@NonNull String inputBuffer) {
        this.inputBuffer = inputBuffer;
        synchronized (this) {
            this.notifyAll();
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
        DLog.d(TAG, "readKey: ");
        if (listener != null) {
            return listener.getKeyBuffer();
        }
        return (char) 0;
    }

    @PascalMethod(description = "system library", returns = "void")
    public boolean keyPressed() {
        return listener != null && listener.keyPressed();
    }

    /**
     * resume when input user press enter key
     */
    public synchronized void resume() {
        this.state = RuntimeExecutableCodeUnit.ControlMode.PAUSED;
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * pause this object, wait for user input data
     */
    public synchronized void pause() {
        this.state = RuntimeExecutableCodeUnit.ControlMode.RUNNING;
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void readz(PascalReference[] values) throws RuntimePascalException {
        setValueForVariables(values);
    }

    public void readlnz(PascalReference[] values) throws RuntimePascalException {
        if (values.length == 0) {
            if (listener != null) {
                listener.startInput(this);
                pause(); //wait for press enter
            }
        } else {
            setValueForVariables(values);
        }
    }
}
