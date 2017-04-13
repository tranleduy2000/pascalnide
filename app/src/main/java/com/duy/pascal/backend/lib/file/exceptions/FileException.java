package com.duy.pascal.backend.lib.file.exceptions;

import android.support.annotation.NonNull;

import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;

/**
 * Created by Duy on 13-Apr-17.
 */

public abstract class FileException extends RuntimePascalException {
    public String filePath = "";

    public FileException(@NonNull String filePath) {
        this.filePath = filePath;
    }

    public FileException(@NonNull File file) {
        this.filePath = file.getPath();
    }
}
