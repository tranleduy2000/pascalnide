package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class NonIntegerIndexException extends com.duy.pascal.backend.exceptions.ParsingException {

    public ReturnValue value;

    public NonIntegerIndexException(ReturnValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Array indexes must be integers: " + value;
    }
}
