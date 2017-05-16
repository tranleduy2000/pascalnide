/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.graphics.PathEffect;
import android.graphics.Shader;

import com.duy.pascal.backend.imageprocessing.ImageUtils;
import com.duy.pascal.backend.lib.graph.paint.TextPaint;
import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 02-Mar-17.
 */

public abstract class GraphObject {
    protected final String TAG = GraphObject.class.getSimpleName();

    protected Paint linePaint = new Paint();
    protected Paint fillPaint = new Paint();
    protected TextPaint textPaint = new TextPaint();

    //fill attributes
    protected int fillStyle = FillType.EmptyFill;
    protected int fillColor = -1; //white
    protected BitmapShader bitmapShader;
    //end

    //line attributes
    protected PathEffect pathEffect;


    public GraphObject() {
        linePaint.setStrokeWidth(LineWidth.NormWidth);
        linePaint.setStyle(Paint.Style.STROKE);
//        linePaint.setAntiAlias(true);

        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setLineWidth(int lineWidth) {
        linePaint.setStrokeWidth(lineWidth);
    }

    public void setLineStyle(int lineStyle) {
        switch (lineStyle) {
            case LineStyle.DottedLn:
                pathEffect = new DashPathEffect(new float[]{4, 4}, 0);
                linePaint.setPathEffect(pathEffect);
                break;
            case LineStyle.CenterLn:
                pathEffect = new DashPathEffect(new float[]{6, 4, 4, 4}, 0);
                linePaint.setPathEffect(pathEffect);
                break;
            case LineStyle.DashedLn:
                pathEffect = new DashPathEffect(new float[]{6, 4}, 0);
                linePaint.setPathEffect(pathEffect);
                break;
            case LineStyle.SolidLn:
                //don't working
                break;
        }

    }

    public void setLineColor(int color) {
        linePaint.setColor(color);
        textPaint.setColor(color);
    }

    public void setFillStyle(Context context, int fillStyle, int color) {
        this.fillStyle = fillStyle;
        this.fillColor = color;
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
                case FillType.EmptyFill:
                    break;
                case FillType.SolidFill:
                    fillPaint.setStyle(Paint.Style.FILL);
                    fillPaint.setColor(fillColor);
                    break;
                default:
                    break;
            }
            if (sourceBitmap != null) {
                this.bitmapShader = new BitmapShader(sourceBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                fillPaint.setShader(this.bitmapShader);
            }

        } catch (Exception ignored) {
            // TODO: 09-Apr-17
        }
    }

    public abstract void draw(Canvas canvas);

    public void draw(Bitmap parent) {
        Canvas canvas = new Canvas(parent);
        draw(canvas);
    }

    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
    }
}
