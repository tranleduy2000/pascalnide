package com.duy.pascal.backend.tokens.basic;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public abstract class BasicToken extends Token {

    public BasicToken(@Nullable LineInfo line) {
        super(line);
        if (this.line != null) {
            this.line.setLength(toString().length());
        }
    }

    public abstract String toString();

}
