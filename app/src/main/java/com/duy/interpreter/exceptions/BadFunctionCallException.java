package com.duy.interpreter.exceptions;


import com.duy.interpreter.linenumber.LineInfo;

public class BadFunctionCallException extends com.duy.interpreter.exceptions.ParsingException {
    public String functionName;
    public boolean functionExists;
    public boolean numargsMatch;

    public BadFunctionCallException(LineInfo line, String functionName,
                                    boolean functionExists, boolean numargsMatch) {
        super(line);
        this.functionName = functionName;
        this.functionExists = functionExists;
        this.numargsMatch = numargsMatch;
    }

    @Override
    public String getMessage() {
        if (functionExists) {
            if (numargsMatch) {
                return ("One or more arguments has an incorrect type when calling function \"" + functionName + "\".");
            } else {
                return ("Either too few or two many arguments are being passed to function \"" + functionName + "\".");
            }
        } else {
            return ("Can not call function or procedure \"" + functionName + "\", which is not defined.");
        }
    }

}
