package com.duy.pascal.interperter.tokenizer;


import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokens.EOFToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.closing.ClosingToken;
import com.duy.pascal.interperter.tokens.grouping.BaseGrouperToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.ignore.CompileDirectiveToken;
import com.duy.pascal.interperter.tokens.ignore.GroupingExceptionToken;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Stack;

public class NewLexer {
    private BaseGrouperToken tokenQueue;
    private Stack<GrouperToken> groupers;
    private Lexer lexer;

    public NewLexer(Reader reader, String sourceName,
                    List<ScriptSource> searchDirectories) throws GroupingException {
        this.lexer = new Lexer(reader, sourceName, searchDirectories);
        groupers = new Stack<>();
        tokenQueue = new BaseGrouperToken(new LineInfo(0, sourceName));
        groupers.push(tokenQueue);
    }

    public BaseGrouperToken getTokenQueue() {
        return tokenQueue;
    }

    private void TossException(GroupingException e) {
        GroupingExceptionToken t = new GroupingExceptionToken(e);
        for (GrouperToken g : groupers) {
            g.put(t);
        }
    }

    private void TossException(LineInfo line, GroupingException.Type t) {
        GroupingExceptionToken gt = new GroupingExceptionToken(line, t);
        for (GrouperToken g : groupers) {
            g.put(gt);
        }
    }

    public void parse() {
        while (true) {
            GrouperToken topOfStack = groupers.peek();
            try {
                Token t = lexer.yylex();
                if (t instanceof EOFToken) {
                    if (groupers.size() != 1) {
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
                        groupers.pop();
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
                    groupers.push((GrouperToken) t);
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
