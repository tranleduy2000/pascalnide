package com.duy.pascal.backend.tokens.basic;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;

public class SemicolonToken extends BasicToken {
    public SemicolonToken(@Nullable LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ";";
    }
}
