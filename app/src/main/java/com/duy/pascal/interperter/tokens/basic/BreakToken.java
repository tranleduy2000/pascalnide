package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 25-Mar-17.
 */


public class BreakToken extends BasicToken {
    public BreakToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "break";
    }

    @NonNull
    public String toCode() {
        return "break";
    }
}
