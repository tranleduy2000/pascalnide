package com.duy.pascal.interperter.tokens.value;


import com.duy.pascal.interperter.linenumber.LineNumber;

public class IntegerToken extends ValueToken {
    private Integer cacheValue = null;
    private String value;

    public IntegerToken(LineNumber line, String value) {
        super(line);
        this.value = value;
        this.line.setLength(value.length());
    }

    @Override
    public Object getValue() {
        if (cacheValue != null) return cacheValue;
        cacheValue = Integer.parseInt(value);
        return cacheValue;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String toCode() {
        return value;
    }
}
