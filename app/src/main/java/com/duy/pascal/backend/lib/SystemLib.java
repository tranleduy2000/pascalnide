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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    @PascalMethod(description = "system lib", returns = "null")
    @SuppressWarnings("unused")
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
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
        return d - Math.floor(d);
    }

    @PascalMethod(description = "Return the largest integer smaller than or equal to argument", returns = "null")
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


    @PascalMethod(description = "Convert ascii to character", returns = "null")
    @SuppressWarnings("unused")
    public char chr(int i) {
        return (char) i;
    }


    @PascalMethod(description = "Convert character to ascii code", returns = "null")
    @SuppressWarnings("unused")
    public int ord(char c) {
        return (int) c;
    }


    @PascalMethod(description = "Convert number to string", returns = "null")
    @SuppressWarnings("unused")
    public void str(Object num, VariableBoxer<StringBuilder> s) {
        s.set(new StringBuilder(String.valueOf(num)));
    }

    @PascalMethod(description = " Calculate numerical/enumerated value of a string.", returns = "null")
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
    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")

    public  char upcase(Character s) {
        return Character.toUpperCase(s);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  StringBuilder concat(StringBuilder... s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object s1 : s) stringBuilder.append(s1);
        return stringBuilder;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  int pos(String substring, String s) {
        return s.indexOf(substring) + 1;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  int length(String s) {
        System.out.println("length " + s + " " + s.length());
        return s.length();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String findregex(String tosearch, String regex) {
        Pattern reg = Pattern.compile(regex);
        Matcher m = reg.matcher(tosearch);
        if (!m.find()) {
            return "";
        }
        return tosearch.substring(m.start(), m.end());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String between(String s1, String s2, String s) {
        int startindex = s.indexOf(s1) + s1.length();
        int endindex = s.indexOf(s2, startindex);
        return s.substring(startindex, endindex);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String capitalize(String s) {
        boolean lastSpace = true;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (lastSpace) {
                chars[i] = Character.toUpperCase(chars[i]);
                lastSpace = false;
            }
            if (chars[i] == ' ') {
                lastSpace = true;
            }
        }
        return new String(chars);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String getletters(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetter(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String getnumbers(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String getothers(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c) && !Character.isLetter(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  void insert(String s, VariableBoxer<StringBuilder> s1, int pos)
            throws RuntimePascalException {
        s1.set(new StringBuilder(s1.get().insert(pos - 1, s)));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String Left(String s, int count) {
        return s.substring(0, count);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String md5(String s) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("MD5");
        digester.update(s.getBytes());
        return new BigInteger(1, digester.digest()).toString(16);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String padl(String topad, int size) {
        StringBuilder result = new StringBuilder(size);
        for (int i = topad.length(); i < size; i++) {
            result.append(' ');
        }
        result.append(topad);
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String padr(String topad, int size) {
        StringBuilder result = new StringBuilder(size);
        result.append(topad);
        for (int i = topad.length(); i < size; i++) {
            result.append(' ');
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String padz(String topad, int size) {
        StringBuilder result = new StringBuilder(size);
        for (int i = topad.length(); i < size; i++) {
            result.append('0');
        }
        result.append(topad);
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  int posex(String tofind, String s, int startindex) {
        return s.indexOf("\\Q" + tofind, startindex);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  int regexpos(String text, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        m.find();
        int i = m.start();
        if (i >= 0) {
            i++;
        }
        return i;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String replaceregex(String text, String regex,
                                      String replacetext) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.replaceAll(replacetext);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String replicate(char c, int times) {
        StringBuilder result = new StringBuilder(times);
        for (int i = 0; i < times; i++) {
            result.append(c);
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String right(String s, int length) {
        return s.substring(s.length() - length, s.length());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  void setlength(VariableBoxer<StringBuilder> s, int length)
            throws RuntimePascalException {
        String filler = "!@#$%";
        StringBuilder old = s.get();
        if (length <= old.length()) {
            s.set(new StringBuilder(old.subSequence(0, length)));
        } else {
            int extra = length - old.length();
            int count = extra / filler.length();
            StringBuilder result = new StringBuilder(length);
            result.append(old);
            for (int i = 0; i < count; i++) {
                result.append(filler);
            }
            int remaining = extra - count * filler.length();
            result.append(filler.subSequence(0, remaining));
            s.set(result);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  boolean startswith(String prefix, String s) {
        return s.startsWith(prefix);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  char strget(VariableBoxer<StringBuilder> s, int index)
            throws RuntimePascalException {
        return s.get().charAt(index);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  void strset(char c, int index, VariableBoxer<StringBuilder> s)
            throws RuntimePascalException {

        s.get().setCharAt(index, c);
        s.set(s.get());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String stringofchar(char c, int times) {
        return replicate(c, times);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String trimex(String delimeter, String s) {
        int beginningindex = 0;
        while (s.startsWith(delimeter, beginningindex)) {
            beginningindex += delimeter.length();
        }
        int endindex = s.length();
        while (s.lastIndexOf(delimeter, endindex) == endindex
                - delimeter.length()) {
            endindex -= delimeter.length();
        }
        return s.substring(beginningindex, endindex);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String trim(String s) {
        return s.trim();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String trimletters(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetter(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String trimnumbers(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  String trimothers(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || Character.isLetter(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  StringBuilder copy(String s, int ifrom, int count) {
        return new StringBuilder(s.substring(ifrom - 1, ifrom - 1 + count));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "none", returns = "void")
    public  void delete(VariableBoxer<StringBuilder> s, int start, int count)
            throws RuntimePascalException {
        s.set(s.get().delete(start - 1, start + count - 1));
    }

}
