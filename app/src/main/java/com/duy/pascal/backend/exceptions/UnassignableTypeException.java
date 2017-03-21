package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.pascal.backend.exceptions.*;

public class UnassignableTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public UnassignableTypeException(ReturnsValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
    }

}
