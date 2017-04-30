package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class NonIntegerException extends ParsingException {

    public RValue value;

    public NonIntegerException(RValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Value must be integers: " + value;
    }
}
