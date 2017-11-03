package com.duy.pascal.interperter.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.ui.common.utils.IOUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileScriptSource implements ScriptSource {
    private File mParent;
    private File mFile;
    private String mSourceCode;
    private Reader mReader;
    private String mName;

    public FileScriptSource(@NonNull File file) {
        if (!file.isFile()) {
            throw new RuntimeException(file + " is not a file");
        }
        this.mFile = file;
        this.mParent = file.getParentFile();
    }

    public FileScriptSource(@NonNull Reader reader, @NonNull String name) {
        this.mReader = reader;
        this.mName = name;
    }

    @Override
    public String toString() {
        if (mSourceCode != null) {
            return mSourceCode;
        }
        try {
            if (mFile != null) {
                mSourceCode = IOUtils.toString(new FileInputStream(mFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.toString();
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

    @Nullable
    @Override
    public Reader read(String fileName) {
        try {
            return new FileReader(new File(mParent, fileName));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public Reader stream() {
        if (mReader != null) return mReader;
        try {
            mReader = new FileReader(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return mReader;
    }

    @Override
    public String getName() {
        if (mName != null) {
            return mName;
        }
        return mFile.getName();
    }

}
