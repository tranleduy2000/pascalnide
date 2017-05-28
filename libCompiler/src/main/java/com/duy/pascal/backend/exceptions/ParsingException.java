package com.duy.pascal.backend.exceptions;


import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Exception when parse syntax of the program
 */
public class ParsingException extends Exception {

    /**
     * The lineInfo of lineInfo error
     */
    public LineInfo lineInfo;

    public ParsingException(@Nullable LineInfo lineInfo, String message) {
        super(message);
        this.lineInfo = lineInfo;
    }

    public ParsingException(LineInfo lineInfo) {
        super();
        this.lineInfo = lineInfo;
    }

    public LineInfo getLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(LineInfo lineInfo) {
        this.lineInfo = lineInfo;
    }

    @Override
    public String toString() {
        return lineInfo + ":" + getMessage();
    }

    public boolean isAutoFix() {
        return false;
    }
}
