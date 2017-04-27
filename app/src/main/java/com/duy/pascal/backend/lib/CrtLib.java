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

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.frontend.activities.ExecHandler;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;
import com.duy.pascal.frontend.view.exec_screen.console.TextRenderer;
import com.js.interpreter.runtime.exception.WrongArgsException;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CrtLib implements PascalLibrary {
    public static final String TAG = CrtLib.class.getSimpleName();
    public static final String NAME = "crt";
    private static final Map<Integer, Integer> mapColorsPascal = new Hashtable<>();
    private static final Map<Integer, Integer> mapColorsAndroid = new Hashtable<>();

    static {
        generateMapColors();
    }

    private ExecHandler handler;
    private AtomicBoolean canPlaySound = new AtomicBoolean(false);
    private long finalFrequency;
    private Runnable soundRunnable = new Runnable() {
        @Override
        public void run() {
            while (canPlaySound.get()) {
                playSound(finalFrequency, 44100);
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
     * convert color of pascal to android color
     */
    public static int androidColorToPascalColor(int androidColor) {
        System.out.println("androidColor = " + androidColor);
        if (mapColorsAndroid.get(androidColor) != null) {
            return mapColorsAndroid.get(androidColor);
        } else {
            return 0;
        }
    }

    public static int pascalColorToAndroidColor(int pascalColor) {
        System.out.println("pascalColor = " + pascalColor);
        if (mapColorsPascal.get(pascalColor) != null) {
            return mapColorsPascal.get(pascalColor);
        }
        return 0;
    }

    public static void generateMapColors() {
        mapColorsPascal.put(0, Color.BLACK);
        mapColorsPascal.put(1, Color.BLUE);
        mapColorsPascal.put(2, Color.GREEN);
        mapColorsPascal.put(3, Color.CYAN);
        mapColorsPascal.put(4, Color.RED);
        mapColorsPascal.put(5, Color.MAGENTA);
        mapColorsPascal.put(6, 0xff49281E);
        mapColorsPascal.put(7, Color.LTGRAY);
        mapColorsPascal.put(8, Color.DKGRAY);
        mapColorsPascal.put(9, 0xffadd8e6);
        mapColorsPascal.put(10, 0xff98fb98);
        mapColorsPascal.put(11, 0xffe0ffff);
        mapColorsPascal.put(12, 0xffffa07a);
        mapColorsPascal.put(13, 0xffff00ff);
        mapColorsPascal.put(14, Color.YELLOW);
        mapColorsPascal.put(15, Color.WHITE);

        mapColorsAndroid.put(Color.BLACK, 0);
        mapColorsAndroid.put(Color.BLUE, 1);
        mapColorsAndroid.put(Color.GREEN, 2);
        mapColorsAndroid.put(Color.CYAN, 3);
        mapColorsAndroid.put(Color.RED, 4);
        mapColorsAndroid.put(Color.MAGENTA, 5);
        mapColorsAndroid.put(0xff49281E, 6);
        mapColorsAndroid.put(Color.LTGRAY, 7);
        mapColorsAndroid.put(Color.DKGRAY, 8);
        mapColorsAndroid.put(0xffadd8e6, 9);
        mapColorsAndroid.put(0xff98fb98, 10);
        mapColorsAndroid.put(0xffe0ffff, 11);
        mapColorsAndroid.put(0xffffa07a, 12);
        mapColorsAndroid.put(0xffff00ff, 13);
        mapColorsAndroid.put(Color.YELLOW, 14);
        mapColorsAndroid.put(Color.WHITE, 15);
    }

    /**
     * goto x, y procedure
     * <p>
     *
     * @param x - x coordinate of screen
     * @param y - y coordinate of screen
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void gotoXY(int x, int y) {
        if (handler == null) return;
        handler.getConsoleView().gotoXY(x, y);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    public void shutdown() {

    }

    /**
     * clear screen
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void clrscr() {
        if (handler == null) return;
        handler.getConsoleView().clearScreen();
    }

    /**
     * set text linePaint color
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void textColor(int code) {
        if (handler == null) return;
        int color = pascalColorToAndroidColor(code);
        handler.getConsoleView().setConsoleTextColor(color);
    }

    /**
     * set background console
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void textBackground(int code) {
        if (handler == null) return;
        int color = pascalColorToAndroidColor(code);
        handler.getConsoleView().setConsoleTextBackground(color);
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public int whereX() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereX();
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public int whereY() {
        if (handler == null) return 0;
        return handler.getConsoleView().whereY();
    }

    private void assertActivityNotNull() {
        if (handler == null) throw new RuntimeException("Can not define screen");
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void NormVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.NORMAL_TEXT_ALPHA);
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void HighVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.HIGH_TEXT_ALPHA);
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void LowVideo() {
        assertActivityNotNull();
        handler.getConsoleView().getTextRenderer().setAlpha(TextRenderer.LOW_TEXT_ALPHA);
    }

    /**
     * Show big cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void cursorBig() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setMode(CursorConsole.BIG_CURSOR);
    }

    /**
     * Hide cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void cursorOff() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setVisible(false);
    }

    /**
     * Display cursor
     */
    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void cursorOn() {
        assertActivityNotNull();
        handler.getConsoleView().getCursorConsole().setVisible(true);
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void sound(Integer frequency) throws WrongArgsException {
        this.sound(Long.valueOf(frequency));
    }

    @PascalMethod(description = "crt library", returns = "void")
    @SuppressWarnings("unused")
    public void noSound() {
        canPlaySound.set(false);
    }

    private void playSound(double frequency, int duration) {
        try {
            // AudioTrack definition
            int mBufferSize = AudioTrack.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_8BIT);

            AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    mBufferSize, AudioTrack.MODE_STREAM);

            // Sine wave
            double[] mSound = new double[4410];
            short[] mBuffer = new short[duration];
            for (int i = 0; i < mSound.length; i++) {
                mSound[i] = Math.sin((2.0 * Math.PI * i / (44100 / frequency)));
                mBuffer[i] = (short) (mSound[i] * Short.MAX_VALUE);
            }

            mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
            mAudioTrack.play();

            mAudioTrack.write(mBuffer, 0, mSound.length);
            mAudioTrack.stop();
            mAudioTrack.release();
        } catch (Exception ignored) {

        }
    }

}
