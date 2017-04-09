package com.duy.pascal.backend.lib.graph.text_model;

/**
 * Created by Duy on 09-Apr-17.
 */

public class TextJustify {
    private int horizontal = HORIZONTAL_STYLE.LeftText;
    private int vertical = VERTICAL_STYLE.BottomText;

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
