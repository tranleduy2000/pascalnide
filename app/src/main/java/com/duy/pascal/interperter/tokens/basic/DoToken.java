package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class DoToken extends BasicToken {
    public DoToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "do";
    }

    @NonNull
    public String toCode() {
        return toString();
    }
}
