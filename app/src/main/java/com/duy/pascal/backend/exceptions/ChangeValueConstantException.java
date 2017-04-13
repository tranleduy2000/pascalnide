package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 13-Apr-17.
 */

public class ChangeValueConstantException extends ParsingException {
    public String name;
    public Object value;

    public ChangeValueConstantException(LineInfo line, String message, String name) {
        super(line, message);
        this.name = name;
    }

    public ChangeValueConstantException(LineInfo line, String message, String name, Object value) {
        super(line, message);
        this.name = name;
        this.value = value;
    }

    public ChangeValueConstantException(LineInfo line) {
        super(line);
    }
}
