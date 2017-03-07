package com.duy.interpreter.tokens;

import com.duy.interpreter.linenumber.LineInfo;

public class WarningToken extends Token {
    public String message;

    public WarningToken(LineInfo line, String message) {
        super(line);
        this.message = message;
    }

    @Override
    public String toString() {
        return "";
    }
}
