package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

public class VarToken extends BasicToken {
    public VarToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "var";
    }

    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
