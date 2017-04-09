package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

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
        foregroundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "RectangleObject: (" + x1 + "," + y1 + ") (" + x2 + "," + y2 + ")");


        canvas.drawRect(x1, y1, x2, y2, foregroundPaint);
    }
}
