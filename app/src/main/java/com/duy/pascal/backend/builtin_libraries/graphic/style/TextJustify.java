/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.builtin_libraries.graphic.style;

/**
 * Created by Duy on 09-Apr-17.
 */

public class TextJustify {
    private int horizontal = HORIZONTAL_STYLE.LeftText;
    private int vertical = VERTICAL_STYLE.BottomText;

    public TextJustify(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public TextJustify() {

    }

    /**
     * clone new text justify
     *
     * @return
     */
    public TextJustify clone() {
        TextJustify textJustify = new TextJustify();
        textJustify.setHorizontal(horizontal);
        textJustify.setVertical(vertical);
        return textJustify;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public static final class HORIZONTAL_STYLE {
        //        Text is set left of the pointer.
        public static final int LeftText = 0;

        //        Text is set centered horizontally on the pointer.
        public static final int CenterText = 1;

        //Text is set to the right of the pointer.
        public static final int RightText = 2;
    }

    public static final class VERTICAL_STYLE {
        //Text is placed under the pointer.
        public static final int BottomText = 0;

        //text is place centered vertically on the pointer
        public static final int CenterText = 1;

        //text is place above the pointer
        public static final int TopText = 2;
    }
}
