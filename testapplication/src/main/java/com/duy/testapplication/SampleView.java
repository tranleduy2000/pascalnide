package com.duy.testapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Duy on 19-Apr-17.
 */

public class SampleView extends View {

    public SampleView(Context context) {
        super(context);
    }

    public SampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SampleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30f);
        canvas.drawText("PASCAL", 0, 30, paint);


    }
}
