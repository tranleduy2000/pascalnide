package com.duy.pascal.interperter.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.tokenizer.Lexer;
import com.duy.pascal.interperter.tokens.EOFToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.ui.common.utils.IOUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;

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
        this.mName = mFile.getName();
    }

    public FileScriptSource(@NonNull Reader reader, @NonNull String name) {
        this.mReader = reader;
        this.mName = name;
    }

    @Override
    public String toString() {
        return "FileScriptSource{" +
                "mParent=" + mParent +
                ", mFile=" + mFile +
                ", mSourceCode='" + mSourceCode + '\'' +
                ", mReader=" + mReader +
                ", mName='" + mName + '\'' +
                '}';
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
        if (mReader != null) {
            try {
                mReader.reset();
                return mReader;
            } catch (IOException e) {
            }
        }
        try {
            mReader = new FileReader(mFile);
        } catch (FileNotFoundException ignored) {
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

    @Override
    public String getContent() {
        if (mSourceCode != null) {
            return mSourceCode;
        }
        try {
            mSourceCode = IOUtils.toString(stream());
        } catch (IOException ignored) {
        }
        return mSourceCode;
    }

    @Override
    public LinkedList<Token> toTokens() throws IOException {
        Lexer lexer = new Lexer(stream(), getName(), new ArrayList<ScriptSource>());
        LinkedList<Token> stack = new LinkedList<>();
        Token token = lexer.yylex();
        while (!(token instanceof EOFToken)) {
            stack.add(token);
            token = lexer.yylex();
        }
        stack.add(token);
        return stack;
    }

}
