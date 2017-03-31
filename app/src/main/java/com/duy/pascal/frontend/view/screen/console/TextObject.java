package com.duy.pascal.frontend.view.screen.console;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.duy.pascal.frontend.view.screen.ScreenObject;

/**
 * Created by Duy on 26-Mar-17.
 */

public class TextObject implements ScreenObject {
    public  Paint paint = new Paint();

    public char[] text;
    public int x, y;
    public int textColor = 0xfffffff; //white
    public int textBackground = 0xff00000;//back

    public TextObject(char[] text, int x, int y, int textColor, int textBackground) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.textColor = textColor;
        this.textBackground = textBackground;
    }

    public TextObject(char[] text, int x, int y, TextRenderer textRenderer) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.textColor = textRenderer.getTextColor();
        this.textBackground = textRenderer.getBackgroundColor();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextBackground() {
        return textBackground;
    }

    public void setTextBackground(int textBackground) {
        this.textBackground = textBackground;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, 0, 1, x, y, paint);
    }
}
