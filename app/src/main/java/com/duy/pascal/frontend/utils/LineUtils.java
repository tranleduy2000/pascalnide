package com.duy.pascal.frontend.utils;

import android.text.Layout;
import android.widget.ScrollView;

/**
 * Created by Duy on 08-Apr-17.
 */

public class LineUtils {
    public static int getYAtLine(ScrollView scrollView, int lineCount, int line) {
        return scrollView.getChildAt(0).getHeight() / lineCount * line;
    }

    /**
     * Gets the line from the index of the letter in the text
     *
     * @param index
     * @param lineCount
     * @param layout
     * @return
     */
    public static int getLineFromIndex(int index, int lineCount, Layout layout) {
        int line;
        int currentIndex = 0;
        for (line = 0; line < lineCount; line++) {
            currentIndex += layout.getLineEnd(line) - layout.getLineStart(line);
            if (currentIndex > index) {
                break;
            }
        }
        return line;
    }

}
