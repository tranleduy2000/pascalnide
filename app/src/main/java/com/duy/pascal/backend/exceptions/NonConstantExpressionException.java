package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class NonConstantExpressionException extends com.duy.pascal.backend.exceptions.ParsingException {

    public NonConstantExpressionException(ReturnsValue value) {
        super(value.getLine(), "The expression \"" + value
                + "\" is not constant.");
    }
}
