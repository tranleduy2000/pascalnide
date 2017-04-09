package com.duy.pascal.backend.lib.graph.graphic_model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.backend.lib.graph.text_model.TextDirection;
import com.duy.pascal.backend.lib.graph.text_model.TextFont;
import com.duy.pascal.backend.lib.graph.text_model.TextJustify;
import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();
    protected Paint foregroundPaint = new Paint();
    protected int background;
    protected int textDirection = TextDirection.HORIZONTAL_DIR;
    protected int fillStyle = FillType.EmptyFill;
    protected int fillColor = -1; //white
    protected TextJustify textJustify = new TextJustify();
    protected Paint backgroundPaint = new Paint();
    protected BitmapShader bitmapShader;
    private int textStyle = TextFont.DefaultFont;

    public GraphObject() {
        foregroundPaint.setTextSize(25f);
        foregroundPaint.setStrokeWidth(LineWidth.NormWidth);

    }

    public BitmapShader getBitmapShader() {
        return bitmapShader;
    }

    public void setBitmapShader(BitmapShader bitmapShader) {
        this.bitmapShader = bitmapShader;
    }

    public void setLineWidth(int lineWidth) {
        foregroundPaint.setStrokeWidth(lineWidth);
    }

    public void setLineStyle(int lineStyle) {
        switch (lineStyle) {
            case LineStyle.DottedLn:
                DashPathEffect dottedPathEffect = new DashPathEffect(new float[]{4, 4}, 0);
                foregroundPaint.setPathEffect(dottedPathEffect);
                break;
            case LineStyle.CenterLn:
                DashPathEffect centerLnPathEffect = new DashPathEffect(new float[]{6, 4, 4, 4}, 0);
                foregroundPaint.setPathEffect(centerLnPathEffect);
                break;
            case LineStyle.DashedLn:
                DashPathEffect dashPathEffect = new DashPathEffect(new float[]{6, 4}, 0);
                foregroundPaint.setPathEffect(dashPathEffect);
                break;
            case LineStyle.SolidLn:
                //don't working
                break;
        }


    }

    public int getFillStyle() {
        return fillStyle;
    }

    private Bitmap replaceColor(Bitmap bitmap, int colorToReplace) {

        int red = Color.red(colorToReplace);
        int green = Color.green(colorToReplace);
        int blue = Color.blue(colorToReplace);

        float[] colorTransform = {
                0, red, 0, 0, 0,
                0, 0, green, 0, 0,
                0, 0, 0, blue, 0,
                0, 0, 0, 1f, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f); //Remove Colour
        colorMatrix.set(colorTransform); //Apply the Red

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        Bitmap resultBitmap = Bitmap.createBitmap(bitmap);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }

    public void setFillStyle(Context context, int fillStyle, int color) {
        this.fillStyle = fillStyle;
        this.fillColor = color;
        if (fillStyle != FillType.EmptyFill && fillStyle != FillType.SolidFill) {
            try {
                Resources resources = context.getResources();
                Bitmap sourceBitmap = null;
                switch (fillStyle) {
                    case FillType.LineFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources,
                                R.drawable.graph_line_fill);
                        sourceBitmap = replaceColor(sourceBitmap, Color.parseColor("#00A8A8"), color);
                        break;
                    case FillType.ltSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_it_slash);
                        sourceBitmap = replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.SlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_slash_fill);
                        sourceBitmap = replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.BkSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_bk_slash);
                        sourceBitmap = replaceColor(sourceBitmap,Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.LtBkSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_lt_bk_slash);
                        sourceBitmap = replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    default:
                        break;
                }
                if (sourceBitmap != null) {
                    this.bitmapShader = new BitmapShader(sourceBitmap, Shader.TileMode.REPEAT,
                            Shader.TileMode.REPEAT);
                }
            } catch (Exception ignored) {
                // TODO: 09-Apr-17
            }
        }
    }

    public Bitmap replaceColor(Bitmap src, int fromColor, int targetColor) {
        if (src == null) {
            return null;
        }
        // Source image size
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        //get pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int x = 0; x < pixels.length; ++x) {
            pixels[x] = (pixels[x] == fromColor) ? targetColor : pixels[x];
        }
        // create result bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        //set pixels
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }

    public TextJustify getTextJustify() {
        return textJustify;
    }

    public void setTextJustify(TextJustify textJustify) {
        this.textJustify = textJustify;
    }

    public float getTextSize() {
        return foregroundPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        foregroundPaint.setTextSize(textSize);
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(int textDirection) {
        this.textDirection = textDirection;
    }

    public int getForeground() {
        return foregroundPaint.getColor();
    }

    public void setForegroundColor(int foreground) {
        foregroundPaint.setColor(foreground);
    }

    public int getBackground() {
        return background;
    }

    public void setBackgroundColor(int background) {
        this.background = background;
    }

    public abstract void draw(Canvas canvas);

    public void draw(Bitmap parent) {
        Canvas canvas = new Canvas(parent);
        draw(canvas);
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public Typeface getTextFont() {
        return foregroundPaint.getTypeface();
    }

    public void setTextFont(Typeface textFont) {
        foregroundPaint.setTypeface(textFont);
    }

}
