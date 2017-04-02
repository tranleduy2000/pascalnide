package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Duy on 15-Mar-17.
 */

public class CodeView extends KeyBoardFilterEditText {
    private static final String TAG = CodeView.class.getSimpleName();

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeView(Context context) {
        super(context);
        init();

    }

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        super.init();
        Log.i(TAG, "init: ");
    }
}
