package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Paint;

/**
 * Created by Duy on 26-Mar-17.
 */

public class CursorConsole {
    public int x = 0, y = 0;
    private int foreColor = 0;
    private int backColor = 0;
    private Paint cursorPaint = new Paint();
    private int cursorColor = 0;

    public CursorConsole(int x, int y, int cursorColor) {
        this.x = x;
        this.y = y;
        this.cursorColor = cursorColor;
    }

    public CursorConsole(int foreColor, int backColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
    }

    public CursorConsole() {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getForeColor() {
        return foreColor;
    }

    public void setForeColor(int foreColor) {

        this.foreColor = foreColor;
    }

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }

    public int getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }
}
