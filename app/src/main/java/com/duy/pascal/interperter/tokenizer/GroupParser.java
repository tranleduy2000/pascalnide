package com.duy.pascal.interperter.tokenizer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokens.other.EOFToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.closing.ClosingToken;
import com.duy.pascal.interperter.tokens.grouping.BaseGrouperToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.ignore.CompileDirectiveToken;
import com.duy.pascal.interperter.tokens.ignore.GroupingExceptionToken;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class GroupParser {
    private BaseGrouperToken mTokenQueue;
    private Stack<GrouperToken> mGroupers;
    private Lexer mLexer;

    public GroupParser(@NonNull ScriptSource source, @Nullable List<ScriptSource> include) throws GroupingException {
        this.mLexer = new Lexer(source.stream(), source.getName(), include);
        this.mGroupers = new Stack<>();
        this.mTokenQueue = new BaseGrouperToken(new LineNumber(0, source.getName()));
        this.mGroupers.push(mTokenQueue);
    }

    public BaseGrouperToken getTokenQueue() {
        return mTokenQueue;
    }

    private void TossException(GroupingException e) {
        GroupingExceptionToken t = new GroupingExceptionToken(e);
        for (GrouperToken g : mGroupers) {
            g.put(t);
        }
    }

    private void TossException(LineNumber line, GroupingException.Type t) {
        GroupingExceptionToken gt = new GroupingExceptionToken(line, t);
        for (GrouperToken g : mGroupers) {
            g.put(gt);
        }
    }

    public void parse() {
        while (true) {
            GrouperToken topOfStack = mGroupers.peek();
            try {
                Token t = mLexer.yylex();
                if (t instanceof EOFToken) {
                    if (mGroupers.size() != 1) {
                        TossException(((EOFToken) t).getClosingException(topOfStack));
                    } else {
                        topOfStack.put(t);
                    }
                    return;
                } else if (t instanceof ClosingToken) {
                    GroupingException g = ((ClosingToken) t).getClosingException(topOfStack);
                    if (g == null) {
                        topOfStack.put(new EOFToken(t.getLineNumber()));
                        topOfStack.setEndLine(t.getLineNumber());
                        mGroupers.pop();
                        continue;
                    } else {
                        TossException(g);
                        return;
                    }
                }
                if (t instanceof CompileDirectiveToken) {
                    topOfStack.put(t);
                    continue;
                }

                // Everything else passes through normally.
                topOfStack.put(t);
                if (t instanceof GrouperToken) {
                    mGroupers.push((GrouperToken) t);
                }
            } catch (IOException e) {
                GroupingException g = new GroupingException(topOfStack.getLineNumber(),
                        GroupingException.Type.IO_EXCEPTION);
                g.setCaused(e);
                TossException(g);
                e.printStackTrace();
                return;
            }
        }
    }

}
