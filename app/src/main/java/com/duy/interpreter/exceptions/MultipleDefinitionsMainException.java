package com.duy.interpreter.exceptions;

import com.duy.interpreter.linenumber.LineInfo;

/**
 * Created by Duy on 12-Mar-17.
 */

public class MultipleDefinitionsMainException extends ParsingException {
    public MultipleDefinitionsMainException(LineInfo line, String message) {
        super(line, message);
    }
}
