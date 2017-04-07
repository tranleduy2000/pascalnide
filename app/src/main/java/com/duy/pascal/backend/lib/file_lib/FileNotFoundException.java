package com.duy.pascal.backend.lib.file_lib;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * This class is throw file not open when call command "readf", "writef" without open file before
 * Created by Duy on 07-Apr-17.
 */
public class FileNotFoundException extends RuntimePascalException {
    public FileNotFoundException(LineInfo line) {
        super(line);
    }

    public FileNotFoundException() {
        super(null);
    }

    public FileNotFoundException(LineInfo line, String mes) {
        super(line, mes);
    }
}
