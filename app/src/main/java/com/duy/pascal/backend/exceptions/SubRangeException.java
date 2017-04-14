package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 14-Apr-17.
 */

public class SubRangeException extends ParsingException {
    public int low;
    public int high;
    public int size;

    public SubRangeException(int low, int high, LineInfo lineInfo, String message) {
        super(lineInfo, message);
        this.low = low;
        this.high = high;
    }  public SubRangeException(int low, int high, LineInfo lineInfo) {
        super(lineInfo);
        this.low = low;
        this.high = high;
    }
}
