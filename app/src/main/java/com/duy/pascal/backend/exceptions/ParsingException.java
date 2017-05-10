package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;

public class ParsingException extends Exception {
    private static final long serialVersionUID = -5592261827134726938L;
    public LineInfo line;

    public ParsingException() {

    }

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
}
