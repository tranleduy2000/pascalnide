package com.duy.interpreter.tokens.value;


import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

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
