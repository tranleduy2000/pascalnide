package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.data.KeyWord;

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
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> list = new ArrayList<>();

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
    public void invalidateKeyWord(String source) {
        list.clear();
        Collections.addAll(list, KeyWord.LIST_KEY_WORD);
        String[] words = source.split("[^a-zA-Z']+");
        Collections.addAll(list, words);
        mAdapter = new ArrayAdapter<>(getContext(), R.layout.code_hint, R.id.txt_title, list);
        setAdapter(mAdapter);
    }

    public void addKeyWord(String key) {
        list.add(key);
    }

    public void removeKeyWord(String key) {
        list.remove(key);
    }

    private void init() {
        Log.i(TAG, "init: ");
        invalidateKeyWord("");
        setTokenizer(new SymbolsTokenizer());
        setThreshold(1);
        invalidateCharHeight();
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

    public class SymbolsTokenizer implements MultiAutoCompleteTextView.Tokenizer {
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

