package com.duy.pascal.interperter.tokens.value;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class LongToken extends ValueToken {
    private Long cacheValue = null;
    private String value;

    public LongToken(LineNumber line, String value) {
        super(line);
        this.value = value;
        this.lineNumber.setLength(value.length());
    }

    @Override
    public Object getValue() {
        if (cacheValue != null) return cacheValue;
        cacheValue = Long.parseLong(value);
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
