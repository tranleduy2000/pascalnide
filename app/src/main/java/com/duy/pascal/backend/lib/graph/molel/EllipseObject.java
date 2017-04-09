package com.duy.pascal.backend.lib.graph.molel;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Duy on 09-Apr-17.
 */

public class EllipseObject extends GraphObject {
    private int x, y, rx, ry, startAngel, endAngle;

    public EllipseObject(int x, int y, int startAngel, int endAngle, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
        this.startAngel = startAngel;
        this.endAngle = endAngle;
    }


    @Override
    public void draw(Canvas canvas) {
        float dx = rx / 2;
        float dy = ry / 2;

        //bound
        RectF rectF = new RectF(x - dx, y - dy, x + dx, y + dy);

        canvas.save();
        canvas.rotate(-90, x,y);
        canvas.drawArc(rectF, startAngel, endAngle, true, mForegroundPaint);
        canvas.restore();
    }
}
