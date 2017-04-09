package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Duy on 09-Apr-17.
 */

public class ArcEllipseObject extends GraphObject {
    private int x, y, rx, ry, startAngel, endAngle;

    public ArcEllipseObject(int x, int y, int startAngel, int endAngle, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
        this.startAngel = startAngel;
        this.endAngle = endAngle;
        mForegroundPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = rx / 2;
        float dy = ry / 2;

        //bound
        RectF rectF = new RectF(x - dx, y - dy, x + dx, y + dy);

        canvas.save();
        //rotate canvas by 180 degree
        canvas.rotate(-180, x, y);
        //reverse canvas
        canvas.scale(-1, 1, x, y);
        canvas.drawArc(rectF, startAngel, endAngle, false, mForegroundPaint);
        canvas.restore();
    }
}
