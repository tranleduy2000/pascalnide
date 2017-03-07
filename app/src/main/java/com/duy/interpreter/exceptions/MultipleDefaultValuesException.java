package com.duy.interpreter.exceptions;

import com.duy.interpreter.exceptions.*;
import com.duy.interpreter.linenumber.LineInfo;

public class MultipleDefaultValuesException extends com.duy.interpreter.exceptions.ParsingException {

    public MultipleDefaultValuesException(LineInfo line) {
        super(line,
                "Default Values can only be used when declaring variables one at a time.");
    }

}
