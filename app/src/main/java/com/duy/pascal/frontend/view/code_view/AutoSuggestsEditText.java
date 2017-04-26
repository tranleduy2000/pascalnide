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

package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.data.KeyWord;
import com.duy.pascal.frontend.program_structure.viewholder.StructureType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * AutoSuggestsEditText
 * show hint when typing
 * Created by Duy on 28-Feb-17.
 */

public abstract class AutoSuggestsEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {
    private static final String TAG = AutoSuggestsEditText.class.getName();
    public int mCharHeight = 0;
    private ArrayList<Character> openBracketList = new ArrayList<>();
    private ArrayList<String> closeBracketList = new ArrayList<>();
    private CodeSuggestAdapter mAdapter;

    public AutoSuggestsEditText(Context context) {
        super(context);
        init();
    }

    public AutoSuggestsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoSuggestsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * slipt string in edittext and put it to list keyword
     */
    public void invalidateKeyWord() {
        setSuggestData(new ArrayList<String>());
    }


    private void init() {
        Log.i(TAG, "init: ");
        invalidateKeyWord();
        setTokenizer(new SymbolsTokenizer());
        setThreshold(1);
        invalidateCharHeight();
        openBracketList = new ArrayList<>();
        Collections.addAll(openBracketList, '[', '{', '\'', '(');
        Collections.addAll(closeBracketList, "]", "}", "'", ")");
        setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start,
                                               int end, Spanned dest, int dstart, int dend) {
                        if (end - start == 1 && start < source.length() &&
                                dstart < dest.length()) {
                            Character c = source.charAt(start);
                            if (c == '\n')
                                return autoIndent(source, start, end, dest, dstart, dend);
                        }
                        return source;
                    }
                }
        });

        //auto add bracket
        addTextChangedListener(new TextWatcher() {
            private int start, end, count;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > start
                        && count == 1 && openBracketList.contains(s.charAt(start))) {
                    CharSequence textToInsert = getBracket(s, start);
                    try {
                        s.insert(start + 1, textToInsert);
                        setSelection(start);
                    } catch (Exception ignored) {

                    }
                }
            }
        });

    }


    private CharSequence getBracket(CharSequence source, int index) {
        if (source.charAt(index) == '\'') {
            if (index > 0 && index < source.length()) {
                if (source.charAt(index - 1) == '\'') {
                    return "";
                } else {
                    return "'";
                }
            }
        } else {
            Character character = source.charAt(index);
            if (openBracketList.contains(character)) {
                return closeBracketList.get(openBracketList.indexOf(character));
            }
        }
        return "";
    }

    private CharSequence autoIndent(CharSequence source,
                                    int start, int end, Spanned dest, int dstart, int dend) {
        String indent = "";
        int indexStart = dstart - 1;
        int indexEnd = -1;
        boolean dataBefore = false;
        int parenthesesCount = 0;

        for (; indexStart > -1; --indexStart) {
            char c = dest.charAt(indexStart);
            if (c == '\n')
                break;
            if (c != ' ' && c != '\t') {
                if (!dataBefore) {
                    if (c == '{' ||
                            c == '+' ||
                            c == '-' ||
                            c == '*' ||
                            c == '/' ||
                            c == '%' ||
                            c == '^' ||
                            c == '=')
                        --parenthesesCount;
                    dataBefore = true;
                }
                if (c == '(')
                    --parenthesesCount;
                else if (c == ')')
                    ++parenthesesCount;
            }
        }
        if (indexStart > -1) {
            char charAtCursor = dest.charAt(dstart);
            for (indexEnd = ++indexStart; indexEnd < dend; ++indexEnd) {
                char c = dest.charAt(indexEnd);
                if (charAtCursor != '\n' && c == '/' && indexEnd + 1 < dend && dest.charAt(indexEnd) == c) {
                    indexEnd += 2;
                    break;
                }
                if (c != ' ' && c != '\t')
                    break;
            }
            indent += dest.subSequence(indexStart, indexEnd);
        }
        if (parenthesesCount < 0)
            indent += "\t";
        return source + indent;
    }

    private void invalidateCharHeight() {
        mCharHeight = (int) Math.ceil(getPaint().getFontSpacing());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        onPopupSuggestChangeSize();
    }

    public abstract void onPopupSuggestChangeSize();

    /**
     * invalidate data for auto suggest
     */
    public void setSuggestData(ArrayList<String> data) {
        if (mAdapter == null) {
            mAdapter = new CodeSuggestAdapter(getContext(), R.layout.code_hint);
            setAdapter(mAdapter);
        }
        ArrayList<SuggestItem> list = new ArrayList<>();
        for (String s : KeyWord.LIST_KEY_WORD) {
            list.add(new SuggestItem(StructureType.TYPE_KEY_WORD, s));
        }
        for (String s : data) {
            list.add(new SuggestItem(StructureType.TYPE_UNKNOWN, s));
        }
        mAdapter.setSource(list);
        mAdapter.notifyDataSetChanged();
    }

    public void addSuggestData(ArrayList<String> data) {

    }

    public void clearKeyword() {
        mAdapter.clear();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setDropDownWidth(w / 2);
        setDropDownHeight(h / 2);
    }

    private class SymbolsTokenizer implements MultiAutoCompleteTextView.Tokenizer {
        String token = "!@#$%^&*()_+-={}|[]:;'<>/<.?1234567890 \n\t";

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
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                    return sp;
                } else {
                    return text;
                }
            }
        }
    }
}

