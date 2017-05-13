/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http,//www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.lib;

import com.duy.pascal.backend.lib.android.media.AndroidToneGeneratorLib;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.view.exec_screen.console.ConsoleCursor;
import com.duy.pascal.frontend.view.exec_screen.console.TextRenderer;
import com.js.interpreter.runtime.exception.WrongArgsException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Duy on 28-Feb-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class CrtLib implements PascalLibrary {
    public static final String TAG = CrtLib.class.getSimpleName();
    public static final String NAME = "crt";


    private ExecHandler handler;
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
     * constructor call by {@link ClassLoader} in {@link com.duy.pascal.backend.core.PascalCompiler}
     */
    public CrtLib(ExecHandler handler) {
        this.handler = handler;

    }


    /**
     * goto x, y procedure
     * <p>
     *
     * @param x - x coordinate of screen
     * @param y - y coordinate of screen
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void gotoXY(int x, int y) {
        if (handler == null) return;
        handler.getConsoleView().gotoXY(x, y);
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

    /**
     * clear screen
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void clrscr() {
        if (handler == null) return;
        handler.getConsoleView().clearScreen();
    }

    /**
     * set text linePaint color
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void textColor(int code) {
        if (handler == null) return;
        int color = ColorUtils.pascalColorToAndroidColor(code);
        handler.getConsoleView().setConsoleTextColor(color);
    }

    /**
     * set background console
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void textBackground(int code) {
        if (handler == null) return;
        int color = ColorUtils.pascalColorToAndroidColor(code);
        handler.getConsoleView().setConsoleTextBackground(color);
    }

    @PascalMethod(description = "crt library", returns = "void")
    public int whereX() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereX();
    }

    @PascalMethod(description = "crt library", returns = "void")
    public int whereY() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereY();
    }

    private void assertActivityNotNull() {
        if (handler == null) throw new RuntimeException("Can not define screen");
    }

    @PascalMethod(description = "crt library", returns = "void")
    public void NormVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.NORMAL_TEXT_ALPHA);
    }

    @PascalMethod(description = "crt library", returns = "void")
    public void HighVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.HIGH_TEXT_ALPHA);
    }

    @PascalMethod(description = "crt library", returns = "void")
    public void LowVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.LOW_TEXT_ALPHA);
    }

    /**
     * Show big cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void cursorBig() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setMode(ConsoleCursor.BIG_CURSOR);
    }

    /**
     * Hide cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void cursorOff() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setVisible(false);
    }

    /**
     * Display cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    public void cursorOn() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setVisible(true);
    }

    @PascalMethod(description = "crt library", returns = "void")
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

    @PascalMethod(description = "crt library", returns = "void")
    public void sound(Integer frequency) throws WrongArgsException {
        this.sound(Long.valueOf(frequency));
    }

    @PascalMethod(description = "crt library", returns = "void")
    public void noSound() {
        canPlaySound.set(false);
    }


}
