package com.duy.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.*;

public class NonIntegerIndexException extends com.duy.interpreter.exceptions.ParsingException {

    ReturnsValue value;

    public NonIntegerIndexException(ReturnsValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Array indexes must be integers: " + value;
    }
}
