package com.js.interpreter.runtime.exception;

/**
 * Created by Duy on 13-Mar-17.
 */

public class WrongArgsException extends Exception {
    public WrongArgsException() {
    }

    public WrongArgsException(String message) {
        super(message);
    }

    public WrongArgsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongArgsException(Throwable cause) {
        super(cause);
    }


}
