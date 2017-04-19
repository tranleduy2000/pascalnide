package com.duy.pascal.backend.lib.graph.text_model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.duy.pascal.backend.lib.graph.graphic_model.GraphObject;

/**
 * Created by Duy on 02-Mar-17.
 */

public class TextGraphObject extends GraphObject {
    private String text;
    private int x, y;

    public TextGraphObject(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {


        switch (textJustify.getHorizontal()) {
            case TextJustify.HORIZONTAL_STYLE.CenterText:
//                deltaX = -bounds.width() * 0.5f;
                linePaint.setTextAlign(Paint.Align.CENTER);
                break;
            case TextJustify.HORIZONTAL_STYLE.LeftText:
//                deltaX = 0;
                linePaint.setTextAlign(Paint.Align.LEFT);
                break;
            case TextJustify.HORIZONTAL_STYLE.RightText:
//                deltaX = -bounds.width();
                linePaint.setTextAlign(Paint.Align.RIGHT);
                break;
            default:
                linePaint.setTextAlign(Paint.Align.LEFT);
                break;
        }

        Rect bounds = new Rect();
        linePaint.getTextBounds(text, 0, text.length(), bounds);
        float deltaY = 0;
        switch (textJustify.getVertical()) {
            case TextJustify.VERTICAL_STYLE.CenterText:
                deltaY = -bounds.height() / 2f;
                break;
            case TextJustify.VERTICAL_STYLE.BottomText:
                deltaY = -bounds.height();
                break;
            case TextJustify.VERTICAL_STYLE.TopText:
                deltaY = 0;
                break;
        }

        if (textDirection == TextDirection.HORIZONTAL_DIR) {
            canvas.drawText(text, x, y + deltaY, linePaint);
        } else { //vertical
            canvas.save();
            canvas.rotate(90f, 50, 50);
            canvas.drawText(text, x, y, linePaint);
            canvas.restore();
        }
    }

}
