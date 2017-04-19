package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 *
 * Created by Duy on 09-Apr-17.
 */

public class PieSliceObject extends GraphObject {
    private int x, y, radius, startAngel, endAngle;

    public PieSliceObject(int x, int y, int startAngel, int endAngle, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.startAngel = startAngel;
        this.endAngle = endAngle;
        linePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = radius;
        //bound
        RectF rectF = new RectF(x - dx, y - dx, x + dx, y + dx);

        canvas.save();
        //rotate canvas by 180 degree
        canvas.rotate(-180, x, y);
        //reverse canvas
        canvas.scale(-1, 1, x, y);
        canvas.drawArc(rectF, startAngel, endAngle - startAngel, true, linePaint);
        canvas.restore();
    }
}
