/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.frontend.view.exec_screen.console;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.duy.pascal.frontend.view.exec_screen.ScreenObject;


/**
 * Text renderer interface
 */

public class TextRenderer implements ScreenObject {
    /**
     * mode text, low-high-normal
     */
    public static final int NORMAL_TEXT_ALPHA = 200;
    public static final int LOW_TEXT_ALPHA = 150;
    public static final int HIGH_TEXT_ALPHA = 255;
    private static final String TAG = "TextRenderer";
    /**
     * character attributes
     */
    private int charHeight;
    private int charAscent;
    private int charDescent;
    private float charWidth;
    private int textMode = 0;
    private Typeface typeface = Typeface.MONOSPACE;
    private Paint mTextPaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private int textColor = Color.WHITE;
    private int textBackgroundColor = Color.BLACK;
    private int alpha = NORMAL_TEXT_ALPHA;
    private int textWidth = 2;
    private boolean fixedWidthFont;

    public TextRenderer(float textSize) {
        init(textSize);
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int color) {
        this.textBackgroundColor = color;
    }

    private void init(float textSize) {
        mTextPaint.setTypeface(typeface);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);

        charHeight = (int) Math.ceil(mTextPaint.getFontSpacing());
        charAscent = (int) Math.ceil(mTextPaint.ascent());
        charDescent = charHeight + charAscent;
        charWidth = mTextPaint.measureText("M");
        if (charWidth != mTextPaint.measureText(".")) {
            this.fixedWidthFont = false;
        } else {
            this.fixedWidthFont = true;
        }
    }


    /**
     * draw text
     *
     * @param x     - start x
     * @param y     - start y
     * @param text  - input text
     * @param start - start index of array text[]
     */
    public void draw(Canvas canvas, int x, int y, char[] text, int start, int count) {
        canvas.drawText(text, start, count, x, y, mTextPaint);
    }

    public void draw(Canvas canvas, int x, int y, String text, int start, int count) {
        canvas.drawText(text, start, start + count, x, y, mTextPaint);
    }

    public void draw(Canvas canvas, float x, float y, TextConsole[] text, int start, int count) {
        float y1 = y + charAscent;
        for (int i = start; i < start + count; i++) {
            if (!fixedWidthFont) charWidth = mTextPaint.measureText(text[i].getSingleString());

            backgroundPaint.setColor(text[i].getTextBackground());
            canvas.drawRect(x, y1, x + charWidth, y1, backgroundPaint);

            mTextPaint.setColor(text[i].getTextColor());
            mTextPaint.setAlpha(text[i].getAlpha());
            canvas.drawText(text[i].getSingleString(), x, y, mTextPaint);

            x += charWidth;
        }
    }

    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        mTextPaint.setTextSize(textSize);
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        mTextPaint.setTypeface(typeface);
    }

    public int getCharHeight() {
        return charHeight;
    }

    public void setCharHeight(int charHeight) {
        this.charHeight = charHeight;
    }

    public int getCharAscent() {
        return charAscent;
    }

    public void setCharAscent(int charAscent) {
        this.charAscent = charAscent;
    }

    public int getCharDescent() {
        return charDescent;
    }

    public void setCharDescent(int charDescent) {
        this.charDescent = charDescent;
    }

    public float getCharWidth() {
        return charWidth;
    }

    public void setCharWidth(int charWidth) {
        this.charWidth = charWidth;
    }


    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextMode() {
        return textMode;
    }

    public void setTextMode(int textMode) {
        this.textMode = textMode;
    }

    void setReverseVideo(boolean reverseVideo) {

    }

    float getCharacterWidth() {
        return 0;

    }

    int getCharacterHeight() {
        return 0;

    }

    @Override
    public void draw(Canvas canvas) {

    }

    public int getBackgroundColor() {
        return textBackgroundColor;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
