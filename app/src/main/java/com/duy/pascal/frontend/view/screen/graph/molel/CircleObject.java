package com.duy.pascal.frontend.view.screen.graph.molel;

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
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, r, paint);
    }
}
