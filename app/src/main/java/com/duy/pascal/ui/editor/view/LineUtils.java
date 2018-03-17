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

package com.duy.pascal.ui.editor.view;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.Layout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ScrollView;

import com.duy.pascal.ui.utils.DLog;

public class LineUtils {
    private static final String TAG = "LineUtils";
    private boolean[] toCountLinesArray;
    private int[] realLines;

    /**
     * @param editable - edit text
     * @param line   - current line
     * @param col    - column index at current line
     * @return the index at (line:col)
     */
    public static int getIndexFromLineCol(Layout editable, int line, int col) {
        int index = editable.getLineStart(line);
        index += col;
        return Math.min(index, editable.getText().length());
    }

    public static int getYAtLine(ScrollView scrollView, int lineCount, int line) {
        if (lineCount == 0) return 0;
        return scrollView.getChildAt(0).getHeight() / lineCount * line;
    }

    public static int getFirstVisibleLine(@NonNull ScrollView scrollView, int childHeight,
                                          int lineCount) throws ArithmeticException {
        if (childHeight == 0) return 0;
        int line = (scrollView.getScrollY() * lineCount) / childHeight;
        if (line < 0) line = 0;
        return line;
    }

    public static int getLastVisibleLine(@NonNull ScrollView scrollView,
                                         int childHeight, int lineCount) {
        if (childHeight == 0) return 0;
        int line = (scrollView.getScrollY() * lineCount) / childHeight;
        if (line > lineCount) line = lineCount;
        return line;
    }

    /**
     * Gets the lineInfo from the index of the letter in the text
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

    /**
     * Gets the line from the index of the letter in the text
     * <p>
     * 1  2  3  |
     * ^  ^  ^  ^
     * 0  1  2  cursor at 4, return (line;col) = (0;4), line start at 0, column start at 0
     */
    @NonNull
    public static Pair<Integer, Integer> getLineColFromIndex(int cursorIndex, int length, int lineCount, Layout layout) {
        int line;
        int currentIndex = 0, oldIndex = 0;

        line = 0;
        while (line < lineCount) {
            oldIndex = currentIndex;
            currentIndex += layout.getLineEnd(line) - layout.getLineStart(line);
            if (currentIndex > cursorIndex) {
                break;
            }
            if (line < lineCount - 1) {
                line++;
            } else {
                break;
            }
        }
        Pair<Integer, Integer> result = new Pair<>(line, cursorIndex - oldIndex);
        DLog.d(TAG, "getLineColFromIndex() returned: " + result);
        return result;


    }

    public static int getStartIndexAtLine(EditText editable, int line) {
        Layout layout = editable.getLayout();
        if (layout != null) {
            return layout.getLineStart(line);
        }
        return 0;
    }

    public boolean[] getGoodLines() {
        return toCountLinesArray;
    }

    public int[] getRealLines() {
        return realLines;
    }

    public void updateHasNewLineArray(int lineCount, Layout layout, String text) {
        boolean[] hasNewLineArray = new boolean[lineCount];
        toCountLinesArray = new boolean[lineCount];
        realLines = new int[lineCount];
        if (TextUtils.isEmpty(text)) {
            toCountLinesArray[0] = false;
            realLines[0] = 0;
            return;
        }

        if (lineCount == 0) return;

        int i;

        // for every lineInfo on the edittext
        for (i = 0; i < lineCount; i++) {
            // check if this lineInfo contains "\n"
            if (layout.getLineEnd(i) == 0) {
                hasNewLineArray[i] = false;
            } else {
                hasNewLineArray[i] = text.charAt(layout.getLineEnd(i) - 1) == '\n';
            }
            // if true
            if (hasNewLineArray[i]) {
                int j = i - 1;
                while (j >= 0 && !hasNewLineArray[j]) {
                    j--;
                }
                toCountLinesArray[j + 1] = true;

            }
        }

        toCountLinesArray[lineCount - 1] = true;

        int realLine = 0;
        for (i = 0; i < toCountLinesArray.length; i++) {
            realLines[i] = realLine;
            if (toCountLinesArray[i]) {
                realLine++;
            }
        }
    }

    public int firstReadLine() {
        return realLines[0];
    }

    public int lastReadLine() {
        return realLines[realLines.length - 1];
    }

    public int fakeLineFromRealLine(int realLine) {
        int i;
        int fakeLine = 0;
        for (i = 0; i < realLines.length; i++) {
            if (realLine == realLines[i]) {
                fakeLine = i;
                break;
            }
        }
        return fakeLine;
    }
}