package com.duy.pascal.interperter.exceptions.runtime;

public class WrongTypeInputException extends RuntimePascalException {
    public WrongTypeInputException() {
        super(resId, args);
    }

    public WrongTypeInputException(String message) {
        super(null, message);
    }
}
