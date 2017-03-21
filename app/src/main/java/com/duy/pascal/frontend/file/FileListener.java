package com.duy.pascal.frontend.file;

import java.io.File;

public interface FileListener {
    void onFileClick(File file);

    void onFileLongClick(File file);

    boolean doRemoveFile(File file);
}