package com.duy.pascal.interperter.exceptions.runtime;

/**
 * Created by Duy on 13-Mar-17.
 */

public class WrongArgsException extends RuntimePascalException {
    private String method;

    public WrongArgsException() {
        super(resId, args);
    }

    public WrongArgsException(String message) {
        super(message);
        this.method = message;
    }


}
