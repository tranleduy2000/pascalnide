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

package com.duy.pascal.ui.autocomplete.autofix;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Layout;
import android.widget.EditText;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.autocomplete.autofix.model.TextData;
import com.duy.pascal.ui.editor.view.EditorView;

/**
 * Created by Duy on 9/24/2017.
 */
public class EditorUtil {
    private static final String TAG = "EditorUtil";

    /**
     * get text in lineInfo
     */
    static CharSequence getTextInLine(EditorView editable, int line, int column) {
        Editable text = editable.getText();
        Layout layout = editable.getLayout();
        if (layout != null) {
            int lineStart = layout.getLineStart(line);
            int lineEnd = layout.getLineEnd(line);
            lineStart = lineStart + column;
            if (lineStart > text.length()) lineStart = text.length();
            if (lineStart > lineEnd) lineStart = lineEnd;
            return text.subSequence(lineStart, lineEnd);
        }
        return "";
    }

    @NonNull
    public static TextData getText(EditText editable, LineInfo startLine, LineInfo endLine) {
        int start = editable.getLayout().getLineStart(startLine.getLine());
        int end = editable.getLayout().getLineEnd(endLine.getLine());

        int startIndex = start + startLine.getColumn();
        CharSequence text = editable.getText().subSequence(startIndex, end);

        int offset = start + startLine.getColumn() /*+ startLine.getLength()*/;

        if (offset < 0) {
            offset = 0;
        }
        return new TextData(text, offset);
    }

    @NonNull
    public static String getIndentLine(String text, int cursor) {
        while (cursor >= 0 && text.charAt(cursor) != '\n') {
            cursor--;
        }
        StringBuilder indent = new StringBuilder();
        if ((cursor >= 0 && text.charAt(cursor) == '\n') || (cursor == 0)) {
            cursor++;
            while (cursor < text.length() && text.charAt(cursor) == ' ') {
                indent.append(" ");
                cursor++;
            }
        }
        return indent.toString();
    }
}
