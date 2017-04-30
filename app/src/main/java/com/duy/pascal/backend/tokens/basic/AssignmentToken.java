package com.duy.pascal.backend.tokens.basic;


import com.duy.pascal.backend.linenumber.LineInfo;

public class AssignmentToken extends BasicToken {
    public AssignmentToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ":=";
    }
}
