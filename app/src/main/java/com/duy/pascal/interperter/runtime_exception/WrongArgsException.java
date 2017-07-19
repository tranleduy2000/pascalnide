package com.duy.pascal.interperter.runtime_exception;

/**
 * Created by Duy on 13-Mar-17.
 */

public class WrongArgsException extends RuntimePascalException {
    private String method;

    public WrongArgsException() {
    }

    public WrongArgsException(String message) {
        super(message);
        this.method = message;
    }


}
