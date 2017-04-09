package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.duy.pascal.backend.lib.graph.graphic_model.style.FillType;
import com.duy.pascal.backend.lib.graph.graphic_model.style.LineType;
import com.duy.pascal.backend.lib.graph.graphic_model.style.LineWidth;
import com.duy.pascal.backend.lib.graph.text.TextDirection;
import com.duy.pascal.backend.lib.graph.text.TextJustify;
import com.duy.pascal.backend.lib.graph.text.TextFont;

/**
 *
 *
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();
    protected Paint foregroundPaint = new Paint();
    protected int background;

    protected int textStyle = TextFont.DefaultFont;

    public Typeface getTextFont() {
        return textFont;
    }

    public void setTextFont(Typeface textFont) {
        this.textFont = textFont;
    }

    protected Typeface textFont = null;
    protected int textDirection = TextDirection.HORIZONTAL_DIR;
    protected int lineWidth = LineWidth.NormWidth;
    protected int lineStyle = LineType.Centerln;
    protected int fillStyle = FillType.EmptyFill;
    protected int fillColor = -1; //white

    protected TextJustify textJustify = new TextJustify();
    protected Paint backgroundPaint = new Paint();

    public GraphObject() {
        foregroundPaint.setTextSize(25f);
    }

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

    public float getTextSize() {
        return foregroundPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        foregroundPaint.setTextSize(textSize);
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
        return foregroundPaint.getColor();
    }

    public void setForegroundColor(int foreground) {
        foregroundPaint.setColor(foreground);
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
        this.foregroundPaint = mPaint;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(Paint mBackgroundPaint) {
        this.backgroundPaint = mBackgroundPaint;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
}
