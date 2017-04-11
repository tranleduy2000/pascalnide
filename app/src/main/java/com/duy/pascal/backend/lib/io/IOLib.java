package com.duy.pascal.backend.lib.io;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.exceptions.InputStreamNotFoundException;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.WrongTypeInputException;

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
//    public IOLib() {
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
            result.append(o);
        }
        activity.getConsoleView().commitString(result.toString());
    }

    public void writeln() {
        println();
    }

    public void writeln(Object values) {
        println(values);
    }

    public void writeln(Object o1, Object o2) {
        println(o1, o2);
    }

    public void writeln(Object o1, Object o2, Object o3) {
        println(o1, o2, o3);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4) {
        println(o1, o2, o3, o4);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5) {
        println(o1, o2, o3, o4, o5);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        println(o1, o2, o3, o4, o5, o6);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7) {
        println(o1, o2, o3, o4, o5, o6, o7);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8) {
        println(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    public void writeln(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                        Object o7, Object o8, Object o9, Object o10) {
        println(o1, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }


    public void write() {

    }

    public void write(Object values) {
        println(values);
    }

    public void write(Object o1, Object o2) {
        println(o1, o2);
    }

    public void write(Object o1, Object o2, Object o3) {
        println(o1, o2, o3);
    }

    public void write(Object o1, Object o2, Object o3, Object o4) {
        println(o1, o2, o3, o4);
    }

    public void write(Object o1, Object o2, Object o3, Object o4, Object o5) {
        println(o1, o2, o3, o4, o5);
    }

    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        println(o1, o2, o3, o4, o5, o6);
    }

    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7) {
        println(o1, o2, o3, o4, o5, o6, o7);
    }

    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8) {
        println(o1, o2, o3, o4, o5, o6, o7, o8);
    }

    public void write(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6,
                      Object o7, Object o8, Object o9) {
        print(o1, o2, o3, o4, o5, o6, o7, o8, o9);
    }

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
    public void read(VariableBoxer<Object> a1) throws RuntimePascalException, NumberFormatException {
        setValueForVariables(a1);
    }

    public void read(VariableBoxer<Object> a1, VariableBoxer a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2, VariableBoxer<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                     VariableBoxer<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    public void read(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                     VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                     VariableBoxer<Object> a5, VariableBoxer<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);

    }

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
            throw new InvalidNumericFormatException();
        }
    }

    private void readLong(Scanner scanner, VariableBoxer<Long> variableBoxer)
            throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextLong());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException();
        }
    }

    private void readDouble(Scanner scanner, VariableBoxer<Double> variableBoxer) throws InvalidNumericFormatException {
        try {
            variableBoxer.set(scanner.nextDouble());
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException();
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
                throw new WrongTypeInputException();
            }
        }
    }


    public void readln() {
        if (activity != null) {
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
        }
    }

    public void readln(VariableBoxer<Object> variableBoxer) throws NumberFormatException, RuntimePascalException {
        setValueForVariables(variableBoxer);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer a2) throws RuntimePascalException {
        setValueForVariables(a1, a2);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2, VariableBoxer<Object> a3) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5, VariableBoxer<Object> a6) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6);
    }

    public void readln(VariableBoxer<Object> a1, VariableBoxer<Object> a2,
                       VariableBoxer<Object> a3, VariableBoxer<Object> a4,
                       VariableBoxer<Object> a5, VariableBoxer<Object> a6,
                       VariableBoxer<Object> a7) throws RuntimePascalException {
        setValueForVariables(a1, a2, a3, a4, a5, a6, a7);
    }


    public void printf(String format, Object... args) {
        if (stdout != null) stdout.printf(format, args);
    }

}
