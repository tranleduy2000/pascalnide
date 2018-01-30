package com.duy.pascal.interperter.tokens.basic;

import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 08-May-17.
 */

public class ContinueToken extends BasicToken {

    public ContinueToken(LineNumber line) {
        super(line);
    }

    @Override
    public String toString() {
        return "continue";
    }
}
