package com.duy.pascal.interperter.tokens.grouping;


import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public class CaseToken extends GrouperToken {

    public CaseToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "case";
    }

    public String toCode() {
        StringBuilder tmp = new StringBuilder("case ");
        if (next != null) {
            tmp.append(next.toCode());
        }
        for (Token t : this.queue) {
            tmp.append(t.toCode()).append(' ');
        }
        tmp.append("end");
        return tmp.toString();

    }

    protected String getClosingText() {
        return "end";
    }
}
