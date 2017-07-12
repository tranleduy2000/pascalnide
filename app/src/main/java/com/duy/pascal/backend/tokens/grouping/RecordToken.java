package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public class RecordToken extends GrouperToken {
    public RecordToken(LineInfo line) {
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

