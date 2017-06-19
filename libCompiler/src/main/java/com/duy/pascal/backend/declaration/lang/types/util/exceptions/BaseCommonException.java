package com.duy.pascal.backend.declaration.lang.types.util.exceptions;


public class BaseCommonException extends Exception {
    private static final long serialVersionUID = 1031L;

    public BaseCommonException() {
        super();
    }

    public BaseCommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseCommonException(String message) {
        super(message);
    }

    public BaseCommonException(Throwable cause) {
        super(cause);
    }
}
