package com.duy.pascal.backend.lib.file_lib.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Reported by the following functions : Close, Read, Write, Seek, EOf, FilePos, FileSize, Flush,
 * BlockRead, and BlockWrite if the file is not open.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenException extends RuntimePascalException {
    public String filePath;

    public FileNotOpenException(String filePath) {
        super(new LineInfo(-1, "unknow"));
        this.filePath = filePath;
    }
}
