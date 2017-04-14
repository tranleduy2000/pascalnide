package com.duy.pascal.backend.lib.runtime_exceptions;

import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 08-Apr-17.
 */

public class RangeCheckError extends RuntimePascalException {

    public RangeCheckError(VariableBoxer<Object> boxer) {

    }
}
