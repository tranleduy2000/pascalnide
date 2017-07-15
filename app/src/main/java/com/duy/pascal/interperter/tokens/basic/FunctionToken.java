package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class FunctionToken extends BasicToken {
    public FunctionToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "function";
    }


    @Override
    public boolean canDeclareInInterface() {
        return true;
    }
}
