package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class UnAssignableTypeException extends com.duy.pascal.backend.exceptions.ParsingException {
    public ReturnsValue returnsValue;

    public UnAssignableTypeException(ReturnsValue value) {
        super(value.getLine(), "The expression " + value
                + " cannot have a value assigned to it.");
        this.returnsValue = value;
    }

}
