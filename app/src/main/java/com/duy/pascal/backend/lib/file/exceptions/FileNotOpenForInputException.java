package com.duy.pascal.backend.lib.file.exceptions;

/**
 * Reported by Read, BlockRead, Eof, Eoln, SeekEof or SeekEoln if the file is not opened with Reset.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotOpenForInputException extends FileException {

    public FileNotOpenForInputException(String filePath) {
        super(filePath);
    }

}
