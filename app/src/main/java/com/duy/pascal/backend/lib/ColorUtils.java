package com.duy.pascal.backend.lib;

import android.graphics.Color;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Duy on 01-May-17.
 */

public class ColorUtils {
    private static final Map<Integer, Integer> mapColorsPascal = new Hashtable<>();
    private static final Map<Integer, Integer> mapColorsAndroid = new Hashtable<>();

    static {
        generateMapColors();
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
}
