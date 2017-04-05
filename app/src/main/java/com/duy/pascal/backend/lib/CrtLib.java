package com.duy.pascal.backend.lib;

import android.graphics.Color;

import com.duy.pascal.frontend.activities.ExecuteActivity;

import java.util.Map;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CrtLib implements PascalLibrary {

    public static final String TAG = CrtLib.class.getSimpleName();
    private ExecuteActivity activity;

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

    public void highVideo() {
        // TODO: 08-Mar-17  do something
    }

}
