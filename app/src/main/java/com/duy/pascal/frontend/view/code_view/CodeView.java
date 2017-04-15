package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Duy on 15-Mar-17.
 */

public class CodeView extends KeyBoardFilterEditText {
    private static final String TAG = CodeView.class.getSimpleName();

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CodeView(Context context) {
        super(context);

    }
    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
