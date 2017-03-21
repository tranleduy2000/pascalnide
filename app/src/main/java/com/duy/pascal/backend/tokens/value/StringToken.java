package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;

public class StringToken extends ValueToken {
    public String value;

    public StringToken(LineInfo line, String s) {
        super(line);
        this.value = s;
    }

    @Override
    public String toString() {
        return new StringBuilder().append('"').append(value).append('"').toString();
    }

    public String toCode() {
        return "\'" + value + "\'";
    }

    @Override
    public Object getValue() {
        return new StringBuilder(value);
    }
}
