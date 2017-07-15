package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Created by Duy on 08-May-17.
 */

public class ContinueToken extends BasicToken {

    public ContinueToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "continue";
    }
}
