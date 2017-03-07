package com.duy.interpreter.exceptions;

import com.duy.interpreter.exceptions.*;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ConstantCalculationException extends com.duy.interpreter.exceptions.ParsingException {
    RuntimePascalException e;

    public ConstantCalculationException(RuntimePascalException e) {
        super(e.line, "Error while computing constant value: " + e.getMessage());
        this.e = e;
    }
}
