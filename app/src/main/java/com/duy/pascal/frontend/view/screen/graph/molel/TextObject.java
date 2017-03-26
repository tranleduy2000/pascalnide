package com.duy.pascal.frontend.view.screen.graph.molel;

import android.graphics.Canvas;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextObject extends GraphObject {
    private String text;
    private int x, y;
    private int textStyle;

    public TextObject(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        paint.setTextSize(11f);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }
}
