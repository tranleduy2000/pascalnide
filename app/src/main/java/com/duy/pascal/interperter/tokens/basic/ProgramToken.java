package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ProgramToken extends BasicToken {

    public ProgramToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "program";
    }
}
