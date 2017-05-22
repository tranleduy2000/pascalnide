package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public abstract class BasicToken extends Token {

    public BasicToken(LineInfo line) {
        super(line);
    }


}
