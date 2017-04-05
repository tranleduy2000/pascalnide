package com.duy.pascal.backend.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public class WrongTypeInputException extends RuntimePascalException {
    public WrongTypeInputException() {
        super(null);
    }

    public WrongTypeInputException(String message) {
        super(null, message);
    }
}
