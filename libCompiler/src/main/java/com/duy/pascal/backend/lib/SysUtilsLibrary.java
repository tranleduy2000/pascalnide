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
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.references.PascalReference;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by Duy on 12-Apr-17.
 */
public class SysUtilsLibrary implements PascalLibrary {
    public static final String NAME = "sysUtils".toLowerCase();

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")
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

    @PascalMethod(description = "Append one ansistring to another.")
    public void appendStr(PascalReference<StringBuilder> dest, StringBuilder s) throws RuntimePascalException {
        dest.set(dest.get().append(s));
    }
    @PascalMethod(description = "Convert a string to an integer value.", returns = "int")
    public int strToInt(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(String.valueOf(s));
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public int StrToDWord(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    //Convert string to DWord (cardinal), using default
    @PascalMethod(description = "sys utis library", returns = "void")
    public int StrToDWordDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public long StrToQWord(StringBuilder s) throws EConvertError {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public long StrToQWordDef(StringBuilder s, int def) {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public int StrToIntDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public double StrToFloat(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to a float, with a default value.
    @PascalMethod(description = "sys utis library", returns = "void")
    public double StrToFloatDef(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to an Int64 value.
    @PascalMethod(description = "sys utis library", returns = "void")
    public long StrToInt64(StringBuilder value) throws EConvertError {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt64", value.toString());
        }
    }

    //Convert a string to an Int64 value, with a default value
    @PascalMethod(description = "sys utis library", returns = "void")
    public long StrToInt64Def(StringBuilder value, Long def) {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return def;
        }
    }


    @PascalMethod(description = "Convert a string to a boolean value", returns = "void")
    public boolean StrToBool(StringBuilder s) throws EConvertError {
        try {
            return Boolean.parseBoolean(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToBool", s.toString());
        }
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public boolean StrToBoolDef(String s, boolean def) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return def;
        }
    }

    @PascalMethod(description = "Trim whitespace from the ends of a string.", returns = "void")
    public StringBuilder trim(StringBuilder s) {
        return new StringBuilder(s.toString().trim());
    }

    @PascalMethod(description = "Suspend execution of a program for a certain time.", returns = "void")
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Convert null-TERMINATED string to all-uppercase
    @PascalMethod(description = "sys utis library", returns = "void")
    public StringBuilder strupper(StringBuilder stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }

    @PascalMethod(description = "sys utis library", returns = "void")
    public StringBuilder strupper(Character stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }


    @PascalMethod(description = "Convert a BCD coded integer to a normal integer.")
    public int BCDToInt(int value) {
        return Integer.parseInt(String.valueOf(value), 2);
    }

    @PascalMethod(description = "Convert a float value to a string using a fixed format.")
    public StringBuilder FloatToStr(double value) {
        return new StringBuilder(String.valueOf(value));
    }

    @PascalMethod(description = "Format a float according to a certain mask.")
    public StringBuilder FormatFloat(StringBuilder format, double value) {
        return new StringBuilder(new DecimalFormat(format.toString()).format(value));
    }

    @PascalMethod(description = "Convert an integer value to a hexadecimal string.")
    public StringBuilder IntToHex(long value, int digit) {
        long i = Long.parseLong(String.valueOf(value), digit);
        String obj = Long.toHexString(i);
        String str = String.valueOf(obj);
        return new StringBuilder(str);
    }

    @PascalMethod(description = "Convert an integer value to a decimal string.")
    public StringBuilder IntToStr(long value) {
        return new StringBuilder(String.valueOf(value));
    }

    @PascalMethod(description = "Convert a string to an integer value, with a default value.")
    public long StrToIntDef(StringBuilder input, long def) {
        try {
            return Long.parseLong(input.toString());
        } catch (Exception igrone) {
            return def;
        }
    }

    @PascalMethod(description = "Convert a string to an integer value")
    public long StrToInt(StringBuilder input) throws EConvertError {
        try {
            return Long.parseLong(input.toString());
        } catch (Exception igrone) {
            throw new EConvertError(input.toString());
        }
    }

    @PascalMethod(description = "Convert a string to a floating-point value.")
    public double StrToFloat(StringBuilder input) throws EConvertError {
        try {
            return Double.parseDouble(input.toString());
        } catch (Exception igrone) {
            throw new EConvertError(input.toString());
        }
    }

    @PascalMethod(description = "Compare 2 ansistrings, case sensitive, ignoring accents characters.")
    public double AnsiCompareStr(StringBuilder s1, StringBuilder s2) throws EConvertError {
        return (s1.toString().compareTo(s2.toString()));
    }

    @PascalMethod(description = "Return a lowercase version of a string.")
    public StringBuilder AnsiLowerCase(StringBuilder s1) throws EConvertError {
        return new StringBuilder(s1.toString().toLowerCase());
    }


}
