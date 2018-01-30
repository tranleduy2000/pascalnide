package com.duy.pascal.interperter.tokens.closing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException.Type;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.interperter.tokens.grouping.CaseToken;
import com.duy.pascal.interperter.tokens.grouping.ClassToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.grouping.RecordToken;
import com.duy.pascal.interperter.tokens.grouping.UnitToken;

public class EndToken extends ClosingToken {

    public EndToken(LineNumber line) {
        super(line);
    }

    @Override
    @Nullable
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof BeginEndToken
                || t instanceof CaseToken
                || t instanceof RecordToken
                || t instanceof UnitToken
                || t instanceof ClassToken) {
            return null;
        } else {
            return new GroupingException(getLineNumber(), Type.EXTRA_END, t, this);
        }
    }

    @NonNull
    public String toCode() {
        return toString();
    }

    @Override
    public String toString() {
        return "end";
    }
}
