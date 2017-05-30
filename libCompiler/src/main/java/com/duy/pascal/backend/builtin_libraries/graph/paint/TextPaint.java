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

package com.duy.pascal.backend.builtin_libraries.graph.paint;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;

import com.duy.pascal.backend.builtin_libraries.graph.style.TextDirection;
import com.duy.pascal.backend.builtin_libraries.graph.style.TextFont;
import com.duy.pascal.backend.builtin_libraries.graph.style.TextJustify;

/**
 * Created by Duy on 20-Apr-17.
 */

public class TextPaint extends Paint implements Cloneable {
    //best size
    public static final float DEF_TEXT_SIZE = 12f;

    private int textStyle;
    private int textDirection;
    private TextJustify textJustify;
    private int textFontID;

    public TextPaint() {
        textDirection = TextDirection.HORIZONTAL_DIR;
        textStyle = TextFont.DefaultFont;
        textJustify = new TextJustify();
        setTextSize(DEF_TEXT_SIZE);
        setColor(Color.WHITE);
        setAntiAlias(true);
        setTypeface(Typeface.MONOSPACE);
    }

    public static float getDefTextSize() {
        return DEF_TEXT_SIZE;
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

    public TextJustify getTextJustify() {
        return textJustify;
    }

    public void setTextJustify(TextJustify textJustify) {
        this.textJustify = textJustify;
        switch (this.getHorizontal()) {
            case TextJustify.HORIZONTAL_STYLE.CenterText:
                this.setTextAlign(Align.CENTER);
                break;
            case TextJustify.HORIZONTAL_STYLE.LeftText:
                this.setTextAlign(Align.LEFT);
                break;
            case TextJustify.HORIZONTAL_STYLE.RightText:
                this.setTextAlign(Align.RIGHT);
                break;
            default:
                this.setTextAlign(Align.LEFT);
                break;
        }
    }

    public int getTextFontID() {
        return textFontID;
    }

    public void setTextFontID(int textFontID) {
        this.textFontID = textFontID;
    }

    @Override
    public void setTextSize(float textSize) {
        super.setTextSize(textSize);
    }

    public void setTextFont(Typeface textFont) {
        setTypeface(textFont);
    }

    public TextPaint clonePaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(getTextSize());
        textPaint.setTextDirection(textDirection);
        textPaint.setColor(getColor());
        textPaint.setStrokeWidth(getStrokeWidth());
        textPaint.setTextStyle(textStyle);
        textPaint.setTypeface(getTypeface());
        textPaint.setTextJustify(textJustify.clone());
        return textPaint;
    }

    @Override
    public void setColor(@ColorInt int color) {
        super.setColor(color);
    }

    public int getHorizontal() {
        return textJustify.getHorizontal();
    }

    public int getVertical() {
        return textJustify.getVertical();
    }

    public int getRealY(String text, float y) {
        Rect bound = new Rect();
        getTextBounds(text, 0, text.length(), bound);
        return (int) (y + bound.height());
    }
}
