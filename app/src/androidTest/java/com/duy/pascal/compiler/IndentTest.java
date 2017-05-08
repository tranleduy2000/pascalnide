package com.duy.pascal.compiler;

import com.duy.pascal.backend.tokenizer.AutoIndentCode;

import org.junit.Before;

/**
 * Created by Duy on 17-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class IndentTest {
    public static final String TAG = "IndentTest";
    private AutoIndentCode autoIndentCode;

    @Before
    public void setup() {
        autoIndentCode = new AutoIndentCode();
    }


}
