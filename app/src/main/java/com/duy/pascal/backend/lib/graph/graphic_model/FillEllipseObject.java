package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 19-Apr-17.
 */

public class FillEllipseObject extends GraphObject {
    private int x, y, rx, ry;

    /**
     * @param x  - x coordinate
     * @param y  - y coordinate
     * @param rx - horizontal radius
     * @param ry - vertical radius
     */
    public FillEllipseObject(int x, int y, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
        linePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = rx;
        float dy = ry;

        //bound
        RectF rectF = new RectF(x - dx, y - dy, x + dx, y + dy);
        canvas.drawOval(rectF, linePaint);
        canvas.drawOval(rectF, fillPaint);
    }
}
