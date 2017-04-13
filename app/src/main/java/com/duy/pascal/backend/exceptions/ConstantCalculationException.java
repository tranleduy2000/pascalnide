package com.duy.pascal.backend.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ConstantCalculationException extends com.duy.pascal.backend.exceptions.ParsingException {
    public RuntimePascalException e;

    public ConstantCalculationException(RuntimePascalException e) {
        super(e.line, "Error while computing constant value: " + e.getMessage());
        this.e = e;
    }
}
