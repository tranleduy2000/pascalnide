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

package com.duy.pascal.frontend.utils;

import android.text.Layout;
import android.widget.ScrollView;

/**
 * Created by Duy on 08-Apr-17.
 */

public class LineUtils {
    public static int getYAtLine(ScrollView scrollView, int lineCount, int line) {
        if (lineCount == 0) return 0;
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
