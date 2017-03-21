package com.duy.pascal.backend.tokens.basic;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 02-Mar-17.
 */

public class UsesToken extends BasicToken {
    public UsesToken(LineInfo line) {
        super(line);
    }


    @Override
    public String toString() {
        return "uses";
    }
}
