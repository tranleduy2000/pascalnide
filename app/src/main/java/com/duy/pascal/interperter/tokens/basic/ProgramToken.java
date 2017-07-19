package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ProgramToken extends BasicToken {

    public ProgramToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "program";
    }
}
