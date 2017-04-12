package com.duy.pascal.backend.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

public class InputStreamNotFoundException extends RuntimePascalException {

    public InputStreamNotFoundException(String message) {
        super(null, message);
    }

    public InputStreamNotFoundException() {
        super("InputStreamNotFoundException");
    }
}
