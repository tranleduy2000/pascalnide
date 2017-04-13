package com.duy.pascal.backend.lib.file.exceptions;

/**
 * This class is throw file not open when call command "readf", "writef" without open file before
 * Created by Duy on 07-Apr-17.
 */
public class FileNotFoundException extends FileException {
    public FileNotFoundException(String filePath) {
        super(filePath);
    }
}
