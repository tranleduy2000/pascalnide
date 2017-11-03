package com.duy.pascal.interperter.source;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileScriptSource implements ScriptSource {
    private File mParent;
    private File mFile;

    public FileScriptSource(File file) {
        if (!file.isFile()) {
            throw new RuntimeException(file + " is not a file");
        }
        this.mFile = file;
        this.mParent = file.getParentFile();
    }


    @Override
    public String[] list() {
        File[] children = mParent.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        if (children == null) {
            return new String[0];
        }
        String[] result = new String[children.length];
        for (int i = 0; i < children.length; i++) {
            result[i] = children[i].getName();
        }
        return result;
    }

    @Override
    public Reader read(String fileName) {
        try {
            return new FileReader(new File(mParent, fileName));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
