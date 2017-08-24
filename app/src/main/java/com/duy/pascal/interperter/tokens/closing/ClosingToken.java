package com.duy.pascal.interperter.tokens.closing;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public abstract class ClosingToken extends Token {

    public ClosingToken(LineInfo line) {
        super(line);
    }

    /**
     * Determines if this name can close the give construct, and returns the
     * correct exception if it can't, and null if it can.
     */
    @Nullable
    public abstract GroupingException getClosingException(GrouperToken t);

}
