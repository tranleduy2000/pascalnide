package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineInfo;

public class AssignmentToken extends BasicToken {
    public AssignmentToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return ":=";
    }
}
