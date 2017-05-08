package com.duy.pascal.frontend.code_editor;

import com.commonsware.cwac.pager.SimplePageDescriptor;

import java.io.File;

/**
 * Created by Duy on 29-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class PageEditInfo extends SimplePageDescriptor {

    private File file;

    public PageEditInfo(File file) {
        super(file.getPath(), file.getName());
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
