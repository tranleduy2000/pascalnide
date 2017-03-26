/*
 * Copyright (C) 2012 Steven Luo
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

import android.graphics.Color;

public class ConsoleColorScheme {
    private int foreColor = Color.BLACK;
    private int backColor = Color.BLACK;
    private int cursorColor = Color.DKGRAY;
    private int xCursor = 0;
    private int yCursor = 0;

    public ConsoleColorScheme(int foreColor, int backColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
    }

    /**
     * Creates a <code>ColorScheme</code> object.
     *
     * @param foreColor       The foreground color as an ARGB hex value.
     * @param backColor       The background color as an ARGB hex value.
     * @param cursorColor The cursor foreground color as an ARGB hex value.
     */
    public ConsoleColorScheme(int foreColor, int backColor,  int cursorColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
        this.cursorColor = cursorColor;
    }

    private static int distance(int a, int b) {
        return channelDistance(a, b, 0) * 3 + channelDistance(a, b, 1) * 5
                + channelDistance(a, b, 2);
    }

    private static int channelDistance(int a, int b, int channel) {
        return Math.abs(getChannel(a, channel) - getChannel(b, channel));
    }

    private static int getChannel(int color, int channel) {
        return 0xff & (color >> ((2 - channel) * 8));
    }

    public int getxCursor() {
        return xCursor;
    }

    public void setxCursor(int xCursor) {
        this.xCursor = xCursor;
    }

    public int getyCursor() {
        return yCursor;
    }

    public void setYCursor(int yCursor) {
        this.yCursor = yCursor;
    }

    /**
     * @return This <code>ColorScheme</code>'s foreground color as an ARGB
     * hex value.
     */
    public int getForeColor() {
        return foreColor;
    }

    public void setForeColor(int foreColor) {
        this.foreColor = foreColor;
    }

    /**
     * @return This <code>ColorScheme</code>'s background color as an ARGB
     * hex value.
     */
    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(int backColor) {
        this.backColor = backColor;
    }



    public int getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }
}
