package com.duy.pascal.backend.tokens.grouping;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;
public class CaseToken extends GrouperToken {

    public CaseToken(LineInfo line) {
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
