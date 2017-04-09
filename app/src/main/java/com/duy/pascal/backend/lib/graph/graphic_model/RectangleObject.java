package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

import com.duy.pascal.backend.lib.graph.graphic_model.style.LineStyle;

/**
 * Created by Duy on 02-Mar-17.
 */

public class RectangleObject extends GraphObject {
    private int x1, y1, x2, y2;

    public RectangleObject(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        // border
        foregroundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "RectangleObject: (" + x1 + "," + y1 + ") (" + x2 + "," + y2 + ")");
        switch (lineStyle) {
            case LineStyle.Dottedln:
                DashPathEffect dottedPathEffect = new DashPathEffect(new float[]{2, 2}, 0);
                foregroundPaint.setPathEffect(dottedPathEffect);
                break;
            case LineStyle.Centerln:
                DashPathEffect centerLnPathEffect = new DashPathEffect(new float[]{3, 2, 2, 2}, 0);
                foregroundPaint.setPathEffect(centerLnPathEffect);
                break;
            case LineStyle.Dashedln:
                DashPathEffect dashPathEffect = new DashPathEffect(new float[]{3, 2}, 0);
                foregroundPaint.setPathEffect(dashPathEffect);
                break;
            case LineStyle.Solidln:
                //don't working
                break;
        }
        canvas.drawRect(x1, y1, x2, y2, foregroundPaint);
    }
}
