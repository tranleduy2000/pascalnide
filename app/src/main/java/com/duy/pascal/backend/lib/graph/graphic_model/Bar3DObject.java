package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Duy on 02-Mar-17.
 */

public class Bar3DObject extends RectangleObject {

    public Bar3DObject(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        // fill
        mForegroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
