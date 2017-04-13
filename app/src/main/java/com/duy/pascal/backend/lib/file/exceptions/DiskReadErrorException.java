package com.duy.pascal.backend.lib.file.exceptions;

/**
 * An error occurred when reading from disk.
 * Typically happens when you try to read past the end of a file.
 * <p>
 * Created by Duy on 07-Apr-17.
 */

public class DiskReadErrorException extends FileException {

    public DiskReadErrorException(String file) {
        super(file);
    }

    @Override
    public String getMessage() {
        return "Disk read error: " + filePath;
    }
}
