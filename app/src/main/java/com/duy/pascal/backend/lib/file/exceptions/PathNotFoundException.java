package com.duy.pascal.backend.lib.file.exceptions;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Duy on 13-Apr-17.
 */

public class PathNotFoundException extends FileException {
    public PathNotFoundException(@NonNull String filePath) {
        super(filePath);
    }

    public PathNotFoundException(@NonNull File file) {
        super(file);
    }
}
