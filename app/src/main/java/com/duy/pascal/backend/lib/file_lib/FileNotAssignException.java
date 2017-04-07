package com.duy.pascal.backend.lib.file_lib;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * This is reported by Reset, Rewrite, Append, Rename and Erase,
 * if you call them with an unassigned file as a parameter.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotAssignException extends RuntimePascalException {
    public FileNotAssignException(LineInfo line) {
        super(line);
    }

    public FileNotAssignException() {
        super(null);
    }

    public FileNotAssignException(LineInfo line, String mes) {
        super(line, mes);
    }
}
