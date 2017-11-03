package com.duy.pascal.interperter.source;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FileScriptSource implements ScriptSource {
    private File directory;

    public FileScriptSource(File directory) {
        this.directory = directory;
    }

    public FileScriptSource(String directory) {
        this.directory = new File(directory);
    }

    @Override
    public String[] list() {
        File[] children = directory.listFiles(new FileFilter() {

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
            return new FileReader(new File(directory, fileName));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
