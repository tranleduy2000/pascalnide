package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.types.OperatorTypes;

public class OperatorToken extends Token {
    public OperatorTypes type;

    public OperatorToken(LineInfo line, OperatorTypes t) {
        super(line);
        this.type = t;
    }

    public boolean canBeUnary() {
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
    public Precedence getOperatorPrecedence() {
        return type.getPrecedence();
    }
}
