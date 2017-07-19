package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ProcedureToken extends BasicToken {
    public ProcedureToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "procedure";
    }

    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
