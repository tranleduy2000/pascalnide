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
    public Paint mForegroundPaint = new Paint();
    protected int background;
    protected int textStyle;
    protected int textDirection = HORIZONTAL_DIR;
    protected Paint mBackgroundPaint = new Paint();

    public GraphObject() {
    }

    public float getTextSize() {
        return mForegroundPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mForegroundPaint.setTextSize(textSize);
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
        return mForegroundPaint.getColor();
    }

    public void setForegroundColor(int foreground) {
        mForegroundPaint.setColor(foreground);
    }

    public int getBackground() {
        return background;
    }

    public void setBackgroundColor(int background) {
        this.background = background;
    }

    public abstract void draw(Canvas canvas);

    public void draw(Bitmap parent) {
        Canvas canvas = new Canvas(parent);
        draw(canvas);
    }

    public void setPaint(Paint mPaint) {
        this.mForegroundPaint = mPaint;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void setBackgroundPaint(Paint mBackgroundPaint) {
        this.mBackgroundPaint = mBackgroundPaint;
    }
}
