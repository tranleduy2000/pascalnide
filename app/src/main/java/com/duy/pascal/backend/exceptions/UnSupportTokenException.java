package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

/**
 * Created by Duy on 16-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class UnSupportTokenException extends ParsingException {
    public Token token;

    public UnSupportTokenException(LineInfo line, Token token) {
        super(line);
    }

    @Override
    public String getMessage() {
        return "Unsupported token " + token;
    }
}
