package com.duy.pascal.interperter.tokens.value;


import com.duy.pascal.interperter.linenumber.LineNumber;

public class StringToken extends ValueToken {
    public String value;

    public StringToken(LineNumber line, String s) {
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
