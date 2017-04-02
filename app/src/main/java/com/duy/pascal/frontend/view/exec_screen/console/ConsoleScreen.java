package com.duy.pascal.frontend.view.exec_screen.console;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.duy.pascal.frontend.view.exec_screen.ScreenObject;

/**
 * Created by Duy on 26-Mar-17.
 */

public class ConsoleScreen implements ScreenObject {
    public int maxLines = 200;

    public int consoleColumn;
    public int row;
    public int firstLine;
    public boolean fullscreen = false;
    private boolean fullScreen = false;
    /**
     * Cursor of console
     */
    private Paint mBackgroundPaint = new Paint();
    private int visibleWidth = 0;
    private int visibleHeight = 0;
    private int topVisible = 0;
    private int leftVisible = 0;
    private Rect visibleRect = new Rect();

    private int screenSize;

    public int getMaxLines() {
        return maxLines;
    }

    /**
     * number of line text
     *
     * @param maxLines
     */
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public int getConsoleColumn() {
        return consoleColumn;
    }

    /**
     * set screen column
     *
     * @param consoleColumn
     */
    public void setConsoleColumn(int consoleColumn) {
        this.consoleColumn = consoleColumn;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }


    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
    }

    public int getBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
    }

    public int getVisibleWidth() {
        return visibleWidth;
    }

    public void setVisibleWidth(int visibleWidth) {
        this.visibleWidth = visibleWidth;
    }

    public int getVisibleHeight() {
        return visibleHeight;
    }

    public void setVisibleHeight(int visibleHeight) {
        this.visibleHeight = visibleHeight;
    }

    public int getTopVisible() {
        return topVisible;
    }

    public void setTopVisible(int topVisible) {
        this.topVisible = topVisible;
    }

    public int getLeftVisible() {
        return leftVisible;
    }

    public void setLeftVisible(int leftVisible) {
        this.leftVisible = leftVisible;
    }

    public Rect getVisibleRect() {
        return visibleRect;
    }

    public void setVisibleRect(Rect visibleRect) {
        this.visibleRect = visibleRect;
    }


    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public void draw(Canvas canvas, int leftVisible, int topVisible, int w, int h) {
        canvas.drawRect(leftVisible, topVisible, w, h, mBackgroundPaint);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }
}
