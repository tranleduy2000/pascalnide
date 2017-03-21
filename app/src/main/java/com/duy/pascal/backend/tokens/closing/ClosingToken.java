package com.duy.pascal.backend.tokens.closing;

import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

public abstract class ClosingToken extends Token {

    public ClosingToken(LineInfo line) {
        super(line);
    }

    /**
     * Determines if this token can close the give construct, and returns the
     * correct exception if it can't, and null if it can.
     *
     * @param t
     * @return
     */
    public abstract GroupingException getClosingException(GrouperToken t);

}
