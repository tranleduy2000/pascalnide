package com.duy.interpreter.tokens.closing;

import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.grouping.GrouperToken;

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
