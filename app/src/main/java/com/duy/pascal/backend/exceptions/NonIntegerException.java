package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class NonIntegerException extends ParsingException {

    public ReturnValue value;

    public NonIntegerException(ReturnValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Value must be integers: " + value;
    }
}
