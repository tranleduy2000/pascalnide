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

package com.duy.pascal.frontend.view.editor_view;

import android.content.Context;
import android.graphics.Rect;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.EditorSetting;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_completion.KeyWord;
import com.duy.pascal.frontend.program_structure.viewholder.StructureType;
import com.duy.pascal.frontend.view.editor_view.adapters.CodeSuggestAdapter;
import com.duy.pascal.frontend.view.editor_view.adapters.StructureItem;

import java.util.ArrayList;

/**
 * AutoSuggestsEditText
 * show hint when typing
 * Created by Duy on 28-Feb-17.
 */

public abstract class CodeSuggestsEditText extends AutoIndentEditText {
    private static final String TAG = CodeSuggestsEditText.class.getName();

    public int mCharHeight = 0;
    protected EditorSetting mEditorSetting;
    protected SymbolsTokenizer mTokenizer;
    private CodeSuggestAdapter mAdapter;


    public CodeSuggestsEditText(Context context) {
        super(context);
        init();
    }

    public CodeSuggestsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeSuggestsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * slipt string in edittext and put it to list keyword
     */
    public void invalidateKeyWord() {
        setSuggestData(new ArrayList<StructureItem>());
    }

    private void init() {
        mEditorSetting = new EditorSetting(getContext());
        invalidateKeyWord();
        mTokenizer = new SymbolsTokenizer();
        setTokenizer(mTokenizer);
        setThreshold(1);
        invalidateCharHeight();

    }

    /**
     * @return the height of view display on screen
     */
    public int getHeightVisible() {
        Rect r = new Rect();
        // r will be populated with the coordinates of     your view
        // that area still visible.
        getWindowVisibleDisplayFrame(r);
        return r.bottom - r.top;
    }

    private void invalidateCharHeight() {
        mCharHeight = (int) Math.ceil(getPaint().getFontSpacing());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        onPopupChangePosition();
    }

    public abstract void onPopupChangePosition();

    /**
     * invalidate data for auto suggest
     */
    public void setSuggestData(ArrayList<StructureItem> data) {
        if (!mEditorSetting.isShowSuggestPopup()) {
            if (mAdapter != null) {
                mAdapter.clear();
            } else {
                mAdapter = new CodeSuggestAdapter(getContext(), R.layout.code_hint,
                        new ArrayList<StructureItem>());
                setAdapter(mAdapter);
            }
            return;
        }
        for (String s : KeyWord.KEY_WORDS) {
            data.add(new StructureItem(StructureType.TYPE_KEY_WORD, s));
        }
        mAdapter = new CodeSuggestAdapter(getContext(), R.layout.code_hint, data);
        setAdapter(mAdapter);
        onDropdownChangeSize();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (Dlog.DEBUG) {
            Log.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" +
                    oldw + "], oldh = [" + oldh + "]");
        }
        onDropdownChangeSize();
    }

    /**
     * this method will be change size of popup window
     */
    protected void onDropdownChangeSize() {
        // 1/2 width of screen
        int width = getWidth() / 2;
        setDropDownWidth(width);

        // 0.5 height of screen
        int height = getHeightVisible() / 2;
        setDropDownHeight(height);

        //change position
        onPopupChangePosition();
    }

    @Override
    public void showDropDown() {
        if (!isPopupShowing()) {
            if (hasFocus()) {
                super.showDropDown();
            }
        }
    }

    protected class SymbolsTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        String token = "!@#$%^&*()_+-={}|[]:;'<>/<.? \n\t";

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !token.contains(Character.toString(text.charAt(i - 1)))) {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }
            return i;
        }

        @Override
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            while (i < len) {
                if (token.contains(Character.toString(text.charAt(i - 1)))) {
                    return i;
                } else {
                    i++;
                }
            }
            return len;
        }

        @Override
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && token.contains(Character.toString(text.charAt(i - 1)))) {
                return text + " ";
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
}

