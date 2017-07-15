package com.duy.pascal.interperter.tokens.value;


import com.duy.pascal.interperter.linenumber.LineInfo;

public class StringToken extends ValueToken {
    public String value;

    public StringToken(LineInfo line, String s) {
        super(line);
        this.value = s;
        this.line.setLength(toCode().length());
    }

    @Override
    public String toString() {
        return "\'" + value + "\'";
    }

    public String toCode() {
        return "\"" + value + '"';
    }

    @Override
    public Object getValue() {
        return new StringBuilder(value);
    }
}
