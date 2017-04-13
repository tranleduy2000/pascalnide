package com.duy.pascal.backend.lib.file.exceptions;


import java.io.File;

/**
 * This is reported by Reset, Rewrite, Append, Rename and Erase,
 * if you call them with an unassigned file as a parameter.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotAssignException extends FileException {

    public FileNotAssignException(String filePath) {
        super(filePath);
    }

    public FileNotAssignException(File filePath) {
        super(filePath);
    }

}
