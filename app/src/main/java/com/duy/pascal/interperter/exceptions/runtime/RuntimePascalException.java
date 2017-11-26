package com.duy.pascal.interperter.exceptions.runtime;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class RuntimePascalException extends RuntimeException {
    @Nullable
    public LineInfo line;

    public RuntimePascalException() {
    }

    public RuntimePascalException(@Nullable LineInfo line) {
        this.line = line;
    }

    public RuntimePascalException(String ms) {
        super(ms);
    }

    public RuntimePascalException(@Nullable LineInfo line, String mes) {
        super(mes);
        this.line = line;
    }

    public RuntimePascalException(Exception e) {
    }

    @Nullable
    public LineInfo getLineNumber() {
        return line;
    }
}
