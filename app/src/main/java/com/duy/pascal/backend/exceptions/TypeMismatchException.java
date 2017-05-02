package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 02-May-17.
 */

public class TypeMismatchException extends RuntimePascalException {

    public TypeMismatchException(LineInfo line, String functionName, DeclaredType[] acceptTypes,
                                 DeclaredType current) {

    }
}
