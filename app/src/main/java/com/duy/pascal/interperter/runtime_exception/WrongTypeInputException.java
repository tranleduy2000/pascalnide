package com.duy.pascal.interperter.runtime_exception;

public class WrongTypeInputException extends RuntimePascalException {
    public WrongTypeInputException() {
    }

    public WrongTypeInputException(String message) {
        super(null, message);
    }
}
