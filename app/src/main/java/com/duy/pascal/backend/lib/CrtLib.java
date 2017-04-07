package com.duy.pascal.backend.lib;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.view.exec_screen.console.CursorConsole;
import com.duy.pascal.frontend.view.exec_screen.console.TextRenderer;
import com.js.interpreter.runtime.exception.WrongArgsException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CrtLib implements PascalLibrary {

    public static final String TAG = CrtLib.class.getSimpleName();
    private ExecuteActivity activity;
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
     *
     * @param activity
     */
    public CrtLib(ExecuteActivity activity) {
        this.activity = activity;
    }

    /**
     * Black = 0;
     * Blue = 1;
     * Green = 2;
     * Cyan = 3;
     * Red = 4;
     * Magenta = 5;
     * Brown = 6;
     * LightGray = 7;
     * DarkGray = 8;
     * LightBlue = 9;
     * LightGreen = 10;
     * LightCyan = 11;
     * LightRed = 12;
     * LightMagenta = 13;
     * Yellow = 14;
     * White = 15;
     */
    public static int getColorPascal(int code) {
        System.out.println("get color " + code);
        switch (code) {
            case 0:
                return (Color.BLACK);
            case 1:
                return (Color.BLUE);
            case 2:
                return (Color.GREEN);
            case 3:
                return (Color.CYAN);
            case 4:
                return (Color.RED);
            case 5:
                return (Color.MAGENTA);
            case 6:
                return (Color.parseColor("#49281E"));
            case 7:
                return (Color.LTGRAY);
            case 8:
                return (Color.DKGRAY);
            case 9:
                return (Color.parseColor("#add8e6"));
            case 10:
                return (Color.parseColor("#98fb98"));
            case 11:
                return (Color.parseColor("#e0ffff"));
            case 12:
                return (Color.parseColor("#ffa07a"));
            case 13:
                return (Color.parseColor("#ff00ff"));
            case 14:
                return (Color.YELLOW);
            case 15:
                return (Color.WHITE);
        }
        return -1;
    }

    /**
     * goto x, y procedure
     * <p>
     *
     * @param x - x coordinate of screen
     * @param y - y coordinate of screen
     */
    public void gotoXY(int x, int y) {
        if (activity == null) return;
        activity.getConsoleView().gotoXY(x, y);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    /**
     * clear screen
     */
    public void clrscr() {
        if (activity == null) return;
        activity.getConsoleView().clearScreen();
    }

    /**
     * set text mPaint color
     *
     * @param code
     */
    public void textColor(int code) {
        if (activity == null) return;
        int color = getColorPascal(code);
        activity.setTextColor((color));
    }

    /**
     * set background console
     */
    public void textBackground(int code) {
        if (activity == null) return;
        int color = getColorPascal(code);
        activity.getConsoleView().setConsoleTextBackground(color);
    }

    public int whereX() {
        if (activity == null) return 0;
        return activity.getConsoleView().whereX();
    }

    public int whereY() {
        if (activity == null) return 0;
        return activity.getConsoleView().whereY();
    }

    private void assertActivityNotNull() {
        if (activity == null) throw new RuntimeException("Can not define screen");
    }

    public void NormVideo() {
        assertActivityNotNull();
        activity.getConsoleView().getTextRenderer().setAlpha(TextRenderer.NORMAL_TEXT_ALPHA);
    }

    public void HighVideo() {
        assertActivityNotNull();
        activity.getConsoleView().getTextRenderer().setAlpha(TextRenderer.HIGH_TEXT_ALPHA);
    }

    public void LowVideo() {
        assertActivityNotNull();
        activity.getConsoleView().getTextRenderer().setAlpha(TextRenderer.LOW_TEXT_ALPHA);
    }

    /**
     * Show big cursor
     */
    public void cursorBig() {
        assertActivityNotNull();
        activity.getConsoleView().getCursorConsole().setMode(CursorConsole.BIG_CURSOR);
    }

    /**
     * Hide cursor
     */
    public void cursorOff() {
        assertActivityNotNull();
        activity.getConsoleView().getCursorConsole().setVisible(false);
    }

    /**
     * Display cursor
     */
    public void cursorOn() {
        assertActivityNotNull();
        activity.getConsoleView().getCursorConsole().setVisible(true);
    }

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

    public void sound(Integer frequency) throws WrongArgsException {
        sound(Long.valueOf(frequency));
    }


    public void noSound() {
        canPlaySound.set(false);
    }

    private void playSound(double frequency, int duration) {

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

    }

}
