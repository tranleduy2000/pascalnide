package com.duy.pascal.compiler.view.code_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.duy.pascal.compiler.R;

/**
 * Created by Duy on 12-Mar-17.
 */

public class CodeThemeUtils extends HighlightEditor {
    public CodeThemeUtils(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public CodeThemeUtils(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);

    }

    public CodeThemeUtils(Context context) {
        super(context);
        init(context, null, -1);
    }

    public void init(Context context, AttributeSet attributeSet, int def) {
        TypedArray typedArray = context.obtainStyledAttributes(R.style.CodeTheme_Default,
                R.styleable.CodeTheme);
        typedArray.getInteger(R.styleable.CodeTheme_bg_editor_color,
                R.color.bg_editor_color);
        COLOR_ERROR = typedArray.getInteger(R.styleable.CodeTheme_error_color,
                R.color.error_color);
        COLOR_NUMBER = typedArray.getInteger(R.styleable.CodeTheme_number_color,
                R.color.number_color);
        COLOR_KEYWORD = typedArray.getInteger(R.styleable.CodeTheme_key_word_color,
                R.color.key_word_color);
        COLOR_COMMENT = typedArray.getInteger(R.styleable.CodeTheme_comment_color,
                R.color.comment_color);
        COLOR_STRINGS = typedArray.getInteger(R.styleable.CodeTheme_string_color,
                R.color.string_color);
        COLOR_BOOLEANS = typedArray.getInteger(R.styleable.CodeTheme_boolean_color,
                R.color.boolean_color);
        COLOR_OPT = typedArray.getInteger(R.styleable.CodeTheme_opt_color,
                R.color.opt_color);
//        setBackgroundColor(resources.getColor(R.color.bg_editor_color));
    }
}
