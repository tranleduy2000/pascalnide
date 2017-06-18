///*
// *  Copyright (c) 2017 Tran Le Duy
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.duy.pascal.frontend.editor.editor_view.highlighter;
//
//import android.text.Editable;
//
//import com.duy.pascal.frontend.editor.editor_view.HighlightEditor;
//
//import static com.duy.pascal.frontend.editor.editor_view.HighlightEditor.CHARS_TO_COLOR;
//
///**
// * Created by Duy on 18-Jun-17.
// */
//
//public class FastHighlight {
//    private HighlightEditor editor;
//
//    public FastHighlight(HighlightEditor highlightEditor) {
//
//        this.editor = highlightEditor;
//    }
//
//    public Editable highlight(Editable editable, boolean newText) {
////        editable.clearSpans();
//        if (editable.length() == 0) {
//            return editable;
//        }
//
//        int editorHeight = editor.getHeightVisible();
//
//        int firstVisibleIndex;
//        int lastVisibleIndex;
//        if (!newText && editorHeight > 0) {
//            if (verticalScroll != null && getLayout() != null) {
//                firstVisibleIndex = getLayout().getLineStart(getFirstLineIndex());
//            } else {
//                firstVisibleIndex = 0;
//            }
//            if (verticalScroll != null && getLayout() != null) {
//                lastVisibleIndex = getLayout().getLineStart(getLastLineIndex());
//            } else {
//                lastVisibleIndex = getText().length();
//            }
//        } else {
//            firstVisibleIndex = 0;
//            lastVisibleIndex = CHARS_TO_COLOR;
//        }
//        int delta = (lastVisibleIndex - firstVisibleIndex) / 3;
//        firstVisibleIndex -= delta;
//        lastVisibleIndex += delta;
//
//        // normalize
//        if (firstVisibleIndex < 0)
//            firstVisibleIndex = 0;
//        if (lastVisibleIndex > editable.length())
//            lastVisibleIndex = editable.length();
//        if (firstVisibleIndex > lastVisibleIndex)
//            firstVisibleIndex = lastVisibleIndex;
//
//        //clear all span for firstVisibleIndex to lastVisibleIndex
//        clearSpans(editable, firstVisibleIndex, lastVisibleIndex);
//
//        CharSequence textToHighlight = editable.subSequence(firstVisibleIndex, lastVisibleIndex);
//        highlighter.highlight(editable, textToHighlight, firstVisibleIndex);
//        applyTabWidth(editable, firstVisibleIndex, lastVisibleIndex);
//        return editable;
//    }
//
//}
