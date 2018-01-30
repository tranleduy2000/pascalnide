package com.duy.pascal.interperter.tokens.value;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class BooleanToken extends ValueToken {
    boolean b;

    public BooleanToken(LineNumber line, boolean b) {
        super(line);
        this.b = b;
        if (this.lineNumber != null) {
            this.lineNumber.setLength(String.valueOf(b).length());
        }
    }

    @NonNull
    @Override
    public String toCode() {
        return String.valueOf(getValue());
    }

    @Override
    public Object getValue() {
        return b;
    }
}
