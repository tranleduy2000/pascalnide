package com.duy.pascal.backend.lib.file_lib;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Reported by Read, BlockRead, Eof, Eoln, SeekEof or SeekEoln if the file is not opened with Reset.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenForInputException extends RuntimePascalException {
    public FileNotOpenForInputException(LineInfo line) {
        super(line);
    }

    public FileNotOpenForInputException() {
        super(null);
    }

    public FileNotOpenForInputException(LineInfo line, String mes) {
        super(line, mes);
    }
}
