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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.QwertyKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.MultiAutoCompleteTextView;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.completion.SuggestOperation;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.editor.view.adapters.CodeSuggestAdapter;
import com.duy.pascal.ui.structure.viewholder.StructureType;
import com.duy.pascal.ui.utils.DLog;

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

    protected MultiAutoCompleteTextView.Tokenizer mTokenizer;
    protected boolean mEnableSyntaxParser = true;
    @NonNull
    protected ArrayList<LineInfo> mLineErrors = new ArrayList<>();
    private CodeSuggestAdapter mAdapter;
    private SuggestOperation pascalParserHelper;
    private ParseDataTask mParseTask;

    private ListPopupWindow mPopup;
    private int mDropDownAnchorId;
    private boolean mBlockCompletion;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    private boolean mDropDownDismissedOnCompletion = true;
    private Filter mFilter;
    private int mLastKeyCode;

    /**
     * Current source name
     */
    @NonNull
    private String mSrcName = "undefine";

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

    public void setDropDownAnchorId(int mDropDownAnchorId) {
        this.mDropDownAnchorId = mDropDownAnchorId;
    }

    private void init(Context context) {
        mTokenizer = new SymbolsTokenizer();
        setTokenizer(mTokenizer);
        // setThreshold(1);
        calculateCharHeight();

        mDropDownAnchorId = View.NO_ID;
        mPopup = new ListPopupWindow(context);
        mPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopup.setPromptPosition(ListPopupWindow.POSITION_PROMPT_BELOW);
        mPopup.setOnItemClickListener(new DropDownItemClickListener());
        mPopup.setAnimationStyle(android.R.style.Animation_Dialog);

        onDropdownChangeSize(getWidth(), getHeight());
    }

    public void setTokenizer(MultiAutoCompleteTextView.Tokenizer t) {
        mTokenizer = t;
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

    private void calculateCharHeight() {
        mCharHeight = (int) Math.ceil(getPaint().getFontSpacing());
        mCharHeight = (int) getPaint().measureText("M");
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mEnableSyntaxParser && hasFocus() && mEditorSetting.isShowSuggestPopup()) {
            try {
                if (mParseTask != null) mParseTask.cancel(true);
                mParseTask = new ParseDataTask(this, mSrcName);
                mParseTask.execute();
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

        DLog.d(TAG, "onDropdownChangeSize: " + rect);
        w = rect.width();
        h = rect.height();

        // 1/2 width of screen
        setDropDownWidth((int) (w * 0.7f));

        // 0.5 height of screen
        setDropDownHeight((int) (h * 0.5f));

        //change position
        onPopupChangePosition();
    }

    public void setDropDownWidth(int width) {
        mPopup.setWidth(width);
    }

    public int getDropDownHeight() {
        return mPopup.getHeight();
    }

    public void setDropDownHeight(int height) {
        mPopup.setHeight(height);
    }

    public void showDropDown() throws Exception {
        if (mPopup.getAnchorView() == null) {
            if (mDropDownAnchorId != View.NO_ID) {
                mPopup.setAnchorView(getRootView().findViewById(mDropDownAnchorId));
            } else {
                mPopup.setAnchorView(this);
            }
        }
        if (mAdapter.getCount() > 0) {
            mPopup.show();
            mPopup.getListView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        }
    }

    public boolean isPopupShowing() {
        return mPopup.isShowing();
    }

    public void setDropDownHorizontalOffset(int offset) {
        mPopup.setHorizontalOffset(offset);
    }

    public void setDropDownVerticalOffset(int offset) {
        mPopup.setVerticalOffset(offset);
    }

    /**
     * invalidate data for auto suggest
     */
    public void setSuggestData(ArrayList<Description> data) {
        try {
            if (mAdapter != null) {
                mAdapter.clearAllData();
                mAdapter.addData(data);
                mAdapter.notifyDataSetChanged();
            } else {
                mAdapter = new CodeSuggestAdapter(getContext(), R.layout.list_item_suggest, data);
                setAdapter(mAdapter);
            }
            if (data.size() > 0) {
                onPopupChangePosition();
                onDropdownChangeSize(getWidth(), getHeight());
                showDropDown();

            } else if (data.size() == 0) {
                dismissDropDown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends ListAdapter & Filterable> void setAdapter(CodeSuggestAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {
            //noinspection unchecked
            mFilter = mAdapter.getFilter();
        } else {
            mFilter = null;
        }

        mPopup.setAdapter(mAdapter);
    }

    public void setLineError(@Nullable LineInfo lineError) {
        this.mLineErrors.clear();
        if (lineError != null) {
            this.mLineErrors.add(lineError);
        }
    }

    /**
     * <p>Performs the text completion by converting the selected item from
     * the drop down list into a string, replacing the text box's content with
     * this string and finally dismissing the drop down menu.</p>
     */
    public void performCompletion() {
        performCompletion(null, -1, -1);
    }

    private void performCompletion(View selectedView, int position, long id) {
        if (isPopupShowing()) {
            Object selectedItem;
            if (position < 0) {
                selectedItem = mPopup.getSelectedItem();
            } else {
                selectedItem = mAdapter.getItem(position);
            }
            if (selectedItem == null) {
                DLog.w(TAG, "performCompletion: no selected item");
                return;
            }

            mBlockCompletion = true;
            try {
                replaceText(convertSelectionToString(selectedItem));
            } catch (Exception ignored) {
                //something wrong when start app
                ignored.printStackTrace();
            }
            mBlockCompletion = false;

            if (mItemClickListener != null) {
                final ListPopupWindow list = mPopup;

                if (selectedView == null || position < 0) {
                    selectedView = list.getSelectedView();
                    position = list.getSelectedItemPosition();
                    id = list.getSelectedItemId();
                }
                mItemClickListener.onItemClick(list.getListView(), selectedView, position, id);
            }
        }

        if (mDropDownDismissedOnCompletion) {
            dismissDropDown();
        }
    }

    /**
     * <p>Converts the selected item from the drop down list into a sequence
     * of character that can be used in the edit box.</p>
     *
     * @param selectedItem the item selected by the user for completion
     * @return a sequence of characters representing the selected suggestion
     */
    protected CharSequence convertSelectionToString(Object selectedItem) {
        return mFilter.convertResultToString(selectedItem);
    }

    protected void replaceText(CharSequence text) {
        clearComposingText();

        int end = getSelectionEnd();
        int start;
        if (SymbolsTokenizer.TOKEN.contains(text.toString().trim())) {
            start = end;
        } else {
            start = mTokenizer.findTokenStart(getText(), end);
        }
        start = Math.max(start, 0);
        end = Math.max(end, 0);
        Editable editable = getText();
        String original = TextUtils.substring(editable, start, end);

        QwertyKeyListener.markAsReplaced(editable, start, end, original);
        editable.replace(start, end, mTokenizer.terminateToken(text));
    }

    /**
     * <p>Closes the drop down if present on screen.</p>
     */
    public void dismissDropDown() {
        mPopup.dismiss();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean consumed = mPopup.onKeyUp(keyCode, event);
        if (consumed) {
            switch (keyCode) {
                // if the list accepts the key events and the key event
                // was a click, the text view gets the selected item
                // from the drop down as its content
                case KeyEvent.KEYCODE_ENTER:
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        performCompletion();
                    }
                    return true;
            }
        }

        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            performCompletion();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!hasFocus()) {
            requestFocus();
        }
        if (mPopup.onKeyDown(keyCode, event)) {
            return true;
        }

        if (!isPopupShowing()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        performValidation();
                    }
            }
        }

        if (isPopupShowing() && keyCode == KeyEvent.KEYCODE_TAB && event.hasNoModifiers()) {
            return true;
        }

        mLastKeyCode = keyCode;
        boolean handled = super.onKeyDown(keyCode, event);
        mLastKeyCode = KeyEvent.KEYCODE_UNKNOWN;

        if (handled && isPopupShowing()) {
            clearListSelection();
        }

        return handled;
    }

    /**
     * <p>Clear the list selection.  This may only be temporary, as user input will often bring
     * it back.
     */
    public void clearListSelection() {
        mPopup.clearListSelection();
    }

    /**
     * Instead of validating the entire text, this subclass method validates
     * each token of the text individually.  Empty tokens are removed.
     */
    public void performValidation() {

    }


    @Override
    protected void onDetachedFromWindow() {
        DLog.d(TAG, "onDetachedFromWindow() called");
        if (mPopup.isShowing()) {
            mPopup.dismiss();
        }
        super.onDetachedFromWindow();
    }

    public void setSrcPath(@NonNull String mSrcPath) {
        if (mSrcPath.contains(".")) {
            mSrcPath = mSrcPath.substring(0, mSrcPath.indexOf("."));
        }
        this.mSrcName = mSrcPath;
    }

    public static class SymbolsTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        static final String TOKEN = "!@#$%^&*()_+-={}|[]:;'<>/<.,? \r\n\t";

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

    private class DropDownItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            performCompletion(v, position, id);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParseDataTask extends AsyncTask<Object, Object, ArrayList<Description>> {
        private String source;
        @NonNull
        private String srcName;
        private SuggestOperation pascalParserHelper;
        private int cursorPos, cursorLine, cursorCol;

        private ParseDataTask(EditText editText, @NonNull String srcName) {
            this.source = editText.getText().toString();
            this.srcName = srcName;
            this.pascalParserHelper = new SuggestOperation();
            calculateCursor(editText);
        }

        @SuppressWarnings("ConstantConditions")
        private void calculateCursor(EditText editText) {
            this.cursorPos = editText.getSelectionStart();
            Pair<Integer, Integer> lineColFromIndex = LineUtils.getLineColFromIndex(cursorPos,
                    editText.getLayout().getLineCount(), editText.getLayout());
            this.cursorLine = lineColFromIndex.first;
            this.cursorCol = lineColFromIndex.second;
        }

        @Override
        protected ArrayList<Description> doInBackground(Object... params) {
            if (!isCancelled()) {
                return pascalParserHelper.getSuggestion(srcName, source, cursorPos, cursorLine, cursorCol);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(ArrayList<Description> result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                DLog.d(TAG, "onPostExecute: cancel");
                return;
            }
            if (result == null) {
                setSuggestData(new ArrayList<Description>());
            } else {
                setSuggestData(result);
            }
            if (pascalParserHelper.getParsingException() != null) {
                ParsingException parsingException = pascalParserHelper.getParsingException();
                setLineError(parsingException.getLineInfo());
            }
        }
    }
}

