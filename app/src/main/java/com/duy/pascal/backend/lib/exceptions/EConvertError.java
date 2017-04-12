package com.duy.pascal.backend.lib.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 12-Apr-17.
 */

public class EConvertError extends RuntimePascalException {
    public EConvertError(String value) {
        super(value + " does not contain a valid hexadecimal value");
    }

    public EConvertError(String method, String value) {
        super("Convert error in " + method + " (" + value + ")");
    }
}
