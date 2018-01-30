package com.duy.pascal.interperter.tokens;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class OperatorToken extends Token {
    @NonNull
    public final OperatorTypes type;

    public OperatorToken(@NonNull LineNumber line, @NonNull OperatorTypes t) {
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
