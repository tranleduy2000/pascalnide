package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class UnAssignableTypeException extends com.duy.pascal.backend.exceptions.ParsingException {
    public RValue rValue;

    public UnAssignableTypeException(RValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
        this.rValue = value;
    }

}
