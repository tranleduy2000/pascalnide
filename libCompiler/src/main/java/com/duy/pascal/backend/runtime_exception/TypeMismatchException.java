package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.declaration.lang.types.Type;

/**
 * Created by Duy on 02-May-17.
 */

public class TypeMismatchException extends RuntimePascalException {

    private final LineInfo line;
    private final String functionName;
    private final Type[] acceptTypes;
    private final Type current;

    public TypeMismatchException(LineInfo line, String functionName, Type[] acceptTypes,
                                 Type current) {
        super(line);
        this.line = line;
        this.functionName = functionName;
        this.acceptTypes = acceptTypes;
        this.current = current;
    }

    @Override
    public String getMessage() {
        StringBuilder args = new StringBuilder();
        for (Type acceptType : acceptTypes) {
            args.append(acceptType).append(" | ");
        }
        return "Type mismatch in function " + functionName + "(" + args.toString() + ")" +
                "\nThe current input type is " + current;
    }
}
