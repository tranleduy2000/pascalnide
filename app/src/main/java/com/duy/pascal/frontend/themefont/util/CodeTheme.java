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

package com.duy.pascal.frontend.themefont.util;

import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class CodeTheme implements Serializable {
    private static final String TAG = "CodeTheme";

    private final boolean builtin;
    private String id;
    private HashMap<String, Integer> colors = new HashMap<>();

    public CodeTheme(String id, boolean builtin) {
        this.id = id;
        this.builtin = builtin;
    }

    public CodeTheme(boolean builtin) {
        this.builtin = builtin;
    }


    public HashMap<String, Integer> getColors() {
        return colors;
    }

    public int getBackground() {
        return getColor("background_color");
    }

    public int getTextColor() {
        return getColor("normal_text_color");
    }

    public void setTextColor(int integer) {
        putColor("normal_text_color", integer);
    }

    @Override
    public String toString() {
        return colors.toString();
    }

    public String getId() {
        return this.id;
    }

    public boolean isBuiltin() {
        return this.builtin;
    }

    public void putColor(String name, Integer color) {
        this.colors.put(name, color);
    }

    public Integer getColor(String name) {
        return this.colors.get(name);
    }

    public void setKeyWordColor(int integer) {
        putColor("key_word_color", integer);
    }

    public void setBooleanColor(int integer) {
        putColor("boolean_color", integer);
    }

    public int getErrorColor() {
        return getColor("error_color");
    }

    public void setErrorColor(int integer) {
        putColor("error_color", integer);
    }

    public int getNumberColor() {
        return getColor("number_color");
    }

    public void setNumberColor(int integer) {
        putColor("number_color", integer);
    }

    public int getKeywordColor() {
        return getColor("key_word_color");
    }

    public int getOptColor() {
        return getColor("opt_color");
    }

    public void setOptColor(int integer) {
        putColor("opt_color", integer);
    }

    public int getCommentColor() {
        return getColor("comment_color");
    }

    public void setCommentColor(int integer) {
        putColor("comment_color", integer);
    }

    public int getStringColor() {
        return getColor("string_color");
    }

    public void setStringColor(int integer) {
        putColor("string_color", integer);
    }

    public int getDebugColor() {
        int background = getBackground();
        float[] hsv = new float[3];
        Color.colorToHSV(background, hsv);
        hsv[2] = Math.min(1, Math.max(0.1f, hsv[2]) * 1.25f);//brightness color
        Log.d(TAG, "getDebugColor: " + Arrays.toString(hsv));
        return Color.HSVToColor(hsv);
    }

    public void setBackgroundColor(int integer) {
        putColor("background_color", integer);
    }
}
