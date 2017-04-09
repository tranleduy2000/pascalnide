package com.duy.pascal.backend.lib.graph.graphic_model;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.duy.pascal.backend.lib.graph.graphic_model.style.FillType;

/**
 * Created by Duy on 02-Mar-17.
 */

public class BarObject extends GraphObject {
    private int x1, y1, x2, y2;

    public BarObject(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        foregroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        if (fillStyle == FillType.EmptyFill) {
            foregroundPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x1, y1, x2, y2, foregroundPaint);
        } else if (fillStyle == FillType.SolidFill) {
            foregroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            foregroundPaint.setColor(fillColor);
            canvas.drawRect(x1, y1, x2, y2, foregroundPaint);
        } else {
            foregroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(x1, y1, x2, y2, foregroundPaint);
        }
    }
}
