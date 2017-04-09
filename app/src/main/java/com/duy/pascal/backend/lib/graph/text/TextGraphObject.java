package com.duy.pascal.backend.lib.graph.text;

import android.graphics.Canvas;
import android.util.Log;

import com.duy.pascal.backend.lib.graph.graphic_model.GraphObject;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextGraphObject extends GraphObject {
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
        Log.d(TAG, "drawBackground: " + mForegroundPaint.getColor());
        if (textDirection == TextDirection.HORIZONTAL_DIR) {
            canvas.drawText(text, x, y, mForegroundPaint);
        } else { //vertical
            canvas.save();
            canvas.rotate(90f, 50, 50);
            canvas.drawText(text, x, y, mForegroundPaint);
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
