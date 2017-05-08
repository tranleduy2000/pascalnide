package com.js.interpreter.ast;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.Token;

/**
 * Created by Duy on 07-May-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class WrongIfElseStatement extends ParsingException {
    public WrongIfElseStatement(Token next) {
        super(next.lineInfo);
    }

    @Override
    public String getMessage() {
        return "if condition then S1 else S2;\n" +
                "Where, S1 and S2 are different statements. Please note that the statement S1 is not followed by a semicolon.";

    }
}
