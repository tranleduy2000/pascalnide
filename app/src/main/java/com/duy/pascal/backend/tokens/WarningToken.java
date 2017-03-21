package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.linenumber.LineInfo;

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
