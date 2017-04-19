package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 09-Apr-17.
 */

public class EllipseObject extends GraphObject {
    private int x, y, rx, ry;

    /**
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @param rx - horizontal radius
     * @param ry - vertical radius
     */
    public EllipseObject(int x, int y, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = rx;
        float dy = ry;

        //bound
        RectF rectF = new RectF(x - dx, y - dy, x + dx, y + dy);
        canvas.drawOval(rectF, linePaint);
    }
}
