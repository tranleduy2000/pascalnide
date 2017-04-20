package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.linenumber.LineInfo;

public class OperatorToken extends Token {
    public OperatorTypes type;

    public OperatorToken(LineInfo line, OperatorTypes t) {
        super(line);
        this.type = t;
    }

    public boolean canBeUnary() {
        switch (type) {
            case MINUS:
            case NOT:
            case PLUS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public precedence getOperatorPrecedence() {
        return type.getPrecedence();
    }
}
