package com.duy.pascal.frontend.view.exec_screen.console;

import android.graphics.Canvas;
import android.graphics.Color;

import com.duy.pascal.frontend.view.exec_screen.ScreenObject;

/**
 * Created by Duy on 04-Apr-17.
 */

public class TextConsole implements ScreenObject {
    public String text = "";
    public int textColor = Color.WHITE; //white
    public int textBackground = Color.BLACK;//back
    public int alpha = 255;

    public TextConsole(String text, int textBackground) {
        this.text = text;
        this.textBackground = textBackground;
    }

    public TextConsole(String text, int textBackground, int textColor) {
        this.text = text;
        this.textBackground = textBackground;
        this.textColor = textColor;
    }

    public TextConsole() {
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public String getSingleString() {
        return text.substring(0, 1);
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

    }

    public void setText(String text) {
        this.text = text;
    }
}
