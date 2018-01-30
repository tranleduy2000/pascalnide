package com.duy.pascal.interperter.tokens.grouping;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public class BeginEndToken extends GrouperToken {

    public BeginEndToken(LineNumber line) {
        super(line);
    }

    @NonNull
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
