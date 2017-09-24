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

package com.duy.pascal.frontend.editor.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.autocomplete.completion.SuggestionProvider;
import com.duy.pascal.frontend.autocomplete.completion.model.Description;
import com.duy.pascal.frontend.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.frontend.autocomplete.completion.model.KeyWord;
import com.duy.pascal.frontend.editor.view.adapters.CodeSuggestAdapter;
import com.duy.pascal.frontend.structure.viewholder.StructureType;

import java.util.ArrayList;

/**
 * AutoSuggestsEditText
 * show hint when typing
 * Created by Duy on 28-Feb-17.
 */

public abstract class CodeSuggestsEditText extends AutoIndentEditText {
    private static final String TAG = "CodeSuggestsEditText";

    public int mCharHeight = 0;
    public int mCharWidth = 0;
    protected SymbolsTokenizer mTokenizer;
    private CodeSuggestAdapter mAdapter;
    private SuggestionProvider pascalParserHelper;
    private ParseTask parseTask;
    protected boolean mEnableSyntaxParser = true;

    public CodeSuggestsEditText(Context context) {
        super(context);
        init(context);
    }

    public CodeSuggestsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CodeSuggestsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * slipt string in edittext and put it to list keyword
     */
    public void setDefaultKeyword() {
        ArrayList<Description> data = new ArrayList<>();
        for (String s : KeyWord.ALL_KEY_WORD) {
            data.add(new DescriptionImpl(StructureType.TYPE_KEY_WORD, s));
        }
        setSuggestData(data);
    }

    private void init(Context context) {
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
        mCharHeight = (int) getPaint().measureText("M");
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mEnableSyntaxParser) {
            try {
                if (mEditorSetting.isShowSuggestPopup()) {
                    if (parseTask != null) {
                        parseTask.cancel(true);
                    }
                    parseTask = new ParseTask(this, "");
                    parseTask.execute();
                }
            } catch (Exception ignored) {
            }
            onPopupChangePosition();
        }
    }

    public abstract void onPopupChangePosition();

    /**
     * invalidate data for auto suggest
     */
    public void setSuggestData(String[] data) {
        ArrayList<Description> items = new ArrayList<>();
        for (String s : data) {
            items.add(new DescriptionImpl(StructureType.TYPE_KEY_WORD, s));
        }
        setSuggestData(items);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (DLog.DEBUG) {
            DLog.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" +
                    oldw + "], oldh = [" + oldh + "]");
        }
        onDropdownChangeSize(w, h);
    }

    /**
     * this method will be change size of popup window
     */
    protected void onDropdownChangeSize(int w, int h) {

        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);

        Log.d(TAG, "onDropdownChangeSize: " + rect);
        w = rect.width();
        h = rect.height();

        // 1/2 width of screen
        setDropDownWidth((int) (w * 0.7f));

        // 0.5 height of screen
        setDropDownHeight((int) (h * 0.5f));

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

    public void setEnoughToFilter() {
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    public void restoreAfterClick(final String[] data) {
        setSuggestData(data);
    }

    public ArrayList<Description> getSuggestData() {
        return mAdapter.getAllItems();
    }

    /**
     * invalidate data for auto suggest
     */
    public void setSuggestData(ArrayList<Description> data) {
        if (mAdapter != null) {
            mAdapter.clearAllData();
            mAdapter.addData(data);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CodeSuggestAdapter(getContext(), R.layout.list_item_suggest, data);
            setAdapter(mAdapter);
        }
        if (data.size() > 0) {
            showDropDown();
            onPopupChangePosition();
            onDropdownChangeSize(getWidth(), getHeight());
        }
    }




    public static class SymbolsTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        static final String TOKEN = "!@#$%^&*()_+-={}|[]:;'<>/<.? \r\n\t";

        @Override
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            while (i > 0 && !TOKEN.contains(Character.toString(text.charAt(i - 1)))) {
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
                if (TOKEN.contains(Character.toString(text.charAt(i - 1)))) {
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
            return text;
        }
    }

    private class ParseTask extends AsyncTask<Object, Object, ArrayList<Description>> {
        private String source;
        private String srcPath;
        private SuggestionProvider pascalParserHelper;
        private int cursorPos, cursorLine, cursorCol;

        private ParseTask(EditText editText, String srcPath) {
            this.source = editText.getText().toString();
            this.srcPath = srcPath;
            this.pascalParserHelper = new SuggestionProvider();
            calculateCursor(editText);
        }

        private void calculateCursor(EditText editText) {
            this.cursorPos = editText.getSelectionStart();
            Pair<Integer, Integer> lineColFromIndex = LineUtils.getLineColFromIndex(cursorPos,
                    editText.getLayout().getLineCount(), editText.getLayout());
            this.cursorLine = lineColFromIndex.first;
            this.cursorCol = lineColFromIndex.second;
        }

        @Override
        protected ArrayList<Description> doInBackground(Object... params) {
            return pascalParserHelper.getSuggestion(srcPath, source,
                    cursorPos, cursorLine, cursorCol);
        }

        @Override
        protected void onPostExecute(ArrayList<Description> result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                Log.d(TAG, "onPostExecute: cancel");
                return;
            }
            if (result == null) {
                setSuggestData(new ArrayList<Description>());
            } else {
                setSuggestData(result);
            }
        }
    }
}

