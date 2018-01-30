package com.duy.pascal.interperter.tokens.value;


import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public abstract class ValueToken extends Token {

    public ValueToken(LineNumber line) {
        super(line);
    }

    public abstract Object getValue();

    @Override
    public String toString() {
        return getValue().toString();
    }

    @NonNull
    public abstract String toCode();
}
