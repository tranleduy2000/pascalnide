package com.duy.pascal.backend.lib.graph.molel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import static com.duy.pascal.backend.lib.graph.molel.TextGraphObject.HORIZONTAL_DIR;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();
    public Paint mPaint = new Paint();
    protected int background;
    protected int textStyle;
    protected int textDirection = HORIZONTAL_DIR;
    protected Paint mBackgroundPaint = new Paint();

    public GraphObject() {

    }

    public float getTextSize() {
        return mPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(int textDirection) {
        this.textDirection = textDirection;
    }

    public int getForeground() {
     return    mPaint.getColor();
    }

    public void setForeground(int foreground) {
        mPaint.setColor(foreground);
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public abstract void draw(Canvas canvas);

    public void draw(Bitmap parent) {
        Canvas canvas = new Canvas(parent);
        draw(canvas);
    }

    public void setPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
    }
}
