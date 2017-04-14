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
    public int strToInt(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(String.valueOf(s));
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    public int StrToDWord(StringBuilder s) throws EConvertError {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    //Convert string to DWord (cardinal), using default
    public int StrToDWordDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    public long StrToQWord(StringBuilder s) throws EConvertError {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt", s.toString());
        }
    }

    public long StrToQWordDef(StringBuilder s, int def) {
        try {
            return Long.parseLong(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    public int StrToIntDef(StringBuilder s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    public double StrToFloat(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to a float, with a default value.
    public double StrToFloatDef(StringBuilder s, double def) {
        try {
            return Double.parseDouble(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    public int StrToIntDef(String s, int def) {
        try {
            return Integer.parseInt(s.toString());
        } catch (Exception e) {
            return def;
        }
    }

    //Convert a string to an Int64 value.
    public long StrToInt64(StringBuilder value) throws EConvertError {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToInt64", value.toString());
        }
    }

    //Convert a string to an Int64 value, with a default value
    public long StrToInt64Def(StringBuilder value, Long def) {
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return def;
        }
    }


    //Convert a string to a boolean value
    public boolean StrToBool(StringBuilder s) throws EConvertError {
        try {
            return Boolean.parseBoolean(s.toString());
        } catch (Exception e) {
            throw new EConvertError("StrToBool", s.toString());
        }
    }

    public boolean StrToBoolDef(String s, boolean def) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return def;
        }
    }

    //Trim whitespace from the ends of a string.
    public StringBuilder trim(StringBuilder s) {
        return new StringBuilder(s.toString().trim());
    }

    //Suspend execution of a program for a certain time.
    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Convert null-terminated string to all-uppercase
    public StringBuilder strupper(StringBuilder stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }

    public StringBuilder strupper(Character stringBuilder) {
        return new StringBuilder(stringBuilder.toString().toUpperCase());
    }

}
