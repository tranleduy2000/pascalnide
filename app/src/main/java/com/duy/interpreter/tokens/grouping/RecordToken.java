package com.duy.interpreter.tokens.grouping;

import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

public class RecordToken extends GrouperToken {
    public RecordToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("record ");
        if (next != null) {
            result.append(next).append(' ');
        }
        for (Token t : this.queue) {
            result.append(t).append(' ');
        }
        result.append("end");
        return result.toString();
    }

    @Override
    protected String getClosingText() {
        return "end";
    }

    @Override
    public String toCode() {
        return "record";
    }
}
