package com.duy.pascal.interperter.tokens.basic;


import com.duy.pascal.interperter.linenumber.LineNumber;

public class AssignmentToken extends BasicToken {
    public AssignmentToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return ":=";
    }
}
