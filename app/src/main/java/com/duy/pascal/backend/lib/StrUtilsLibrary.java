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

import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.runtime_exceptions.EConvertError;
import com.js.interpreter.runtime.PascalReference;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Duy on 12-Apr-17.
 */

public class StrUtilsLibrary implements PascalLibrary {
    public static final String NAME = "strUtils".toLowerCase();

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Add characters to the left of a string till a certain length", returns = "void")
    public StringBuilder addChar(char c, StringBuilder s, int index) {
        if (index < s.length()) return s;
        while (s.length() < index) {
            s.insert(0, c);
        }
        return s;
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Add chars at the end of a string till it reaches a certain length",
            returns = "StringBuilder")
    public StringBuilder addCharR(char c, StringBuilder s, int index) {
        if (index < s.length()) return s;
        while (s.length() < index) {
            s.append(c);
        }
        return s;
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Checks whether a string contains a given substring", returns = "void")
    public boolean AnsiContainsStr(StringBuilder s1, StringBuilder s2) {
        return s1.toString().contains(s2.toString());

    }

    /**
     * AnsiEndsStr checks AText to see whether it ends with ASubText, and returns True if it does,
     * False if not. The check is performed case-sensitive. Basically, it checks whether the
     * position of ASubText equals the length of AText minus the length of ASubText plus one.
     */
    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public boolean AnsiEndsStr(StringBuilder sub, StringBuilder text) {
        return text.toString().endsWith(sub.toString());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Check whether a string ends with a certain substring, ignoring case.", returns = "void")
    public boolean AnsiEndsText(String sub, String text) {
        return text.toLowerCase().endsWith(sub.toLowerCase());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Searches, observing case, for a string in an array of strings.", returns = "void")
    public int AnsiIndexStr(String find, String... array) {
        return Arrays.asList(array).indexOf(find);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Searches, case insensitive, for a string in an array of strings.", returns = "void")
    public int AnsiIndexText(String find, String... array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].toLowerCase();
        }
        return Arrays.asList(array).indexOf(find.toLowerCase());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Copies a number of characters starting at the left of a string", returns = "void")
    public String AnsiLeftStr(String text, int count) {
        try {
            return text.substring(0, count - 1);
        } catch (Exception e) {
            return text;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public boolean AnsiMatchStr(String find, String... array) {
        return Arrays.asList(array).contains(find);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public boolean AnsiMatchText(String find, String... array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].toLowerCase();
        }
        return Arrays.asList(array).contains(find.toLowerCase());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a number of characters copied from a given location in a string", returns = "void")
    public StringBuilder AnsiMidStr(StringBuilder find, int start, int count) {
        return new StringBuilder(find.substring(start + 1, start + count));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Pretty-Print a string: make lowercase and capitalize first letters of words", returns = "void")
    public StringBuilder AnsiProperCase(StringBuilder input, char word) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == word) {
                if (input.length() - 1 > i) {
                    input.setCharAt(i + 1, Character.toUpperCase(input.charAt(i + 1)));
                }
            }
        }
        return input;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Search and replace all occurrences of a string, case sensitive.", returns = "void")
    public StringBuilder AnsiReplaceStr(StringBuilder original, String from, String to) {
        return new StringBuilder(original.toString().replace(from, to));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public StringBuilder ReplaceStr(StringBuilder original, String from, String to) {
        return new StringBuilder(original.toString().replace(from, to));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Search and replace all occurrences of a string, case insensitive.", returns = "String")
    public StringBuilder AnsiReplaceText(StringBuilder original, String from, String to) {
        Pattern pattern = Pattern.compile(from, Pattern.CASE_INSENSITIVE);
        return new StringBuilder(original.toString().replaceAll(pattern.toString(), to));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public StringBuilder ReplaceText(StringBuilder original, String from, String to) {
        Pattern pattern = Pattern.compile(from, Pattern.CASE_INSENSITIVE);
        return new StringBuilder(original.toString().replaceAll(pattern.toString(), to));
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public boolean AnsiResemblesText(String s1, String s2) {
        return s1.equals(s2);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Reverse the letters in a string.", returns = "void")
    public StringBuilder AnsiReverseString(StringBuilder in) {
        return new StringBuilder(in).reverse();
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Reverse characters in a string", returns = "void")
    public StringBuilder ReverseString(StringBuilder in) {
        return new StringBuilder(in).reverse();
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Copies a number of characters starting at the right of a string", returns = "void")
    public StringBuilder AnsiRightStr(StringBuilder text, int count) {
        try {
            if (count > text.length()) return text;
            return new StringBuilder(text.substring(text.length() - 1, text.length() - count));
        } catch (Exception e) {
            return new StringBuilder(text);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public StringBuilder RightStr(StringBuilder text, int count) {
        try {
            if (count > text.length()) return text;
            return new StringBuilder(text.substring(text.length() - 1, text.length() - count));
        } catch (Exception e) {
            return new StringBuilder(text);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public StringBuilder RightBStr(StringBuilder text, int count) {
        try {
            if (count > text.length()) return text;
            return new StringBuilder(text.substring(text.length() - 1, text.length() - count));
        } catch (Exception e) {
            return new StringBuilder(text);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Check whether a string starts with a given substring, observing case", returns = "void")
    public boolean AnsiStartsStr(String sub, String text) {
        return text.startsWith(sub);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Check whether a string starts with a given substring, observing case", returns = "void")
    public boolean AnsiStartsText(String sub, String text) {
        return text.toLowerCase().startsWith(sub.toLowerCase());
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns all characters in a string till the first space character (not included).", returns = "void")
    public String Copy2Space(String text) {
        int index = text.indexOf(" ");
        if (index > 0) {
            return text.substring(0, index);
        } else {
            return text;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Deletes and returns all characters in a string till the first space character (not included).", returns = "void")
    public String Copy2SpaceDel(PascalReference<StringBuilder> variableBoxer) throws RuntimePascalException {
        StringBuilder stringBuilder = variableBoxer.get();
        int index = stringBuilder.indexOf(" ");
        if (index > 0 && index < stringBuilder.length()) {
            return stringBuilder.substring(index + 1, stringBuilder.length());
        } else {
            return stringBuilder.toString();
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "str utils library", returns = "void")
    public int indexStr(String text, String... array) {
        return Arrays.asList(array).indexOf(text);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns all characters in a string till a given character (not included).", returns = "void")
    public StringBuilder Copy2Symb(String text, char separator) {
        int index = text.indexOf(separator);
        if (index > 0) {
            return new StringBuilder(text.substring(0, index));
        } else {
            return new StringBuilder(text);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Deletes and returns all characters in a string till a given character (not included).", returns = "void")
    public StringBuilder Copy2SymbDel(PascalReference<StringBuilder> variableBoxer, char separator) throws RuntimePascalException {
        StringBuilder stringBuilder = variableBoxer.get();
        int index = stringBuilder.indexOf(Character.toString(separator));
        if (index > 0 && index < stringBuilder.length()) {
            return new StringBuilder(stringBuilder.substring(index + 1, stringBuilder.length()));
        } else {
            return stringBuilder;
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Convert a decimal number to a string representation, using given a base.", returns = "void")
    public StringBuilder Dec2Numb(long num, byte length, byte base) {
        Long number = num;
        String s = Long.toString(num, base);
        while (s.length() < length) {
            s = " " + s;
        }
        return new StringBuilder(s);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Delete all occurrences of a given character from a string.", returns = "void")
    public StringBuilder DelChars(String source, char charToDel) {
        return new StringBuilder(source.replace(Character.toString(charToDel), ""));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Delete all occurrences of a space from a string.", returns = "void")
    public StringBuilder DelSpace(String source) {
        return new StringBuilder(source.replace(" ", ""));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Reduces sequences of space characters to 1 space character.", returns = "void")
    public StringBuilder DelSpace1(String source) {
        while (source.contains("  ")) {
            source = source.replace("  ", " ");
        }
        return new StringBuilder(source);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Creates and concatenates N copies of a string", returns = "void")
    public StringBuilder DupeString(String text, int count) {
        StringBuilder stringBuilder = new StringBuilder();
        while (count > 0) {
            stringBuilder.append(text);
            count--;
        }
        return stringBuilder;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Converts a hexadecimal string to a decimal value", returns = "void")
    public long hex2Dec(String hex) throws EConvertError {
        try {
            return Long.parseLong(hex, 16);
        } catch (Exception e) {
            throw new EConvertError("hex2Dec", hex);
        }
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Converts an integer to a binary string representation, inserting spaces at fixed locations.", returns = "void")
    public StringBuilder intToBin(long value, int digits, int space) {
        StringBuilder stringBuilder = new StringBuilder(Long.toBinaryString(value));
        while (stringBuilder.length() > digits) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        while (stringBuilder.length() < digits) {
            if (value > 0) {
                stringBuilder.insert(0, "0");
            } else {
                stringBuilder.insert(0, "1");
            }
        }
        if (space != 0) {
            for (int i = stringBuilder.length(); i >= 0; i--) {
                if (i % space == 0) stringBuilder.insert(i, " ");
            }
        }
        return stringBuilder;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Converts an integer to a binary string representation, inserting spaces at fixed locations.", returns = "void")
    public StringBuilder intToBin(long value, int digits) {
        StringBuilder stringBuilder = new StringBuilder(Long.toBinaryString(value));
        while (stringBuilder.length() > digits) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        while (stringBuilder.length() < digits) {
            if (value > 0) {
                stringBuilder.insert(0, "0");
            } else {
                stringBuilder.insert(0, "1");
            }
        }
        return stringBuilder;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Count the number of words in a string.", returns = "void")
    public int WordCount(StringBuilder s, char separator) {
        return s.toString().split(Character.toString(separator)).length;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Search position of Nth word in a string.", returns = "void")
    public int WordPosition(int n, StringBuilder s, char separator) {
        String[] split = s.toString().split(Character.toString(separator));
        if (split.length < n) {
            return 0;
        }
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            res += split[i].length() + 1; //one for split
        }
        //tran le duy -> duy 9
        res += 1; //one for split
        return res;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Convert tab characters to a number of spaces", returns = "void")
    public StringBuilder Tab2Space(StringBuilder s, byte size) {
        String space = "";
        for (byte i = 0; i < size; i++) {
            space += " ";
        }
        return new StringBuilder(s.toString().replace("\t", space));
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Replace part of a string with another string.", returns = "void")
    public StringBuilder StuffString(StringBuilder text, int start, int count, String replace) {
        return text.replace(start - 1, start - 1 + count, replace);
    }

}
