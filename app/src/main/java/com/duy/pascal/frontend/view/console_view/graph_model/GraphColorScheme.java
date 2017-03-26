package com.duy.pascal.frontend.view.console_view.graph_model;

import android.graphics.Color;

/**
 * Created by Duy on 26-Mar-17.
 */

public class GraphColorScheme {
    private int backgroundColor = Color.BLACK;
    private int foregroundColor = 0;
    private int cursorForeColor = Color.DKGRAY;
    private int cursorBackColor = Color.DKGRAY;

    public GraphColorScheme() {
    }

    public GraphColorScheme(int backgroundColor, int foregroundColor, int cursorForeColor, int cursorBackColor) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.cursorForeColor = cursorForeColor;
        this.cursorBackColor = cursorBackColor;
    }

    public GraphColorScheme(int backgroundColor, int cursorBackColor) {
        this.backgroundColor = backgroundColor;
        this.cursorBackColor = cursorBackColor;
    }

    public int getCursorForeColor() {
        return cursorForeColor;
    }

    public void setCursorForeColor(int cursorForeColor) {
        this.cursorForeColor = cursorForeColor;
    }

    public int getCursorBackColor() {
        return cursorBackColor;
    }

    public void setCursorBackColor(int cursorBackColor) {
        this.cursorBackColor = cursorBackColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
}
