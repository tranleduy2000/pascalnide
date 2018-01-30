package com.duy.pascal.interperter.tokens.closing;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException.Type;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.grouping.ParenthesizedToken;

public class CloseParenToken extends ClosingToken {

    public CloseParenToken(LineNumber line) {
        super(line);
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        return t instanceof ParenthesizedToken //(
                ? null
                : new GroupingException(getLineNumber(), Type.MISMATCHED_PARENTHESES);
    }

    @NonNull
    public String toCode() {
        return ")";
    }

    @Override
    public String toString() {
        return ")";
    }
}
