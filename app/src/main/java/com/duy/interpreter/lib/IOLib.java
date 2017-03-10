package com.duy.interpreter.lib;

import com.duy.interpreter.startup.PascalCompiler;
import com.duy.pascal.compiler.activities.ExecuteActivity;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class IOLib implements PascalLibrary {

    public static final String TAG = IOLib.class.getSimpleName();

    PrintStream stdout;
    Scanner stdin;
    ExecuteActivity activity;

    /**
     * constructor call by {@link ClassLoader} in {@link PascalCompiler}
     *
     * @param activity
     */
    public IOLib(ExecuteActivity activity) {
        System.out.println("IOLib activity");
        this.activity = activity;
        if (!PascalCompiler.android) {
            this.stdout = System.out;
            this.stdin = new Scanner(System.in);
        }
    }

    /**
     * default constructor
     */
    public IOLib() {
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

    /**
     * Writeln procedure
     *
     * @param values - list variable
     */
    public void writeln(Object... values) {
        write(values);
        if (stdout != null) stdout.println();
        if (activity != null) activity.getConsoleView().emitChar('\n');
    }


    /**
     * write procedure
     *
     * @param values - list variable
     */
    public void write(Object... values) {
        StringBuilder result = new StringBuilder();
        for (Object o : values) {
            result.append(o);
        }
        if (stdout != null) stdout.print(result);
        if (activity != null) activity.getConsoleView().emitString(result.toString());
    }

    /**
     * readln procedure
     */
    public void readln(VariableBoxer<Object> s) throws RuntimePascalException, NumberFormatException {
        if (activity == null) {
            throw new RuntimeException("Can not define ExecuteActivity");
        }
        System.out.println("readln: " + s.get().getClass());
        String inp = "";
        if (s.get() instanceof StringBuilder) {
            //read string
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
            inp = activity.getInput();
            s.set(new StringBuilder(inp));
        } else if (s.get() instanceof Double) {
            //read real
            inp = inp.replaceAll("\\s+", "");
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Double.parseDouble(inp));
        } else if (s.get() instanceof Integer) {
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Integer.parseInt(inp));
        } else if (s.get() instanceof Long) {
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Long.parseLong(inp));
        } else if (s.get() instanceof Character) {
            while (inp.trim().length() < 1) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
                s.set(inp.charAt(0));
            }
        }
    }

    /**
     * readln procedure
     */
    public void readlnn(Object o) throws RuntimePascalException, NumberFormatException {
        System.out.println(o.getClass().getSimpleName());
        if (activity == null) {
            throw new RuntimeException("Can not define ExecuteActivity");
        }
        VariableBoxer s = null;
        if (s == null) return;
        System.out.println("readln: " + s.get().getClass());
        String inp = "";
        if (s.get() instanceof StringBuilder) {
            //read string
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
            inp = activity.getInput();
            s.set(new StringBuilder(inp));
        } else if (s.get() instanceof Double) {
            //read real
            inp = inp.replaceAll("\\s+", "");
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Double.parseDouble(inp));
        } else if (s.get() instanceof Integer) {
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Integer.parseInt(inp));
        } else if (s.get() instanceof Long) {
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Long.parseLong(inp));
        } else if (s.get() instanceof Character) {
            while (inp.trim().length() < 1) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
                s.set(inp.charAt(0));
            }
        }
    }

    private void sleep() {
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * read procedure
     */
    public void read(VariableBoxer<Object> s) throws RuntimePascalException, NumberFormatException {
        if (activity == null) {
            throw new RuntimeException("Can not define ExecuteActivity");
        }
        if (s == null) return;
        String inp = "";
        if (s.get() instanceof StringBuilder) {
            //read string
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
            inp = activity.getInput();
            s.set(new StringBuilder(inp));
        } else if (s.get() instanceof Double) {
            //read real
            inp = inp.replaceAll("\\s+", "");
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Double.parseDouble(inp));
        } else if (s.get() instanceof Integer) {
            inp = inp.replaceAll("\\s+", "");
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Integer.parseInt(inp));
        } else if (s.get() instanceof Long) {
            while (inp.trim().isEmpty()) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
            }
            s.set(Long.parseLong(inp));
        } else if (s.get() instanceof Character) {
            while (inp.trim().length() < 1) {
                activity.startInput();
                while (activity.isInputting()) {
                    sleep();
                }
                inp = activity.getInput();
                s.set(inp.charAt(0));
            }
        }
    }


    /**
     * readln procedure
     */
    public void waitEnter() {
        if (activity != null) {
            activity.startInput();
            while (activity.isInputting()) {
                sleep();
            }
        }
    }

    public void printf(String format, Object... args) {
        if (stdout != null) stdout.printf(format, args);
    }


}
