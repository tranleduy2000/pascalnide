package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public class BeginEndToken extends GrouperToken {

    public BeginEndToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder("begin ");
        if (next != null) {
            builder.append(next.toCode());
        }
        for (Token t : this.queue) {
            builder.append(t.toCode()).append(' ');
        }
        builder.append("end ");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "begin";
    }

    @Override
    protected String getClosingText() {
        return "end";
    }
}
