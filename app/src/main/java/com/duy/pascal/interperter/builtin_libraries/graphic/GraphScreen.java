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

package com.duy.pascal.interperter.builtin_libraries.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.builtin_libraries.graphic.model.GraphObject;
import com.duy.pascal.interperter.builtin_libraries.graphic.paint.FillPaint;
import com.duy.pascal.interperter.builtin_libraries.graphic.paint.LinePaint;
import com.duy.pascal.interperter.builtin_libraries.graphic.paint.TextPaint;
import com.duy.pascal.interperter.builtin_libraries.graphic.style.FillType;
import com.duy.pascal.interperter.builtin_libraries.graphic.style.LineStyle;
import com.duy.pascal.interperter.builtin_libraries.graphic.style.LineWidth;
import com.duy.pascal.interperter.builtin_libraries.graphic.style.TextJustify;
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

    private TextPaint mTextPaint = new TextPaint();
    private LinePaint mLinePaint = new LinePaint();
    private FillPaint mFillPaint = new FillPaint();

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
        mBackgroundPaint.setAlpha(255);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAlpha(255);
        mLinePaint.setAlpha(255);
        mTextPaint.setAlpha(255);
    }

    public void setTextStyle(int textStyle) {
        this.mTextPaint.setTextStyle(textStyle);
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
        this.mTextPaint.setTextDirection(textDirection);
    }

    public void setTextSize(int textSize) {
        mTextPaint.setTextSize(textSize * TextPaint.DEF_TEXT_SIZE);
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
                mPrimaryBitmap.eraseColor(Color.BLACK);
            } else {
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(mPrimaryBitmap, 0, 0, mTextPaint);
                synchronized (mLock) {
                    mPrimaryBitmap.recycle();
                    mPrimaryBitmap = bitmap;
                }
            }
            if (bufferEnable) {
                if (mBitmapBuffer == null) {
                    mBitmapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    mBitmapBuffer.eraseColor(Color.BLACK);
                } else {
                    Bitmap buffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    Canvas canvasBuffer = new Canvas(buffer);
                    canvasBuffer.drawBitmap(mBitmapBuffer, 0, 0, mTextPaint);
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
        return mTextPaint.getColor();
    }

    public void setPaintColor(int paintColor) {
        this.mTextPaint.setColor(paintColor);
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
            if (ensurePrimaryNonNull()) {
                mPrimaryBitmap.eraseColor(Color.BLACK);
            }
        }
    }

    public void setCursorPosition(int x, int y) {
        this.mCursor.setCoordinate(x, y);
    }

    public ConsoleCursor getCursor() {
        return mCursor;
    }

    public void addGraphObject(GraphObject graphObject) {
        graphObject.setFillStyle(context, fillPattern, fillColor);

        graphObject.setLineWidth(lineWidth);
        graphObject.setLineStyle(lineStyle);
        graphObject.setLineColor(mTextPaint.getColor());
        graphObject.setTextPaint(mTextPaint.clonePaint());
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
        mTextPaint.getTextBounds(text, 0, text.length(), store);
    }

    public TextPaint getTextPaint() {
        return mTextPaint;
    }

    /**
     * Set the current graphic viewport to the retangle define by then top-left (x1, y1) and then
     * bottom-right (x2, y2). If clip
     */
    public void setViewPort(int x1, int y1, int x2, int y2, boolean clip) {
        viewPort.setViewPort(x1, y1, x2, y2, clip);
    }

    public void setPaintStyle(int style, int pattern, int width) {
        mTextPaint.setStrokeWidth(width);

    }

    public synchronized void setFontID(int fontID) {
        this.mTextPaint.setTextFontID(context, fontID);
    }


    public TextJustify getTextJustify() {
        return mTextPaint.getTextJustify();
    }

    public void setTextJustify(TextJustify textJustify) {
        this.mTextPaint.setTextJustify(textJustify);
    }

    public void setLinePattern(int linePattern) {
        this.linePattern = linePattern;
    }

    public Bitmap getFillBitmap(Bitmap.Config rgb565) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = rgb565;
        return FillType.createFillBitmap(context, fillPattern, fillColor, options);
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
            this.mPrimaryBitmap = mBitmapBuffer.copy(mBitmapBuffer.getConfig(), true);
        }
    }

    public synchronized void clearBufferBitmap() {
        synchronized (mLock) {
            if (mBitmapBuffer != null) mBitmapBuffer.eraseColor(Color.BLACK);
        }
    }

    public void setAntiAlias(boolean antiAlias) {
        this.antiAlias = antiAlias;
    }
}
