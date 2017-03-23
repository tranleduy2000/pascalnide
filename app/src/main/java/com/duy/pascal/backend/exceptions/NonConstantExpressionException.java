package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.pascal.backend.exceptions.*;

public class NonConstantExpressionException extends com.duy.pascal.backend.exceptions.ParsingException {

    public NonConstantExpressionException(ReturnsValue value) {
        super(value.getLineNumber(), "The expression \"" + value
                + "\" is not constant.");
    }
}
