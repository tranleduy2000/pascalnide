package com.duy.pascal.backend.lib;

import android.util.Log;

import com.duy.pascal.backend.exceptions.OrdinalExpressionExpectedException;
import com.duy.pascal.backend.lib.exceptions.RangeCheckError;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.ScriptTerminatedException;
import com.js.interpreter.runtime.exception.WrongArgsException;

import java.util.Map;
import java.util.Random;

/**
 * System lib
 * <p>
 * - key event
 * - key read
 * <p>
 * Created by Duy on 07-Mar-17.
 */

public class SystemLib implements PascalLibrary {


    private Random random = new Random();
    private ExecuteActivity activity;

    public SystemLib(ExecuteActivity activity) {
        this.activity = activity;
    }


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    /**
     * key pressed method
     *
     * @return
     */
    public boolean keyPressed() {
        if (activity != null) {
            return activity.getConsoleView().keyPressed();
        }
        return false;
    }

    /**
     * procedure readkey
     */
    public char readKey() {
        assert activity != null;
        return activity.readKey();
    }

    /**
     * delay procedure
     *
     * @param ms - time
     */
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
//            System.err.println("??? Interrupted.");
            e.printStackTrace();
        }
    }

    public int Byte(boolean b) {
        return b ? 1 : 0;
    }

    public int random(int range) {
        Log.d("random", "random: " + range);
        return random.nextInt(range);
    }

    public void randomize() {
        random = new Random(System.currentTimeMillis());
    }

    public void inc(VariableBoxer<Object> boxer) throws RuntimePascalException, WrongArgsException {
        inc(boxer, 1);
    }

    public void inc(VariableBoxer<Object> boxer, Object increment) throws RuntimePascalException, WrongArgsException {
        if (boxer.get() instanceof Long) {
            long count;
            if (increment instanceof Integer) {
                count = Long.parseLong(increment.toString());
            } else if (increment instanceof Long) {
                count = (long) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Long) boxer.get()) + count);
        } else if (boxer.get() instanceof Integer) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Integer) boxer.get()) + count);
        } else if (boxer.get() instanceof Character) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }

            Character character = (Character) boxer.get();
            Character newChar = (char) (character + count);
            boxer.set(newChar);
        } else if (boxer.get() instanceof Boolean) {
            if ((Boolean) boxer.get()) {
                throw new RangeCheckError(boxer);
            } else {
                boxer.set(true);
            }
        } else {
            //throw exception
            throw new WrongArgsException("inc");
//            boxer.set((long) boxer.get() - 1);
        }
    }

    public void dec(VariableBoxer<Object> boxer) throws RuntimePascalException, WrongArgsException {
        dec(boxer, 1);
    }

    public void dec(VariableBoxer<Object> boxer, Object increment) throws RuntimePascalException, WrongArgsException {
        if (boxer.get() instanceof Long) {
            long count;
            if (increment instanceof Integer) {
                count = Long.parseLong(increment.toString());
            } else if (increment instanceof Long) {
                count = (long) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Long) boxer.get()) - count);
        } else if (boxer.get() instanceof Integer) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Integer) boxer.get()) - count);
        } else if (boxer.get() instanceof Character) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }

            Character character = (Character) boxer.get();
            Character newChar = (char) (character - count);
            boxer.set(newChar);
        } else if (boxer.get() instanceof Boolean) {
            if (!(Boolean) boxer.get()) {
                throw new RangeCheckError(boxer);
            } else {
                boxer.set(false);
            }
        } else {
            //throw exception
            throw new WrongArgsException("dec");
//            boxer.set((long) boxer.get() - 1);
        }
    }


    public int ceil(double d) {
        return (int) Math.ceil(d);
    }

    public int trunc(double d) {
        return floor(d);
    }

    public double frac(double d) {
        return d - floor(d);
    }

    public int floor(double d) {
        return (int) Math.floor(d);
    }

    public double abs(double d) {
        return Math.abs(d);
    }

    public int round(double d) {
        return (int) Math.round(d);
    }

    public double sin(double d) {
        return Math.sin(d);
    }

    public double cos(double d) {
        return Math.cos(d);
    }

    public double sqr(double d) {
        return d * d;
    }

    /**
     * square root
     *
     * @param d
     * @return
     * @throws InvalidFloatingPointOperation
     */
    public double sqrt(double d) throws InvalidFloatingPointOperation {
        if (d < 0) {
            throw new InvalidFloatingPointOperation(d);
        }
        return Math.sqrt(d);
    }

    public int pred(int d) {
        return d - 1;
    }

    public int succ(int d) {
        return d + 1;
    }

    /**
     * logarithm function
     *
     * @param d
     * @return
     */
    public double ln(double d) throws InvalidFloatingPointOperation {
        if (d < 0) {
            throw new InvalidFloatingPointOperation(d);
        }
        return Math.log(d);
    }

    public double arctan(double a) {
        return Math.atan(a);
    }

    public double exp(double a) {
        return Math.exp(a);
    }


    public int Int(double x) {
        return (int) x;
    }

    public boolean odd(long x) {
        return x % 2 == 0;
    }

    /**
     * exit program
     * @param value - exitcode
     * @throws ScriptTerminatedException
     */
    public void halt(int value) throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }

    /**
     * exit program
     * @throws ScriptTerminatedException
     */
    public void halt() throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }
}
