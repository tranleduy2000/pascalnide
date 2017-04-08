package com.duy.pascal.backend.lib.file_lib.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Reported by Read, BlockRead, Eof, Eoln, SeekEof or SeekEoln if the file is not opened with Reset.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenForInputException extends RuntimePascalException {
    public String filePath;

    public FileNotOpenForInputException(String filePath) {
        super(null);
        this.filePath = filePath;
    }

}
