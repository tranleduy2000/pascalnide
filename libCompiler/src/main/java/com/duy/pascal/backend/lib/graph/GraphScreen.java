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

package com.duy.pascal.backend.lib.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.duy.pascal.backend.imageprocessing.ImageUtils;
import com.duy.pascal.backend.lib.graph.graphic_model.GraphObject;
import com.duy.pascal.backend.lib.graph.paint.FillPaint;
import com.duy.pascal.backend.lib.graph.paint.LinePaint;
import com.duy.pascal.backend.lib.graph.paint.TextPaint;
import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.backend.lib.graph.style.TextFont;
import com.duy.pascal.backend.lib.graph.style.TextJustify;
import com.duy.pascal.frontend.theme.FontManager;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleCursor;

/**
 * Created by Duy on 30-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class GraphScreen {
    private static final String TAG = GraphScreen.class.getSimpleName();
    private final Context context;
    private final Object mLock = new Object();
    protected GraphObject lastObject;
    protected int fillPattern = FillType.EmptyFill;
    protected int fillColor = -1;//white
    private int width = 1;
    private int height = 1;
    private ViewPort viewPort = new ViewPort(0, 0, width, height);
    //background
    private Paint mBackgroundPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private LinePaint linePaint = new LinePaint();

    //    private int textStyle = TextFont.DefaultFont;
//    private int textDirection = TextDirection.HORIZONTAL_DIR;
//    private Typeface currentFont;
//    private TextJustify textJustify = new TextJustify();
    private FillPaint fillPaint = new FillPaint();
    /**
     * this object used to draw {@link GraphObject}
     */
    private Bitmap mGraphBitmap;
    private ConsoleCursor mCursor = new ConsoleCursor(0, 0, 0xffffffff);
    private int lineWidth = LineWidth.NormWidth;
    private int lineStyle = LineStyle.SolidLn;
    private int linePattern;

    public GraphScreen(Context context) {
        this.context = context;
        //setup cursor paint
        mBackgroundPaint.setColor(Color.BLACK);
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setTextStyle(int textStyle) {
        this.textPaint.setTextStyle(textStyle);
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public void setFillPattern(int fillPattern) {
        this.fillPattern = fillPattern;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setTextDirection(int textDirection) {
        this.textPaint.setTextDirection(textDirection);
    }

    public void setTextSize(int textSize) {
        textPaint.setTextSize(textSize * TextPaint.DEF_TEXT_SIZE);
    }

    public int getBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
    }

    public int getWidth() {
        return mGraphBitmap.getWidth();
    }

    public void setWidth(int visibleWidth) {
        if (visibleWidth < 1) visibleWidth = 1;
        this.width = visibleWidth;
    }

    public int getHeight() {
        return mGraphBitmap.getHeight();
    }

    public void setHeight(int visibleHeight) {
        if (visibleHeight < 1) visibleHeight = 1;
        this.height = visibleHeight;
    }

    public void onSizeChange(int width, int height) {
        setWidth(width);
        setHeight(height);
        //set view port
        viewPort.set(0, 0, width, height);

        invalidateBitmap();
    }

    /**
     * resize the graph bitmap if size of screen change
     */
    private synchronized void invalidateBitmap() {
        synchronized (mLock) {
            if (mGraphBitmap == null) {
                mGraphBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            } else {
                mGraphBitmap = ImageUtils.getResizedBitmap(mGraphBitmap, width, height);
            }
        }
    }

    /**
     * @return the graph bitmap
     */
    public synchronized Bitmap getGraphBitmap() {
        synchronized (mLock) {
            return mGraphBitmap;
        }
    }

    public int getColorPixel(int x, int y) {
        return mGraphBitmap.getPixel(x, y);
    }

    public int getYCursor() {
        return mCursor.getY();
    }

    public int getXCursor() {
        return mCursor.getX();
    }

    public int getPaintColor() {
        return textPaint.getColor();
    }

    public void setPaintColor(int paintColor) {
        this.textPaint.setColor(paintColor);
    }

    public void closeGraph() {
//        graphObjects.clear(); //GC
//        invalidateBitmap();
    }

    public void clear() {
        mCursor.setCoordinate(0, 0);
        clearGraphBitmap();
    }

    /**
     * draw rect with background black instead of create new bitmap
     * It will be improve performance
     */
    private void clearGraphBitmap() {
        synchronized (mLock) {
            Canvas canvas = new Canvas(mGraphBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, width, height, paint);
        }
    }

    public void setCursorPostion(int x, int y) {
        this.mCursor.setCoordinate(x, y);
    }

    public ConsoleCursor getCursor() {
        return mCursor;
    }

    public void addGraphObject(GraphObject graphObject) {
        // TODO: 30-Mar-17
        graphObject.setFillStyle(context, fillPattern, fillColor);

        graphObject.setLineWidth(lineWidth);
        graphObject.setLineStyle(lineStyle);
        graphObject.setLineColor(textPaint.getColor());

//        graphObject.setTextDirection(textDirection);
//        graphObject.setTextStyle(textStyle);
//        graphObject.setTextFont(currentFont);
//        graphObject.setTextJustify(textJustify);

        graphObject.setTextPaint(textPaint.clonePaint());

        graphObject.draw(mGraphBitmap);
        this.lastObject = graphObject;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void getTextBound(String text, Rect store) {
        textPaint.getTextBounds(text, 0, text.length(), store);
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        viewPort.setViewPort(x1, y1, x2, y2, clip);
    }

    public void setPaintStyle(int style, int pattern, int width) {
        textPaint.setStrokeWidth(width);

    }

    public synchronized void setFontID(int fontID) {
        this.textPaint.setTextFontID(fontID);
        Typeface font;
        switch (fontID) {
            case TextFont.DefaultFont:
                font = Typeface.MONOSPACE;
                break;
            case TextFont.SansSerifFont:
                font = Typeface.SANS_SERIF;
                break;
            case TextFont.TriplexFont:
                font = FontManager.getFontFromAsset(context, "lcd_solid.ttf");
                break;
            case TextFont.EuroFont:
                font = FontManager.getFontFromAsset(context, "graph_euro.ttf");
                break;
            case TextFont.ScriptFont:
                font = FontManager.getFontFromAsset(context, "graph_script.ttf");
                break;
            case TextFont.BoldFont:
                font = Typeface.DEFAULT_BOLD;
                break;
            case TextFont.GothicFont:
                font = FontManager.getFontFromAsset(context, "gothic.ttf");
                break;
            default:
                font = Typeface.DEFAULT;
                break;
        }
        textPaint.setTextFont(font);
    }


    public TextJustify getTextJustify() {
        return textPaint.getTextJustify();
    }

    public void setTextJustify(TextJustify textJustify) {
        this.textPaint.setTextJustify(textJustify);
    }

    public void setLinePattern(int linePattern) {
        this.linePattern = linePattern;
    }

    public Bitmap getFillBitmap() {
        return FillType.createFillBitmap(context, fillPattern, fillColor);
    }

    public void gc() {
        if (mGraphBitmap != null && !mGraphBitmap.isRecycled()) {
            mGraphBitmap.recycle();
        }
    }
}
