package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 02-Mar-17.
 */

public class ArcObject extends GraphObject {
    private int x, y, stAngle, enAngle, radius;

    public ArcObject(int x, int y, int stAngle, int enAngle, int radius) {
        this.x = x;
        this.y = y;
        this.stAngle = stAngle;
        this.enAngle = enAngle;
        this.radius = radius;
        mForegroundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        //bound of arc
        RectF rectF = new RectF(x - radius, y - radius, x + radius, y + radius);

        //rotate
        canvas.save();
        canvas.rotate(-90, x, y);
        canvas.drawArc(rectF, stAngle, enAngle, false, mForegroundPaint);
        canvas.restore();
    }

}
