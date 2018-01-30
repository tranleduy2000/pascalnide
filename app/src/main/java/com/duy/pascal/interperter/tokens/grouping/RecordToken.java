package com.duy.pascal.interperter.tokens.grouping;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public class RecordToken extends GrouperToken {
    public RecordToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "record";
    }

    @Override
    protected String getClosingText() {
        return "end";
    }

    @Override
    public String toCode() {
        StringBuilder result = new StringBuilder("record ");
        if (next != null) {
            result.append(next.toCode()).append(' ');
        }
        for (Token t : this.queue) {
            result.append(t.toCode()).append(' ');
        }
        result.append("end");
        return result.toString();
    }
}

