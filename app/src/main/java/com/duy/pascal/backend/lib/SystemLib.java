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

package com.duy.pascal.backend.lib;

import android.util.Log;

import com.duy.pascal.backend.exceptions.OrdinalExpressionExpectedException;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.runtime_exceptions.InvalidFloatingPointOperation;
import com.duy.pascal.backend.lib.runtime_exceptions.RangeCheckError;
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


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }


    /**
     * delay procedure
     *
     * @param ms - time
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
//            System.err.println("??? Interrupted.");
            e.printStackTrace();
        }
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int Byte(boolean b) {
        return b ? 1 : 0;
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int random(int range) {
        Log.d("random", "random: " + range);
        return random.nextInt(range);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void randomize() {
        random = new Random(System.currentTimeMillis());
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void inc(VariableBoxer<Object> boxer) throws RuntimePascalException, WrongArgsException {
        inc(boxer, 1);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
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

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void dec(VariableBoxer<Object> boxer) throws RuntimePascalException, WrongArgsException {
        dec(boxer, 1);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
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


    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int ceil(double d) {
        return (int) Math.ceil(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int trunc(double d) {
        return floor(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double frac(double d) {
        return d - floor(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int floor(double d) {
        return (int) Math.floor(d);
    }


    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int abs(int d) {
        return Math.abs(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public long abs(long d) {
        return Math.abs(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double abs(double d) {
        return Math.abs(d);
    }


    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int round(double d) {
        return (int) Math.round(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double sin(double d) {
        return Math.sin(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double cos(double d) {
        return Math.cos(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double sqr(double d) {
        return d * d;
    }

    /**
     * square root
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double sqrt(double d) throws InvalidFloatingPointOperation {
        if (d < 0) {
            throw new InvalidFloatingPointOperation(d);
        }
        return Math.sqrt(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int pred(int d) {
        return d - 1;
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int succ(int d) {
        return d + 1;
    }

    /**
     * logarithm function
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double ln(double d) throws InvalidFloatingPointOperation {
        if (d < 0) {
            throw new InvalidFloatingPointOperation(d);
        }
        return Math.log(d);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double arctan(double a) {
        return Math.atan(a);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public double exp(double a) {
        return Math.exp(a);
    }


    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int Int(double x) {
        return (int) x;
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public boolean odd(long x) {
        return x % 2 == 0;
    }

    /**
     * exit program
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void halt(int value) throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }

    /**
     * exit program
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void halt() throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public boolean odd(Integer i) {
        return i % 2 == 1;
    }

    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public boolean odd(Long i) {
        return i % 2 == 1;
    }

    /**
     * ascii to character
     *
     * @param i - ascii code
     * @return character
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public char chr(int i) {
        return (char) i;
    }

    /**
     * character to ascii code
     *
     * @param c - input char
     * @return ascii code
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public int ord(char c) {
        return (int) c;
    }

    /**
     * convert number to string
     *
     * @param num input number
     * @param s   output string
     */
    @PascalMethod(description = "Convert number to string", returns = "null")
    @SuppressWarnings("unused")
    public void str(Object num, VariableBoxer<StringBuilder> s) {
//        System.out.print(num);
        s.set(new StringBuilder(String.valueOf(num)));
    }

    /**
     * Calculate numerical/enumerated value of a string.
     *
     * @see {http://www.freepascal.org/docs-html/rtl/system/val.html}
     */
    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void val(String input, VariableBoxer<Object> output, VariableBoxer<Integer> resultCode) throws RuntimePascalException, WrongArgsException {
        try {
            input = input.trim(); //remove white space in start and end postion
            if (output.get() instanceof Long) {
                Long l = Long.parseLong(input);
                output.set(l);
                resultCode.set(1);
            } else if (output.get() instanceof Double) {
                Double d = Double.parseDouble(input);
                output.set(d);
                resultCode.set(1);
            } else if (output.get() instanceof Integer) {
                Integer d = Integer.parseInt(input);
                output.set(d);
                resultCode.set(1);
            } else {
                throw new WrongArgsException("Can not call \"val(string, number, result)\"");
            }
        } catch (NumberFormatException e) {
            resultCode.set(-1);
        }
    }

}
