package com.duy.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.*;

public class NonConstantExpressionException extends com.duy.interpreter.exceptions.ParsingException {

    public NonConstantExpressionException(ReturnsValue value) {
        super(value.getLineNumber(), "The expression \"" + value
                + "\" is not constant.");
    }
}
