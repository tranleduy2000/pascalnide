package com.duy.testapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

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
        int width = getWidth();
        int height = getHeight();

        Bitmap sourceBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas bitmapCanvas = new Canvas(sourceBitmap);
        Paint paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(5f);

        bitmapCanvas.drawRect(width / 2, height / 2, width / 2 + 100, height / 2 + 100, paint);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);

        // if mask, use it
//        Paint fillPaint = new Paint();
//        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        Bitmap maskImage = BitmapFactory.decodeResource(getResources(), R.drawable.graph_bk_slash);
//        BitmapShader bitmapShader;
//        bitmapShader = new BitmapShader(maskImage, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        fillPaint.setShader(bitmapShader);
//        canvas.drawRect(0, 0, width, height, paint);

        Bitmap fillBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.graph_bk_slash);

        FloodFill floodFill = new FloodFill(sourceBitmap, fillBitmap);
        floodFill.fill(width / 2, height / 2, new Random().nextInt());
        int[] image = floodFill.getImagePixels();


        sourceBitmap.setPixels(image, 0, width, 0, 0, width, height);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);


    }
}
