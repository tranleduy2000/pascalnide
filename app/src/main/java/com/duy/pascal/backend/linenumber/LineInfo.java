package com.duy.pascal.backend.linenumber;

public class LineInfo {
    public int line;
    public int column = 0;
    private String sourceFile;

    public LineInfo(int line, String sourceFile) {
        this.line = line;
        this.sourceFile = sourceFile;
    }

    public LineInfo(int line, int column, String sourceFile) {
        this.line = line;
        this.column = column;
        this.sourceFile = sourceFile;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }


    public String getSourceFile() {
        return sourceFile;
    }

    @Override
    public String toString() {
        return "Line " + line + (column >= 0 ? ":" + column : "") + " " + sourceFile;

    }

}
