package com.duy.pascal.interperter.tokens.value;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class DoubleToken extends ValueToken {
    private Double cacheValue = null;
    private String value;

    public DoubleToken(LineNumber line, String value) {
        super(line);
        this.value = value;
    }

    @Override
    public Object getValue() {
        if (cacheValue != null) return cacheValue;
        cacheValue = Double.parseDouble(value);
        return cacheValue;
    }

    @Override
    public String toString() {
        return value;
    }

    @NonNull
    @Override
    public String toCode() {
        return value;
    }
}
