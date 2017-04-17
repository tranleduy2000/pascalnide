package com.duy.pascal.compiler;

import android.util.Log;

import com.duy.pascal.frontend.alogrithm.AutoIndentCode;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void test1() {
        String code = "begin end";
        Log.v(TAG, "test1: " + autoIndentCode.indent(code));
    }

    @Test
    public void test2() {
        String code = "if a = 1 ten a := 2 else a := 3;";
        Log.v(TAG, autoIndentCode.indent(code));
    }

    @Test
    public void test3() {
        String code = "while a = 2 do writeln(2)";
        Log.v(TAG, autoIndentCode.indent(code));
    }

    @Test
    public void testRepeatUntil() {
        String code = "repeat writeln(1) until false;";
        Log.v(TAG, autoIndentCode.indent(code));
    }
}
