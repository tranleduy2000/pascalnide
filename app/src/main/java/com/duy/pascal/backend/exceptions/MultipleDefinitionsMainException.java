package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 12-Mar-17.
 */

public class MultipleDefinitionsMainException extends ParsingException {
    public MultipleDefinitionsMainException(LineInfo line) {
        super(line);
    }

    @Override
    public String getMessage() {
        return "Multiple definitions of main.";
    }
}
