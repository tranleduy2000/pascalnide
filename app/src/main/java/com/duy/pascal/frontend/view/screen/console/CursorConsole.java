package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Cursor in console
 * Created by Duy on 26-Mar-17.
 */

public class CursorConsole {
    public int x = 0, y;
    private boolean visible = true;
    private int foreColor = 0;
    private int backColor = 0;
    private Paint cursorPaint = new Paint();
    private int cursorColor = 0;
    private boolean cursorBlink = true;

    public CursorConsole(int x, int y, int cursorColor) {
        this.x = x;
        this.y = y;
        this.cursorColor = cursorColor;
        setupPaint();
    }

    public CursorConsole(int foreColor, int backColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
        setupPaint();

    }

    public CursorConsole() {
        setupPaint();
    }

    private void setupPaint() {
        cursorPaint.setColor(Color.DKGRAY);
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

    public Paint getCursorPaint() {
        return cursorPaint;
    }

    public void setCursorPaint(Paint cursorPaint) {
        this.cursorPaint = cursorPaint;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * draw cursor into the {@link Canvas}
     *
     * @param canvas - canvas of view
     * @param x      -  x coordinate of screen
     * @param y      - y coordinate of screen
     */
    public void drawCursor(Canvas canvas, int x, int y, int CharHeight, int charWidth, int charDescent) {
        if (cursorBlink && visible) {
            canvas.drawRect(x + 1, y - CharHeight + charDescent, x + charWidth, y + 1, cursorPaint);
        }
    }

    public boolean isCursorBlink() {
        return cursorBlink;
    }

    public void setCursorBlink(boolean cursorBlink) {
        this.cursorBlink = cursorBlink;
    }

    public void toggleState() {
        cursorBlink = !cursorBlink;
    }

    /**
     * set position of the screen
     *
     * @param x  - x
     * @param y - y
     */
    public void setCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setStyle(int style, int pattern, int width) {
    }
}
