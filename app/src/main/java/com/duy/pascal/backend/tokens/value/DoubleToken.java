package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;

public class DoubleToken extends ValueToken {
    public double value;

    public DoubleToken(LineInfo line, double d) {
        super(line);
        value = d;
    }

    @Override
    public String toString() {
        return "double_of_value[" + value + ']';
    }

    @Override
    public String toCode() {
        return String.valueOf(getValue());
    }

    @Override
    public Object getValue() {
        return value;
    }
}
