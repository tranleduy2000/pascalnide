package com.duy.pascal.interperter.exceptions.parsing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.linenumber.LineInfo;


public class ParsingException extends Exception {
    @Nullable
    private LineInfo lineInfo;

    public ParsingException(@Nullable LineInfo lineInfo, @NonNull String message) {
        super(message);
        this.lineInfo = lineInfo;
    }

    public ParsingException(@Nullable LineInfo lineInfo) {
        this.lineInfo = lineInfo;
    }

    @Nullable
    public final LineInfo getLineInfo() {
        return this.lineInfo;
    }

    public final void setLineInfo(@Nullable LineInfo var1) {
        this.lineInfo = var1;
    }

    @NonNull
    public String toString() {
        return this.lineInfo + ":" + this.getMessage();
    }

    public boolean canAutoFix() {
        return false;
    }
}
