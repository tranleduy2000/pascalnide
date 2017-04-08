package com.duy.pascal.backend.linenumber;

public class LineInfo {
    public int lineNumber;
    public int column = -1;
    public String sourcefile;

    public LineInfo(int lineNumber, String sourcefile) {
        this.lineNumber = lineNumber;
        this.sourcefile = sourcefile;
    }

    public LineInfo(int lineNumber, int column, String sourcefile) {
        this.lineNumber = lineNumber;
        this.column = column;
        this.sourcefile = sourcefile;
    }

    @Override
    public String toString() {
        return "Line " + lineNumber + (column >= 0 ? ":" + column : "") + " " + sourcefile;

    }
}
