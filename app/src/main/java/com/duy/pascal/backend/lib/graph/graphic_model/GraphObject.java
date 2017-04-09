package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.duy.pascal.backend.lib.graph.line_style.FillType;
import com.duy.pascal.backend.lib.graph.line_style.LineStyle;
import com.duy.pascal.backend.lib.graph.line_style.LineWidth;
import com.duy.pascal.backend.lib.graph.text_model.TextDirection;
import com.duy.pascal.backend.lib.graph.text_model.TextFont;
import com.duy.pascal.backend.lib.graph.text_model.TextJustify;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();
    protected Paint foregroundPaint = new Paint();
    protected int background;

    private int textStyle = TextFont.DefaultFont;
    protected Typeface textFont = null;
    protected int textDirection = TextDirection.HORIZONTAL_DIR;
    protected int fillStyle = FillType.EmptyFill;
    protected int fillColor = -1; //white
    protected TextJustify textJustify = new TextJustify();
    protected Paint backgroundPaint = new Paint();

    public GraphObject() {
        foregroundPaint.setTextSize(25f);
        foregroundPaint.setStrokeWidth(LineWidth.NormWidth);
    }

    public void setLineWidth(int lineWidth) {
        foregroundPaint.setStrokeWidth(lineWidth);
    }

    public void setLineStyle(int lineStyle) {
        switch (lineStyle) {
            case LineStyle.DottedLn:
                DashPathEffect dottedPathEffect = new DashPathEffect(new float[]{4, 4}, 0);
                foregroundPaint.setPathEffect(dottedPathEffect);
                break;
            case LineStyle.CenterLn:
                DashPathEffect centerLnPathEffect = new DashPathEffect(new float[]{6, 4, 4, 4}, 0);
                foregroundPaint.setPathEffect(centerLnPathEffect);
                break;
            case LineStyle.DashedLn:
                DashPathEffect dashPathEffect = new DashPathEffect(new float[]{6, 4}, 0);
                foregroundPaint.setPathEffect(dashPathEffect);
                break;
            case LineStyle.SolidLn:
                //don't working
                break;
        }
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

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public Typeface getTextFont() {
        return textFont;
    }

    public void setTextFont(Typeface textFont) {
        this.textFont = textFont;
    }

}
