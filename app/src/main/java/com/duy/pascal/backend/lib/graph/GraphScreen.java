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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.duy.pascal.backend.lib.graph.graphic_model.GraphObject;
import com.duy.pascal.backend.lib.graph.style.FillType;
import com.duy.pascal.backend.lib.graph.style.LineStyle;
import com.duy.pascal.backend.lib.graph.style.LineWidth;
import com.duy.pascal.backend.lib.graph.text_model.TextDirection;
import com.duy.pascal.backend.lib.graph.text_model.TextFont;
import com.duy.pascal.backend.lib.graph.text_model.TextJustify;
import com.duy.pascal.frontend.utils.FontManager;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;

import static android.R.attr.textSize;

/**
 * Created by Duy on 30-Mar-17.
 */

public class GraphScreen {
    private static final String TAG = GraphScreen.class.getSimpleName();
    private final Context context;
    protected int fillColor = -1;//white
    protected GraphObject lastObject;
    private int width = 1;
    private int height = 1;
    private ViewPort viewPort = new ViewPort(0, 0, width, height);
    //background
    private Paint mBackgroundPaint = new Paint();

    //cursor
    private Paint mForegroundPaint = new Paint();
    /**
     * this object used to drawBackground {@link GraphObject}
     */
    private Bitmap mGraphBitmap;
    private CursorConsole mCursor = new CursorConsole(0, 0, 0xffffffff);

    private int textStyle = TextFont.DefaultFont;
    private int textDirection = TextDirection.HORIZONTAL_DIR;
    private Typeface currentFont;
    private TextJustify textJustify = new TextJustify();

    private int lineWidth = LineWidth.NormWidth;
    private int lineStyle = LineStyle.SolidLn;
    private int linePattern;

    private int fillPattern = FillType.EmptyFill;
    private Paint fillPaint = new Paint();


    public GraphScreen(Context context) {
        this.context = context;
        //setup cursor paint
        mForegroundPaint.setColor(Color.WHITE);
        mForegroundPaint.setTextSize(16f);
        mForegroundPaint.setAntiAlias(true);
        mForegroundPaint.setTypeface(Typeface.MONOSPACE);

        mBackgroundPaint.setColor(Color.BLACK);

        fillPaint.setStyle(Paint.Style.FILL);
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getFillPattern() {
        return fillPattern;
    }

    public void setFillPattern(int fillPattern) {
        this.fillPattern = fillPattern;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(int textDirection) {
        this.textDirection = textDirection;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        mForegroundPaint.setTextSize(textSize);
    }

    public Paint getCursorPaint() {
        return mForegroundPaint;
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
        if (width < 1) width = 1;
        this.width = visibleWidth;
    }

    public int getHeight() {
        return mGraphBitmap.getHeight();
    }

    public void setHeight(int visibleHeight) {
        if (height < 1) height = 1;
        this.height = visibleHeight;
    }

    public void onSizeChange(int width, int height) {
        setWidth(width);
        setHeight(height);
        //set view port
        viewPort.set(0, 0, width, height);

        invalidateBitmap();
    }

    private void invalidateBitmap() {
        mGraphBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(mGraphBitmap);
//        for (GraphObject object : graphObjects) {
//            object.draw(canvas);
//        }
    }

    public synchronized Bitmap getGraphBitmap() {
        return mGraphBitmap;
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
        return mForegroundPaint.getColor();
    }

    public void setPaintColor(int paintColor) {
        this.mForegroundPaint.setColor(paintColor);
    }

    public void closeGraph() {
//        graphObjects.clear(); //GC
//        invalidateBitmap();
    }

    public void clear() {
//        graphObjects.clear();
        mCursor.setCoordinate(0, 0);
        invalidateBitmap();
    }

    public void setCursorPostion(int x, int y) {
        this.mCursor.setCoordinate(x, y);
    }

    public CursorConsole getCursor() {
        return mCursor;
    }

    public void addGraphObject(GraphObject graphObject) {
        // TODO: 30-Mar-17
        graphObject.setBackgroundColor(mBackgroundPaint.getColor());

        graphObject.setFillStyle(context, fillPattern, fillColor);

        graphObject.setLineWidth(lineWidth);
        graphObject.setLineStyle(lineStyle);
        graphObject.setLineColor(mForegroundPaint.getColor());

        graphObject.setTextDirection(textDirection);
        graphObject.setTextStyle(textStyle);
        graphObject.setTextFont(currentFont);
        graphObject.setTextJustify(textJustify);

        //end

        //save to list
//        graphObjects.add(graphObject);

        //add to screen
        graphObject.draw(mGraphBitmap);
        this.lastObject = graphObject;
    }

    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    public void getTextBound(String text, Rect store) {
        mForegroundPaint.getTextBounds(text, 0, text.length(), store);
    }

    public Paint getTextPaint() {
        return mForegroundPaint;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        viewPort.setViewPort(x1, y1, x2, y2, clip);
    }

    public void setPaintStyle(int style, int pattern, int width) {
        mForegroundPaint.setStrokeWidth(width);

    }

    public void setFont(int font) {
        this.textStyle = font;
        switch (font) {
            case TextFont.DefaultFont:
                currentFont = Typeface.MONOSPACE;
                break;
            case TextFont.SansSerifFont:
                currentFont = Typeface.SANS_SERIF;
                break;
            case TextFont.TriplexFont:
                currentFont = FontManager.getFontFromAsset(context, "triple.ttf");
                break;
            case TextFont.EuroFont:
                currentFont = FontManager.getFontFromAsset(context, "graph_euro.ttf");
                break;
            case TextFont.ScriptFont:
                currentFont = FontManager.getFontFromAsset(context, "graph_script.ttf");
                break;
            case TextFont.BoldFont:
                currentFont = Typeface.DEFAULT_BOLD;
                break;
            case TextFont.GothicFont:
                currentFont = FontManager.getFontFromAsset(context, "gothic.ttf");
                break;
            default:
                currentFont = Typeface.DEFAULT;
                break;
        }

    }


    public TextJustify getTextJustify() {
        return textJustify;
    }

    public void setTextJustify(TextJustify textJustify) {
        this.textJustify = textJustify;
    }

//    public Paint getFillPaint() {
//        return FillType.createPaintFill(context, fillPattern, fillColor);
//    }

    public void setLinePattern(int linePattern) {
        this.linePattern = linePattern;
    }

    public Paint getFillPaint() {
        return FillType.createPaintFill(context, fillPattern, fillColor);
    }

    public Bitmap getFillBitmap() {
        return FillType.createFillBitmap(context, fillPattern, fillColor);
    }
}
