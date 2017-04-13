package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class NonIntegerException extends ParsingException {

    public ReturnsValue value;

    public NonIntegerException(ReturnsValue value) {
        super(value.getLine());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Value must be integers: " + value;
    }
}
