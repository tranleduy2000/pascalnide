package com.duy.pascal.backend.lib.graph.graphic_model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;

import com.duy.pascal.backend.imageprocessing.ImageUtils;
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

    public void setFillStyle(Context context, int fillStyle, int color) {
        this.fillStyle = fillStyle;
        this.fillColor = color;
        if (fillStyle != FillType.EmptyFill && fillStyle != FillType.SolidFill) {
            try {
                Resources resources = context.getResources();
                Bitmap sourceBitmap = null;
                switch (fillStyle) {
                    case FillType.LineFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_line_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#00A8A8"), color);
                        break;
                    case FillType.ltSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_it_slash);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.SlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_slash_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.BkSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_bk_slash);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.LtBkSlashFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_lt_bk_slash);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.HatchFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_hatch_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.XHatchFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_xhatch_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.InterLeaveFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_inter_leave_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.WideDotFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_wide_dot_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
                        break;
                    case FillType.CloseDotFill:
                        sourceBitmap = BitmapFactory.decodeResource(resources, R.drawable.graph_close_dot_fill);
                        sourceBitmap = ImageUtils.replaceColor(sourceBitmap, Color.parseColor("#0000A8"), color);
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
