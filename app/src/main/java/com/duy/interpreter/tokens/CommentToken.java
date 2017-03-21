package com.duy.interpreter.tokens;

import com.duy.interpreter.linenumber.LineInfo;

/**
 * Created by Duy on 21-Mar-17.
 */

public class CommentToken extends Token {
    public String comment;


    public CommentToken(LineInfo line, String cmt) {
        super(line);
        this.comment = cmt;
        System.out.println("Comment: " + cmt);
    }

    @Override
    public String toString() {
        return comment;
    }
}
