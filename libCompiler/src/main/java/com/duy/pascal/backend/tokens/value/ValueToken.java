package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public abstract class ValueToken extends Token {

    public ValueToken(LineInfo line) {
        super(line);
    }

    public abstract Object getValue();

    @Override
    public String toString() {
        return getValue().toString();
    }

    public abstract String toCode();
}
