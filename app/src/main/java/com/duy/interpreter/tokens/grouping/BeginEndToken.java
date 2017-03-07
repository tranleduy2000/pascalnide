package com.duy.interpreter.tokens.grouping;


import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

public class BeginEndToken extends GrouperToken {

    public BeginEndToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toCode() {
        return "begin";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("begin ");
        if (next != null) {
            builder.append(next);
        }
        for (Token t : this.queue) {
            builder.append(t).append(' ');
        }
        builder.append("end ");
        return builder.toString();
    }

    @Override
    protected String getClosingText() {
        return "end";
    }
}
