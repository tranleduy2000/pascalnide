package com.duy.pascal.backend.tokens.grouping;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public class BracketedToken extends GrouperToken {

    public BracketedToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "[";
    }

    public String toCode() {
        StringBuilder tmp = new StringBuilder("[");
        if (next != null) {
            tmp.append(next);
        }
        for (Token t : this.queue) {
            tmp.append(t).append(' ');
        }
        tmp.append(']');
        return tmp.toString();
    }

    @Override
    protected String getClosingText() {
        return "]";
    }

    @Override
    public precedence getOperatorPrecedence() {
        return precedence.Dereferencing;
    }
}
