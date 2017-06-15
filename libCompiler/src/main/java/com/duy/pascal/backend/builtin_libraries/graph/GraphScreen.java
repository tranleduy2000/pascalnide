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

package com.duy.pascal.backend.builtin_libraries.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.builtin_libraries.graph.model.GraphObject;
import com.duy.pascal.backend.builtin_libraries.graph.paint.FillPaint;
import com.duy.pascal.backend.builtin_libraries.graph.paint.LinePaint;
import com.duy.pascal.backend.builtin_libraries.graph.paint.TextPaint;
import com.duy.pascal.backend.builtin_libraries.graph.style.FillType;
import com.duy.pascal.backend.builtin_libraries.graph.style.LineStyle;
import com.duy.pascal.backend.builtin_libraries.graph.style.LineWidth;
import com.duy.pascal.backend.builtin_libraries.graph.style.TextFont;
import com.duy.pascal.backend.builtin_libraries.graph.style.TextJustify;
import com.duy.pascal.frontend.theme.util.FontManager;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleCursor;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleView;

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

    private Paint mBackgroundPaint = new Paint();
    private TextPaint textPaint = new TextPaint();
    private LinePaint linePaint = new LinePaint();
    private FillPaint fillPaint = new FillPaint();

    /**
     * this object used to draw {@link GraphObject}
     */
    private Bitmap mPrimaryBitmap, mBitmapBuffer;
    private ConsoleCursor mCursor = new ConsoleCursor(0, 0, 0xffffffff);
    private int lineWidth = LineWidth.NormWidth;
    private int lineStyle = LineStyle.SolidLn;
    private int linePattern;

    private volatile boolean bufferEnable = false;
    private boolean antiAlias;
    private ConsoleView mConsoleView;

    public GraphScreen(Context context, ConsoleView consoleView) {
        this.context = context;
        this.mConsoleView = consoleView;
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
        return mPrimaryBitmap.getWidth();
    }

    public void setWidth(int visibleWidth) {
        if (visibleWidth < 1) visibleWidth = 1;
        this.width = visibleWidth;
    }

    public int getHeight() {
        return mPrimaryBitmap.getHeight();
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
            if (mPrimaryBitmap == null) {
                mPrimaryBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            } else {
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(mPrimaryBitmap, 0, 0, textPaint);
                synchronized (mLock) {
                    mPrimaryBitmap.recycle();
                    mPrimaryBitmap = bitmap;
                }
            }
            if (bufferEnable) {
                if (mBitmapBuffer == null) {
                    mBitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                } else {
                    Bitmap buffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvasBuffer = new Canvas(buffer);
                    canvasBuffer.drawBitmap(mBitmapBuffer, 0, 0, textPaint);
                    synchronized (mLock) {
                        mBitmapBuffer.recycle();
                        mBitmapBuffer = buffer;
                    }
                }
            }
        }
    }

    /**
     * @return the graph bitmap
     */
    @Nullable
    public Bitmap getGraphBitmap() {
        return mPrimaryBitmap;
    }

    public int getColorPixel(int x, int y) {
        return mPrimaryBitmap.getPixel(x, y);
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
    }

    public void clear() {
        mCursor.setCoordinate(0, 0);
        clearPrimaryBitmap();
    }

    /**
     * draw rect with background black instead of create new bitmap
     * It will be improve performance
     */
    private void clearPrimaryBitmap() {
        synchronized (mLock) {
         /*   Canvas canvas = null;
            if (mPrimaryBitmap != null) {
                canvas = new Canvas(mPrimaryBitmap);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                canvas.drawRect(0, 0, mPrimaryBitmap.getWidth(), mPrimaryBitmap.getHeight(), paint);
            }*/
            if (ensurePrimaryNonNull()) {
                mPrimaryBitmap.eraseColor(Color.TRANSPARENT);
            }
        }
    }

    public void setCursorPostion(int x, int y) {
        this.mCursor.setCoordinate(x, y);
    }

    public ConsoleCursor getCursor() {
        return mCursor;
    }

    public void addGraphObject(GraphObject graphObject) {
        graphObject.setFillStyle(context, fillPattern, fillColor);

        graphObject.setLineWidth(lineWidth);
        graphObject.setLineStyle(lineStyle);
        graphObject.setLineColor(textPaint.getColor());
        graphObject.setTextPaint(textPaint.clonePaint());
        graphObject.setAntiAlias(antiAlias);

        this.lastObject = graphObject;

        if (isBufferEnable()) {
            graphObject.draw(mBitmapBuffer);
        } else {
            graphObject.draw(mPrimaryBitmap);
            mConsoleView.postInvalidate();
        }
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
                font = FontManager.getFontFromAsset(context, "graph/lcd_solid.ttf");
                break;
            case TextFont.EuroFont:
                font = FontManager.getFontFromAsset(context, "graph/graph_euro.ttf");
                break;
            case TextFont.BoldFont:
                font = Typeface.DEFAULT_BOLD;
                break;
            case TextFont.GothicFont:
                font = FontManager.getFontFromAsset(context, "graph/gothic.ttf");
                break;
            default:
                font = Typeface.MONOSPACE;
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

    public synchronized void clearData() {
        if (mPrimaryBitmap != null && !mPrimaryBitmap.isRecycled()) {
            mPrimaryBitmap.recycle();
        }
        if (mBitmapBuffer != null && !mBitmapBuffer.isRecycled()) {
            mBitmapBuffer.recycle();
        }
    }

    public boolean isBufferEnable() {
        return bufferEnable;
    }

    public void setBufferEnable(boolean bufferEnable) {
        this.bufferEnable = bufferEnable;
        if (bufferEnable && ensureBufferNonNull()) {
            clearBufferBitmap();
        }
    }

    private boolean ensureBufferNonNull() {
        if (mBitmapBuffer == null) {
            mBitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            return false;
        }
        return true;
    }

    private boolean ensurePrimaryNonNull() {
        if (mPrimaryBitmap == null) {
            mPrimaryBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            return false;
        }
        return true;
    }

    public void bufferToPrimary() {
        if (mBitmapBuffer != null) {
//            ensurePrimaryNonNull();
//            Canvas canvas = new Canvas(mPrimaryBitmap);
//            canvas.drawBitmap(mBitmapBuffer, 0, 0, null);
//            this.mPrimaryBitmap = mBitmapBuffer.copy(mBitmapBuffer.getConfig(), true);
            this.mPrimaryBitmap = mBitmapBuffer.copy(mBitmapBuffer.getConfig(), true);
        }
    }

    public synchronized void clearBufferBitmap() {
        synchronized (mLock) {
         /*   Canvas canvas = null;
            if (mBitmapBuffer != null) {
                canvas = new Canvas(mBitmapBuffer);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                canvas.drawRect(0, 0, mBitmapBuffer.getWidth(), mBitmapBuffer.getHeight(), paint);
            }*/
            if (mBitmapBuffer != null) mBitmapBuffer.eraseColor(Color.TRANSPARENT);
        }
    }

    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }
}
