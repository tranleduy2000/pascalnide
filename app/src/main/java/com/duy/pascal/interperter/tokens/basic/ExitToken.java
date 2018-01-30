package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 25-Mar-17.
 */

public class ExitToken extends BasicToken {
    public ExitToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "exit";
    }

    @NonNull
    public String toCode() {
        return "exit";
    }
}
