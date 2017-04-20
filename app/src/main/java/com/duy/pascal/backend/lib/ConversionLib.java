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

import com.js.interpreter.runtime.exception.WrongArgsException;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.Map;

public class ConversionLib implements PascalLibrary {
    public ConversionLib() {
        //System.out.print("$ " + ConversionLib.class.getSimpleName());
    }

//    public static String BoolToStr(boolean b) {
//        return b ? "True" : "False";
//    }

//    public static StringBuilder IntToStr(int i) {
//        return new StringBuilder(String.valueOf(i));
//    }

//    public static String FloatToStr(double f) {
//        return String.valueOf(f);
//    }

//    public static String CharToStr(char c) {
//        return String.valueOf(c);
//    }

//    public static int strtoint(String s) {
//        return strtointdef(s, -1);
//    }

//    public static int strtointdef(String s, int i) {
//        try {
//            return Integer.parseInt(s);
//        } catch (NumberFormatException e) {
//            return i;
//        }
//    }

    /**
     * ascii to character
     *
     * @param i - ascii code
     * @return character
     */
    public static char chr(int i) {
        return (char) i;
    }

    /**
     * character to ascii code
     *
     * @param c - input char
     * @return ascii code
     */
    public static int ord(char c) {
        return (int) c;
    }

    /**
     * convert number to string
     *
     * @param num input number
     * @param s   output string
     */
    public static void str(Object num, VariableBoxer<StringBuilder> s) {
//        System.out.print(num);
        s.set(new StringBuilder(String.valueOf(num)));
    }

    /**
     * Calculate numerical/enumerated value of a string.
     *
     * @param input
     * @param output
     * @param resultCode
     * @see {http://www.freepascal.org/docs-html/rtl/system/val.html}
     */
    public static void val(String input, VariableBoxer<Object> output, VariableBoxer<Integer> resultCode) throws RuntimePascalException, WrongArgsException {
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

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
