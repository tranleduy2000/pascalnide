package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class UnAssignableTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public UnAssignableTypeException(ReturnsValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
    }

}
