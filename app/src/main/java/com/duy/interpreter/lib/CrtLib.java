package com.duy.interpreter.lib;

import android.graphics.Color;

import com.duy.pascal.compiler.activities.ExecuteActivity;

import java.util.Map;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CrtLib implements PascalLibrary {

    public static final String TAG = CrtLib.class.getSimpleName();
    ExecuteActivity activity;
//    String doc = "\n" +
//            "AssignCrt: " + "Assign file to CRT.\n" +
//            "ClrEol\n" + "Clear from cursor position till end of line.\n" +
//            "ClrScr\n" + "Clear current window.\n" + //ok
//            "cursorbig\n" + "Show big cursor\n" +
//            "cursoroff\n" + "Hide cursor\n" +
//            "cursoron\n" + "Display cursor\n" +
//            "Delay\n" + "Delay program execution.\n" + //ok
//            "DelLine\n" + "Delete line at cursor position.\n" +
//            "gotoXY\n" + "Set cursor position on screen.\n" + //ok
//            "HighVideo\n" + "Switch to highlighted text mode\n" +
//            "InsLine\n" + "Insert an empty line at cursor position\n" +
//            "KeyPressed\n" + "Check if there is a keypress in the keybuffer\n" +
//            "LowVideo\n" + "Switch to low intensity colors.\n" +
//            "NormVideo\n" + "Return to normal (startup) modus\n" +
//            "NoSound\n" + "Stop system speaker\n" +
//            "ReadKey\n" + "Read key from keybuffer\n" +
//            "Sound\n" + "Sound system speaker\n" +
//            "TextBackground\n" + "Set text background\n" +
//            "TextColor\n" + "Set text color\n" +
//            "TextMode\n" + "Set screen mode.\n" +
//            "whereX\n" + "Return X (horizontal) cursor position\n" +
//            "whereY\n" + "Return Y (vertical) cursor position\n" +
//            "Window\n" + "Create new window on screen.";

    /**
     * constructor call by {@link ClassLoader} in {@link com.duy.interpreter.startup.PascalCompiler}
     *
     * @param activity
     */
    public CrtLib(ExecuteActivity activity) {
        System.out.println("CrtLib activity");
        this.activity = activity;
    }

    public CrtLib() {

    }


    /**
     * delay procedure
     *
     * @param ms - time
     */
    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
//            System.err.println("??? Interrupted.");
            e.printStackTrace();
        }
    }

    /**
     * goto x, y procedure
     *
     * @param x - x coordinate of screen
     * @param y - y coordinate of screen
     */
    public void gotoXY(int x, int y) {
        activity.mConsoleView.gotoXY(x, y);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    /**
     * clear screen
     */
    public void clrscr() {
        activity.mConsoleView.clearScreen();
    }

    /**
     * set text paint color
     *
     * @param code
     */
    public void textColor(int code) {
        int color = getColorPascal(code);
        if (color != -1) activity.setTextColor((color));
    }

    /**
     * set background console
     */
    public void textBackground(int code) {
        int color = getColorPascal(code);
        if (color != -1) activity.setTextBackground(color);
    }

    public int whereX() {
        return activity.mConsoleView.whereX();
    }

    public int whereY() {
        return activity.mConsoleView.whereY();
    }

    private int getColorPascal(int code) {
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

    public void highVideo() {
        // TODO: 08-Mar-17  do something
    }

}
