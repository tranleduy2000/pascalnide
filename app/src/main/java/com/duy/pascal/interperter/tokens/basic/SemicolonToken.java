package com.duy.pascal.interperter.tokens.basic;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class SemicolonToken extends BasicToken {
    public SemicolonToken(@Nullable LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ";";
    }
}
