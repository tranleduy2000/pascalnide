package com.duy.pascal.interperter.tokens.closing;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.grouping.GroupingException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public abstract class ClosingToken extends Token {

    public ClosingToken(LineInfo line) {
        super(line);
    }

    /**
     * Determines if this name can close the give construct, and returns the
     * correct exception if it can't, and null if it can.
     *
     * @param t
     * @return
     */
    public abstract GroupingException getClosingException(GrouperToken t);

}
