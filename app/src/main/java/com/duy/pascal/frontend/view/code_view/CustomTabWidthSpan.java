package com.duy.pascal.frontend.view.code_view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

class CustomTabWidthSpan extends ReplacementSpan {
    int tabWidth = 3;

    CustomTabWidthSpan(int tabWidth) {
        this.tabWidth = tabWidth;
    }

    @Override
    public int getSize(Paint p1, CharSequence p2, int p3, int p4, Paint.FontMetricsInt p5) {
        return tabWidth;
    }

    @Override
    public void draw(Canvas p1, CharSequence p2, int p3, int p4, float p5, int p6, int p7, int p8, Paint p9) {
    }
}