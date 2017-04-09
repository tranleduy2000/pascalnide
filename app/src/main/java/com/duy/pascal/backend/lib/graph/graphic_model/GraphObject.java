package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.duy.pascal.backend.lib.graph.graphic_model.style.FillType;
import com.duy.pascal.backend.lib.graph.graphic_model.style.LineType;
import com.duy.pascal.backend.lib.graph.graphic_model.style.LineWidth;
import com.duy.pascal.backend.lib.graph.text.TextDirection;
import com.duy.pascal.backend.lib.graph.text.TextJustify;
import com.duy.pascal.backend.lib.graph.text.TextStyle;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();
    public Paint mForegroundPaint = new Paint();
    protected int background;

    protected int textStyle = TextStyle.DefaultFont;
    protected int textDirection = TextDirection.HORIZONTAL_DIR;
    protected int lineWidth = LineWidth.NormWidth;
    protected int lineStyle = LineType.Centerln;
    protected int fillStyle = FillType.EmptyFill;
    protected TextJustify textJustify = new TextJustify();

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getFillStyle() {
        return fillStyle;
    }

    public void setFillStyle(int fillStyle) {
        this.fillStyle = fillStyle;
    }

    public TextJustify getTextJustify() {
        return textJustify;
    }

    public void setTextJustify(TextJustify textJustify) {
        this.textJustify = textJustify;
    }

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
