package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class SemicolonToken extends BasicToken {
    public SemicolonToken(@Nullable LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return ";";
    }
}
