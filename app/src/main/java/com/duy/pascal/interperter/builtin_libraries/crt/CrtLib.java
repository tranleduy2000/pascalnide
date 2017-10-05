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

package com.duy.pascal.interperter.builtin_libraries.crt;

import android.graphics.Color;

import com.duy.pascal.ui.runnable.ConsoleHandler;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleCursor;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleScreen;
import com.duy.pascal.ui.view.exec_screen.console.ConsoleView;
import com.duy.pascal.ui.view.exec_screen.console.TextRenderer;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.exceptions.runtime.WrongArgsException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * CRT - Turbo Pascal screen and keyboard handling unit
 * See https://www.freepascal.org/docs-html/rtl/crt/index.html
 * <p>
 * Created by Duy on 28-Feb-17.
 */
public class CrtLib extends PascalLibrary {
    public static final String TAG = CrtLib.class.getSimpleName();
    public static final String NAME = "crt";


    private ConsoleHandler handler;
    private AtomicBoolean canPlaySound = new AtomicBoolean(false);
    private long finalFrequency;
    private Runnable soundRunnable = new Runnable() {
        @Override
        public void run() {
            while (canPlaySound.get()) {
                AndroidToneGeneratorLib.playSound(finalFrequency, 44100);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Thread soundThread;

    /**
     * constructor call by {@link ClassLoader} in {@link com.duy.pascal.interperter.core.PascalCompiler}
     */
    public CrtLib(ConsoleHandler handler) {
        this.handler = handler;
    }

    public CrtLib() {

    }


    /**
     * goto x, y procedure
     * <p>
     *
     * @param x - x coordinate of screen
     * @param y - y coordinate of screen
     */
    @PascalMethod(description = "crt library")
    public void gotoXY(int x, int y) {
        if (handler == null) return;
        handler.getConsoleView().moveCursorTo(x, y);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    @PascalMethod(description = "stop")
    public void shutdown() {
        noSound();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {
        Map<Name, ConstantDefinition> constants = parentContext.getConstants();
        ConstantDefinition colorConst;
        colorConst = new ConstantDefinition("Black", 0, new LineInfo(-1, "black = 0"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Blue", 1, new LineInfo(-1, "Blue = 1"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Green", 2, new LineInfo(-1, "Green = 2"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Cyan", 3, new LineInfo(-1, "Cyan = 3"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Red", 4, new LineInfo(-1, "Red = 4"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Magenta", 5, new LineInfo(-1, "Magenta = 5"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Brown", 6, new LineInfo(-1, "Brown = 6"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightGray", 7, new LineInfo(-1, "LightGray  = 7"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("DarkGray", 8, new LineInfo(-1, "DarkGray = 8"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightBlue", 9, new LineInfo(-1, "LightBlue = 9"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightGreen", 10, new LineInfo(-1, "LightGreen = 10"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightCyan", 11, new LineInfo(-1, "LightCyan = 11"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightRed", 12, new LineInfo(-1, "LightRed = 12"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("LightMagenta", 13, new LineInfo(-1, "LightMagenta = 13"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("Yellow", 14, new LineInfo(-1, " Yellow = 14"));
        constants.put(colorConst.getName(), colorConst);
        colorConst = new ConstantDefinition("White", 15, new LineInfo(-1, "White = 15"));
        constants.put(colorConst.getName(), colorConst);

        constants.put(Name.create("pi"), new ConstantDefinition("Pi", Math.PI));
        constants.put(Name.create("bw40"), new ConstantDefinition("bw40", 0));
        constants.put(Name.create("bw80"), new ConstantDefinition("bw80", 2));
        constants.put(Name.create("co40"), new ConstantDefinition("co40", 1));
        constants.put(Name.create("co80"), new ConstantDefinition("co80", 3));
        constants.put(Name.create("c80"), new ConstantDefinition("c80", 3));
        constants.put(Name.create("mono"), new ConstantDefinition("mono", 7));
    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    /**
     * clear screen
     */
    @PascalMethod(description = "crt library")
    public void clrscr() {
        if (handler == null) return;
        handler.clearConsole();
    }

    /**
     * set text linePaint color
     */
    @PascalMethod(description = "crt library")
    public void textColor(int code) {
        if (handler == null) return;
        int color = ColorUtils.pascalColorToAndroidColor(code);
        handler.getConsoleView().getTextRenderer().setTextColor(color);
    }

    /**
     * set background console
     */
    @PascalMethod(description = "crt library")
    public void textBackground(int code) {
        if (handler == null) return;
        int color = ColorUtils.pascalColorToAndroidColor(code);
        handler.getConsoleView().getTextRenderer().setTextBackgroundColor(color);
    }

    @PascalMethod(description = "Return X (horizontal) cursor position")
    public int whereX() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereX();
    }

    @PascalMethod(description = "Return Y (vertical) cursor position")
    public int whereY() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereY();
    }

    private void assertActivityNonNull() {
        if (handler == null) throw new RuntimeException("Can not define screen");
    }

    @PascalMethod(description = "Return to normal (startup) modes")
    public void NormVideo() {
        assertActivityNonNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.NORMAL_TEXT_ALPHA);
    }

    @PascalMethod(description = "Switch to highlighted text mode")
    public void HighVideo() {
        assertActivityNonNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.HIGH_TEXT_ALPHA);
    }

    @PascalMethod(description = "Switch to low intensity colors")
    public void LowVideo() {
        assertActivityNonNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.LOW_TEXT_ALPHA);
    }

    @PascalMethod(description = "Show big cursor")
    public void cursorBig() {
        assertActivityNonNull();
        handler.getConsoleView().getCursorConsole().setMode(ConsoleCursor.BIG_CURSOR);
    }

    @PascalMethod(description = "Hide cursor")
    public void cursorOff() {
        assertActivityNonNull();
        handler.getConsoleView().getCursorConsole().setVisible(false);
    }

    @PascalMethod(description = "Display cursor")
    public void cursorOn() {
        assertActivityNonNull();
        handler.getConsoleView().getCursorConsole().setVisible(true);
    }

    @PascalMethod(description = "Sound system speaker")
    public void sound(Long frequency) throws WrongArgsException {
        if (frequency != null) {
            this.finalFrequency = frequency;
        } else {
            throw new WrongArgsException("method sound");
        }
        if (soundThread == null) {
            soundThread = new Thread(soundRunnable);
            canPlaySound.set(true);
            soundThread.start();
        }
    }

    @PascalMethod(description = "Sound system speaker")
    public void sound(Integer frequency) throws WrongArgsException {
        this.sound(Long.valueOf(frequency));
    }

    @PascalMethod(description = "Stop system speaker")
    public void noSound() {
        canPlaySound.set(false);
    }

    @PascalMethod(description = "Delay program execution.")
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    @PascalMethod(description = "Set screen mode.")
    public void textMode(int mode) {
        // TODO: 24-May-17
    }

    @PascalMethod(description = "Current screen width")
    public int ScreenWidth() {
        ConsoleView consoleView = handler.getConsoleView();
        ConsoleScreen consoleScreen = null;
        if (consoleView != null) {
            consoleScreen = consoleView.getConsoleScreen();
            return consoleScreen.getConsoleColumn();
        }
        return 0;
    }

    @PascalMethod(description = "Current screen height.")
    public int ScreenHeight() {
        ConsoleView consoleView = handler.getConsoleView();
        ConsoleScreen consoleScreen = null;
        if (consoleView != null) {
            consoleScreen = consoleView.getConsoleScreen();
            return consoleScreen.getConsoleRow();
        }
        return 0;
    }

    @PascalMethod(description = "Return a color-int from red, green, blue components.\n" +
            "The alpha component is implicity 255 (fully opaque).\n" +
            "These component values should be [0..255], but there is no\n" +
            "range check performed, so if they are out of range, the\n" +
            "returned color is undefined.")
    public int rgb(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }

    @PascalMethod(description = "Return a color-int from alpha, red, green, blue components.\n" +
            "These component values should be [0..255], but there is no\n" +
            "range check performed, so if they are out of range, the\n" +
            "returned color is undefined.")
    public int argb(int a, int r, int g, int b) {
        return Color.argb(a, r, g, b);
    }
}
