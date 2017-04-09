package com.duy.pascal.backend.lib.graph.molel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class RectangleObject extends GraphObject {
    private int x1, y1, x2, y2;

    public RectangleObject(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        // border
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(x1, y1, x2, y2, mPaint);
    }

    @Override
    public void draw(Bitmap parent) {

    }
}
