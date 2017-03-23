package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

public class MultipleDefaultValuesException extends com.duy.pascal.backend.exceptions.ParsingException {

    public MultipleDefaultValuesException(LineInfo line) {
        super(line,
                "Default Values can only be used when declaring variables one at a time.");
    }

}
