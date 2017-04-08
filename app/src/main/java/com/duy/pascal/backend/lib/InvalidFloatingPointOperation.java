package com.duy.pascal.backend.lib;

import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Can occur if you try to calculate the square root or logarithm of a negative number.
 * <p>
 * Created by Duy on 08-Apr-17.
 */

class InvalidFloatingPointOperation extends RuntimePascalException {
    private Object object;

    public InvalidFloatingPointOperation(Object d) {
        this.object = d;
    }
}
