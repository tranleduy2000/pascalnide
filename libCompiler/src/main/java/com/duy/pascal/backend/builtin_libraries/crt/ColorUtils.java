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

package com.duy.pascal.backend.builtin_libraries.crt;

import android.graphics.Color;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Duy on 01-May-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ColorUtils {
    private static final Map<Integer, Integer> mapColorsPascal = new Hashtable<>();
    private static final Map<Integer, Integer> mapColorsAndroid = new Hashtable<>();

    static {
        generateMapColors();
    }

    public static void generateMapColors() {
        mapColorsPascal.put(0, Color.BLACK);
        mapColorsAndroid.put(Color.BLACK, 0);
        mapColorsPascal.put(1, Color.BLUE);
        mapColorsAndroid.put(Color.BLUE, 1);
        mapColorsPascal.put(2, Color.GREEN);
        mapColorsAndroid.put(Color.GREEN, 2);
        mapColorsPascal.put(3, Color.CYAN);
        mapColorsAndroid.put(Color.CYAN, 3);
        mapColorsPascal.put(4, Color.RED);
        mapColorsAndroid.put(Color.RED, 4);
        mapColorsPascal.put(5, Color.MAGENTA);
        mapColorsAndroid.put(Color.MAGENTA, 5);
        mapColorsPascal.put(6, 0xff49281E);
        mapColorsAndroid.put(0xff49281E, 6);
        mapColorsPascal.put(7, Color.LTGRAY);
        mapColorsAndroid.put(Color.LTGRAY, 7);
        mapColorsPascal.put(8, Color.DKGRAY);
        mapColorsAndroid.put(Color.DKGRAY, 8);
        mapColorsPascal.put(9, 0xffadd8e6);
        mapColorsAndroid.put(0xffadd8e6, 9);
        mapColorsPascal.put(10, 0xff98fb98);
        mapColorsAndroid.put(0xff98fb98, 10);
        mapColorsPascal.put(11, 0xffe0ffff);
        mapColorsAndroid.put(0xffe0ffff, 11);
        mapColorsPascal.put(12, 0xffffa07a);
        mapColorsAndroid.put(0xffffa07a, 12);
        mapColorsPascal.put(13, 0xffff00ff);
        mapColorsAndroid.put(0xffff00ff, 13);
        mapColorsPascal.put(14, Color.YELLOW);
        mapColorsAndroid.put(Color.YELLOW, 14);
        mapColorsPascal.put(15, Color.WHITE);
        mapColorsAndroid.put(Color.WHITE, 15);
    }

    /**
     * convert color of pascal to android color
     */
    public static int androidColorToPascalColor(int androidColor) {
        System.out.println("androidColor = " + androidColor);
        if (mapColorsAndroid.get(androidColor) != null) {
            return mapColorsAndroid.get(androidColor);
        }
        return Color.rgb(Color.red(androidColor), Color.green(androidColor),
                Color.blue(androidColor));
    }

    public static int pascalColorToAndroidColor(int pascalColor) {
        if (mapColorsPascal.get(pascalColor) != null) {
            return mapColorsPascal.get(pascalColor);
        }
        return Color.rgb(Color.red(pascalColor), Color.green(pascalColor), Color.blue(pascalColor));
    }
}
