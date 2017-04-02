package com.duy.pascal.frontend.view.exec_screen.graph.molel;

import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextGraphObject extends GraphObject {
    public static final int HORIZONTAL_DIR = 0;
    public static final int VERTICAL_DIR = 1;

    private String text;
    private int x, y;
    private int textStyle;

    public TextGraphObject(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw: " + mPaint.getColor());
        if (textDirection == HORIZONTAL_DIR) {
            canvas.drawText(text, x, y, mPaint);
        } else {
            canvas.save();
            canvas.rotate(90f, 50, 50);
            canvas.drawText(text, x, y, mPaint);
            canvas.restore();
        }
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }
}
