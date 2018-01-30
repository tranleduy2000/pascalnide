package com.duy.pascal.interperter.exceptions.runtime;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.declaration.lang.types.Type;

/**
 * Created by Duy on 02-May-17.
 */

public class TypeMismatchException extends RuntimePascalException {

    private final LineNumber line;
    private final Name functionName;
    private final Type[] acceptTypes;
    private final Type current;

    public TypeMismatchException(LineNumber line, Name functionName, Type[] acceptTypes,
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
