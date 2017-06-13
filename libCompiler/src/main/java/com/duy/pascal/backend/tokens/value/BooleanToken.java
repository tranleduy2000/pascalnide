package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;

public class BooleanToken extends ValueToken {
    boolean b;

    public BooleanToken(LineInfo line, boolean b) {
        super(line);
        this.b = b;
        if (this.line != null) {
            this.line.setLength(String.valueOf(b).length());
        }
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
