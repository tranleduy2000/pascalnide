package com.duy.pascal.frontend.view.screen.graph.molel;

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
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        float delta = radius / 2;
        RectF rectF = new RectF((float) x - delta, y - delta, x + delta, y + delta);
        canvas.drawArc(rectF, 90, 45, false, paint);
    }

}
