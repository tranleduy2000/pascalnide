package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Exception when parse syntax of the program
 */
public class ParsingException extends Exception {

    /**
     * The line of line error
     */
    public LineInfo line;

    public ParsingException(LineInfo line, String message) {
        super(message);
        this.line = line;
    }

    public ParsingException(LineInfo line) {
        super();
        this.line = line;
    }

    @Override
    public String toString() {
        return line + ":" + getMessage();
    }

    public boolean isAutoFix() {
        return false;
    }
}
