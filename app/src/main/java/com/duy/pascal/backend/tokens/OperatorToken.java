package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.linenumber.LineInfo;

public class OperatorToken extends Token {
    public OperatorTypes type;

    public OperatorToken(LineInfo line, OperatorTypes t) {
        super(line);
        this.type = t;
    }

    public boolean can_be_unary() {
        return type.canBeUnary;
    }

    public boolean postfix() {
        return type.postfix;
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
