package com.duy.interpreter.tokens.value;


import com.duy.interpreter.linenumber.LineInfo;

public class BooleanToken extends ValueToken {
    boolean b;

    public BooleanToken(LineInfo line, boolean b) {
        super(line);
        this.b = b;
    }

    @Override
    public String toCode() {
        return String.valueOf(getValue());
    }

    @Override
    public Object getValue() {
        return b;
    }
}
