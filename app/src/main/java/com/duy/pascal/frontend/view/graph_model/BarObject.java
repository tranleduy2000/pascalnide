package com.duy.pascal.frontend.view.graph_model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class BarObject extends RectangleObject {

    public BarObject(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        // fill
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
