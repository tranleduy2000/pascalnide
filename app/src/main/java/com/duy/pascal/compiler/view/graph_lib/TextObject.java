package com.duy.pascal.compiler.view.graph_lib;

import android.graphics.Canvas;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextObject extends GraphObject {
    private String text;
    private int x, y;

    public TextObject(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        paint.setTextSize(14f);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
    }
}
