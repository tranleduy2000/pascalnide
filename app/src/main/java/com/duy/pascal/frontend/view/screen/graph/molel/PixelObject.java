package com.duy.pascal.frontend.view.screen.graph.molel;

import android.graphics.Canvas;

/**
 * Created by Duy on 02-Mar-17.
 */

public class PixelObject extends GraphObject {
    private int x, y;

    public PixelObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPoint(x, y, paint);
    }
}
