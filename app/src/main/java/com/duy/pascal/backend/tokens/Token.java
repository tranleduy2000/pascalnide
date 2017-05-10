package com.duy.pascal.backend.tokens;


import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;

public abstract class Token {
    public LineInfo lineInfo;

    public Token(LineInfo line) {
        this.lineInfo = line;
    }

    public WordToken getWordValue() throws ParsingException {
        throw new ExpectedTokenException("[Identifier]", this);
    }

    /**
     * Null means not an operator
     *
     * @return
     */
    public precedence getOperatorPrecedence() {
        return null;
    }

    public enum precedence {
        Dereferencing, Negation, Multiplicative, Additive, Relational, NoPrecedence
    }

}
