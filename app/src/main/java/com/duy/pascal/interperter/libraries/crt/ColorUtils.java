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

package com.duy.pascal.interperter.libraries.crt;

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
        //black color
        mapColorsPascal.put(0, Color.BLACK);
        mapColorsAndroid.put(Color.BLACK, 0);

        //blue color
        mapColorsPascal.put(1, Color.BLUE);
        mapColorsAndroid.put(Color.BLUE, 1);

        //green color
        mapColorsPascal.put(2, Color.rgb(0, 127, 0));
        mapColorsAndroid.put(Color.rgb(0, 127, 0), 2);

        //cyan color
        mapColorsPascal.put(3, Color.rgb(0, 127, 127));
        mapColorsAndroid.put(Color.rgb(0, 127, 127), 3);

        //red color
        mapColorsPascal.put(4, Color.RED);
        mapColorsAndroid.put(Color.RED, 4);

        //magenta color
        mapColorsPascal.put(5, Color.rgb(255,0,127));
        mapColorsAndroid.put(Color.rgb(255,0,127), 5);

        //brown color
        mapColorsPascal.put(6, Color.rgb(127, 127, 0));
        mapColorsAndroid.put(Color.rgb(127, 127, 0), 6);

        //light gray
        mapColorsPascal.put(7, Color.LTGRAY);
        mapColorsAndroid.put(Color.LTGRAY, 7);

        //dark gray
        mapColorsPascal.put(8, Color.DKGRAY);
        mapColorsAndroid.put(Color.DKGRAY, 8);

        //light blue
        mapColorsPascal.put(9, Color.rgb(159, 159, 255));
        mapColorsAndroid.put(Color.rgb(159, 159, 255), 9);

        //light green
        mapColorsPascal.put(10, Color.rgb(127, 255, 127));
        mapColorsAndroid.put(Color.rgb(127, 255, 127), 10);

        //light cyan
        mapColorsPascal.put(11, Color.rgb(127, 255, 255));
        mapColorsAndroid.put(Color.rgb(127, 255, 255), 11);

        //light red
        mapColorsPascal.put(12, Color.rgb(255, 159, 159));
        mapColorsAndroid.put(Color.rgb(255, 159, 159), 12);

        //light magenta
        mapColorsPascal.put(13, Color.rgb(255, 127, 255));
        mapColorsAndroid.put(Color.rgb(255, 127, 255), 13);

        //yellow
        mapColorsPascal.put(14, Color.YELLOW);
        mapColorsAndroid.put(Color.YELLOW, 14);

        //white
        mapColorsPascal.put(15, Color.WHITE);
        mapColorsAndroid.put(Color.WHITE, 15);
    }

    /**
     * convert color of pascal to android color
     */
    public static int androidColorToPascalColor(int androidColor) {
        if (mapColorsAndroid.get(androidColor) != null) {
            return mapColorsAndroid.get(androidColor);
        }
        if (androidColor < 1 << 24) { //not alpha
            return Color.rgb(Color.red(androidColor), Color.green(androidColor), Color.blue(androidColor));
        }
        return androidColor; //include alpha
    }

    public static int pascalColorToAndroidColor(int pascalColor) {
        if (mapColorsPascal.get(pascalColor) != null) {
            return mapColorsPascal.get(pascalColor);
        }
        if (pascalColor < 1 << 24) { //not alpha
            return Color.rgb(Color.red(pascalColor), Color.green(pascalColor), Color.blue(pascalColor));
        }
        return pascalColor; //include alpha
    }
}
