package com.duy.pascal.frontend.view.exec_screen.graph.molel;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class PixelObject extends GraphObject {
    /**
     * It differs from other objects
     */
    private Paint mPaint = new Paint();
    private int x, y;

    public PixelObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PixelObject(int x, int y, int color) {
        this.x = x;
        this.y = y;
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPoint(x, y, mPaint);
    }
}
