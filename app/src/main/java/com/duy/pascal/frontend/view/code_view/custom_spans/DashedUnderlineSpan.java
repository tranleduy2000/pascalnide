package com.duy.pascal.frontend.view.code_view.custom_spans;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.widget.TextView;

/**
 * Created by Duy on 15-Apr-17.
 */

public class DashedUnderlineSpan implements LineBackgroundSpan, LineHeightSpan {
    private Paint paint;
    private TextView textView;
    private float offsetY;
    private float spacingExtra;

    public DashedUnderlineSpan(TextView textView, int color, float thickness, float dashPath,
                               float offsetY, float spacingExtra) {
        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setPathEffect(new DashPathEffect(new float[]{dashPath, dashPath}, 0));
        this.paint.setStrokeWidth(thickness);
        this.textView = textView;
        this.offsetY = offsetY;
        this.spacingExtra = spacingExtra;
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v,
                             Paint.FontMetricsInt fm) {
        fm.ascent -= spacingExtra;
        fm.top -= spacingExtra;
        fm.descent += spacingExtra;
        fm.bottom += spacingExtra;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint p, int left, int right, int top, int baseline,
                               int bottom, CharSequence text, int start, int end, int lnum) {
        int lineNum = textView.getLineCount();
        for (int i = 0; i < lineNum; i++) {
            Layout layout = textView.getLayout();
            canvas.drawLine(layout.getLineLeft(i), layout.getLineBottom(i) - spacingExtra + offsetY,
                    layout.getLineRight(i), layout.getLineBottom(i) - spacingExtra + offsetY,
                    this.paint);
        }
    }
}
