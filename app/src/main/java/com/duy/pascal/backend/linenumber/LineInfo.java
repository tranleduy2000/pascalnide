package com.duy.pascal.backend.linenumber;

public class LineInfo {
    public int line;
    public int column = 0;
    public String sourceFile;

    public LineInfo(int line, String sourceFile) {
        this.line = line;
        this.sourceFile = sourceFile;
    }

    public LineInfo(int line, int column, String sourceFile) {
        this.line = line;
        this.column = column;
        this.sourceFile = sourceFile;
    }

    @Override
    public String toString() {
        return "Line " + line + (column >= 0 ? ":" + column : "") + " " + sourceFile;

    }
}
