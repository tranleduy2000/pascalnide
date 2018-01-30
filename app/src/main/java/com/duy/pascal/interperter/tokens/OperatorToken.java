package com.duy.pascal.interperter.tokens;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;

public class OperatorToken extends Token {
    public OperatorTypes type;

    public OperatorToken(LineNumber line, OperatorTypes t) {
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
