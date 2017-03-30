package com.duy.test.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Duy on 30-Mar-17.
 */

public class DisplayTest extends View {

    Paint paint = new Paint();
    private Bitmap bitmap;
    private Handler handler = new Handler();

    {
        paint.setColor(Color.RED);
    }

    public DisplayTest(Context context) {
        super(context);
        init(context);

    }

    public DisplayTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public DisplayTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(getContext());
    }

    private void init(Context context) {
        bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_4444);
        Paint paint = new Paint();
        paint.setColor(0xff000000);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, 300, 300, paint);
        paint.setColor(0xffffffff);
//        canvas.drawRect(100, 100, 200, 200, paint);
//        canvas.drawText("tran le duy", 20, 20, paint);

        RectF rectF = new RectF(0, 0, 300, 300);
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 90, 45, false, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawPoint(event.getX(), event.getY(), paint);
        }
        return super.onTouchEvent(event);
    }
}
