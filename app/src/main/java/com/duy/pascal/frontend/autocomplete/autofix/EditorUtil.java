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

package com.duy.pascal.frontend.autocomplete.autofix;

import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.widget.EditText;

import com.duy.pascal.frontend.autocomplete.autofix.model.TextData;
import com.duy.pascal.frontend.editor.view.EditorView;
import com.duy.pascal.interperter.linenumber.LineInfo;

/**
 * Created by Duy on 9/24/2017.
 */

class EditorUtil {
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

    static TextData getText(EditText editable, LineInfo startLine, LineInfo endLine) {
        Log.d(TAG, "getText() called with: editable = [" + editable + "], startLine = [" + startLine + "], endLine = [" + endLine + "]");

        CharSequence text = editable.getText().subSequence(
                editable.getLayout().getLineStart(startLine.getLine())
                        + startLine.getColumn(),

                editable.getLayout().getLineEnd(endLine.getLine()));

        int offset = editable.getLayout().getLineStart(startLine.getLine())
                + startLine.getColumn()
                + startLine.getLength();

        if (offset < 0) offset = 0;
        TextData textData = new TextData(text, offset);
        Log.d(TAG, "getText() returned: " + textData);
        return textData;
    }
}
