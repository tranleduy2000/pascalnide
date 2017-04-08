package com.duy.pascal.backend.lib.file_lib.exceptions;

import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * This is reported by Reset, Rewrite, Append, Rename and Erase,
 * if you call them with an unassigned file as a parameter.
 * <p>
 * Created by Duy on 07-Apr-17.
 */
public class FileNotAssignException extends RuntimePascalException {
    public String filePath;


    public FileNotAssignException(String filePath) {
        super(null);
        this.filePath = filePath;
    }

}
