package com.duy.interpreter.tokens.grouping;


import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;

public class CaseToken extends GrouperToken {

    public CaseToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder("case ");
        if (next != null) {
            tmp.append(next);
        }
        for (Token t : this.queue) {
            tmp.append(t).append(' ');
        }
        tmp.append("end");
        return tmp.toString();

    }

    public String toCode() {
        return "case";
    }

    protected String getClosingText() {
        return "end";
    }
}
