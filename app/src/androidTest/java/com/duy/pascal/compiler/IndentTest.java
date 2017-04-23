package com.duy.pascal.compiler;

import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.MaskFilterSpan;

import com.duy.pascal.backend.tokenizer.AutoIndentCode;

import org.junit.Before;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;

/**
 * Created by Duy on 17-Apr-17.
 */

public class IndentTest {
    public static final String TAG = "IndentTest";
    private AutoIndentCode autoIndentCode;

    @Before
    public void setup() {
        autoIndentCode = new AutoIndentCode();
    }


}
