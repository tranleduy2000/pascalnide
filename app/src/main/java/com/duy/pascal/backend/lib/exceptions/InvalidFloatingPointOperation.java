package com.duy.pascal.backend.lib.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Can occur if you try to calculate the square root or logarithm of a negative number.
 * <p>
 * Created by Duy on 08-Apr-17.
 */

public  class InvalidFloatingPointOperation extends RuntimePascalException {
    private Object object;

    public InvalidFloatingPointOperation(Object d) {
        this.object = d;
    }
}
