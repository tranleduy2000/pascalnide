package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class NonConstantExpressionException extends com.duy.pascal.backend.exceptions.ParsingException {

    public NonConstantExpressionException(ReturnValue value) {
        super(value.getLineNumber(), "The expression \"" + value
                + "\" is not constant.");
    }
}
