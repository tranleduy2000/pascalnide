/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.duy.pascal.frontend.view.screen.ScreenObject;

/**
 * Text renderer interface
 */

public class TextRenderer implements ScreenObject {
    public static final int MODE_OFF = 0;
    public static final int MODE_ON = 1;
    public static final int MODE_LOCKED = 2;
    public static final int MODE_MASK = 3;

    public static final int MODE_SHIFT_SHIFT = 0;
    public static final int MODE_ALT_SHIFT = 2;
    public static final int MODE_CTRL_SHIFT = 4;
    public static final int MODE_FN_SHIFT = 6;

    public int charHeight;
    public int charAscent;
    public int charDescent;
    public int charWidth;

    private Paint textPaint = new Paint();
    private int textMode = MODE_ON;
    private Typeface typeface = Typeface.MONOSPACE;

    public TextRenderer(float textSize) {
        init(textSize);
    }

    public float getTextSize() {
        return textPaint.getTextSize();
    }

    public void setTextSize(float textSize) {
        textPaint.setTextSize(textSize);
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        textPaint.setTypeface(typeface);
    }

    private void init(float textSize) {
        textPaint.setTypeface(typeface);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        charHeight = (int) Math.ceil(textPaint.getFontSpacing());
        charAscent = (int) Math.ceil(textPaint.ascent());
        charDescent = charHeight + charAscent;
        charWidth = (int) textPaint.measureText(new char[]{'M'}, 0, 1);
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

    public int getCharWidth() {
        return charWidth;
    }

    public void setCharWidth(int charWidth) {
        this.charWidth = charWidth;
    }

    public Paint getTextPaint() {
        return textPaint;
    }

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    public int getTextColor() {
        return textPaint.getColor();
    }

    public void setTextColor(int textColor) {
        this.textPaint.setColor(textColor);
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

    /**
     * @return pixels above top row of text to avoid looking cramped.
     */
    int getTopMargin() {

        return 0;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    public void draw(Canvas canvas, int x, int y, char[] text, int start, int count) {
        canvas.drawText(text, start, count, x, y, textPaint);
    }
}
