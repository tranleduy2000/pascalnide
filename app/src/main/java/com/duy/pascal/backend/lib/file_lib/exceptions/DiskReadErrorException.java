package com.duy.pascal.backend.lib.file_lib.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * An error occurred when reading from disk.
 * Typically happens when you try to read past the end of a file.
 * <p>
 * Created by Duy on 07-Apr-17.
 */

public class DiskReadErrorException extends RuntimePascalException {
    public DiskReadErrorException(LineInfo line) {
        super(line);
    }

    public DiskReadErrorException(LineInfo line, String mes) {
        super(line, mes);
    }

    public DiskReadErrorException() {
        super(null);
    }
}
