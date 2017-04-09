package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class CircleObject extends GraphObject {
    private int x, y, r;

    public CircleObject(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;

    }

    @Override
    public void draw(Canvas canvas) {
        foregroundPaint.setAntiAlias(true);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, r, foregroundPaint);
    }
}
