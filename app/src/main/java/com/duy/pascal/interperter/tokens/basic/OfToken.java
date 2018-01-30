package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class OfToken extends BasicToken {

    public OfToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "of";
    }

    @NonNull
    public String toCode() {
        return "of";
    }
}
