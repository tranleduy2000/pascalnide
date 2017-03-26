package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Paint;

/**
 * Created by Duy on 26-Mar-17.
 */

public class CursorConsole {
    public int xCoordinate = 0, yCoordinate;
    private int foreColor = 0;
    private int backColor = 0;
    private Paint cursorPaint = new Paint();
    private int cursorColor = 0;

    public CursorConsole(int xCoordinate, int y, int cursorColor) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = y;
        this.cursorColor = cursorColor;
    }

    public CursorConsole(int foreColor, int backColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
    }

    public CursorConsole() {

        yCoordinate = 0;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
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

    public Paint getCursorPaint() {
        return cursorPaint;
    }

    public void setCursorPaint(Paint cursorPaint) {
        this.cursorPaint = cursorPaint;
    }
}
