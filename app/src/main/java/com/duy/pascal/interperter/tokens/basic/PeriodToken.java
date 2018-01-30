package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class PeriodToken extends BasicToken {
    public PeriodToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return ".";
    }

    @Override
    public Precedence getOperatorPrecedence() {
        return Precedence.Dereferencing;
    }
}
