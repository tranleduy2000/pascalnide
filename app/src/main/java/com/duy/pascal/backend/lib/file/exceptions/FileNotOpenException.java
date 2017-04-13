package com.duy.pascal.backend.lib.file.exceptions;

import java.io.File;

/**
 * Reported by the following functions : Close, Read, Write, Seek, EOf, FilePos, FileSize, Flush,
 * BlockRead, and BlockWrite if the file is not open.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenException extends FileException {
    public FileNotOpenException(String filePath) {
        super(filePath);
    }

    public FileNotOpenException(File filePath) {
        super(filePath);
    }
}
