package com.duy.pascal.backend.lib;

import com.duy.pascal.backend.lib.runtime_exceptions.EConvertError;

import java.util.Map;

/**
 * Created by Duy on 12-Apr-17.
 */

public class SysUtilsLibrary implements PascalLibrary {
    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    //Convert a string to an integer value.
    @SuppressWarnings("unused")
    public int strToInt(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(String.valueOf(s));
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    @SuppressWarnings("unused")
    public int StrToDWord(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    //Convert string to DWord (cardinal), using default
    @SuppressWarnings("unused")
    public int StrToDWordDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @SuppressWarnings("unused")
    public long StrToQWord(StringBuilder s) throws EConvertError {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    @SuppressWarnings("unused")
    public long StrToQWordDef(StringBuilder s, int def) {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @SuppressWarnings("unused")
    public int StrToIntDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @SuppressWarnings("unused")
    public double StrToFloat(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to a float, with a default value.
    @SuppressWarnings("unused")
    public double StrToFloatDef(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    @SuppressWarnings("unused")
    public int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to an Int64 value.
    @SuppressWarnings("unused")
    public long StrToInt64(StringBuilder value) throws EConvertError {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt64", value.toString());
        }
    }

    //Convert a string to an Int64 value, with a default value
    @SuppressWarnings("unused")
    public long StrToInt64Def(StringBuilder value, Long def) {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return def;
        }
    }


    //Convert a string to a boolean value
    @SuppressWarnings("unused")
    public boolean StrToBool(StringBuilder s) throws EConvertError {
        try {
            return Boolean.parseBoolean(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToBool", s.toString());
        }
    }

    @SuppressWarnings("unused")
    public boolean StrToBoolDef(String s, boolean def) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return def;
        }
    }

    //Trim whitespace from the ends of a string.
    @SuppressWarnings("unused")
    public StringBuilder trim(StringBuilder s) {
        return new StringBuilder(s.toString().trim());
    }

    //Suspend execution of a program for a certain time.
    @SuppressWarnings("unused")
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Convert null-terminated string to all-uppercase
    @SuppressWarnings("unused")
    public StringBuilder strupper(StringBuilder stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }

    @SuppressWarnings("unused")
    public StringBuilder strupper(Character stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }

}
