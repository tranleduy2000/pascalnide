package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ThenToken extends BasicToken {
    public ThenToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "then";
    }

    @NonNull
    public String toCode() {
        return toString();
    }
}
