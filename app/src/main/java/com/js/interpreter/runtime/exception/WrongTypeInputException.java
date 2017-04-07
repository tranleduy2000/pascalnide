package com.js.interpreter.runtime.exception;

public class WrongTypeInputException extends RuntimePascalException {
    public WrongTypeInputException() {
        super(null);
    }

    public WrongTypeInputException(String message) {
        super(null, message);
    }
}
