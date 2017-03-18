package com.duy.pascal.compiler.manager;

import android.util.Log;

import com.duy.pascal.compiler.BuildConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.compiler.data.KeyWordAndPattern.readln;
import static com.duy.pascal.compiler.data.KeyWordAndPattern.uses;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CodeManager {
    private static final String TAG = CodeManager.class.getSimpleName();
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static String normalCode(String code_) {
        if (code_.isEmpty()) return code_;
        for (Matcher m = uses.matcher(code_); m.find(); ) {
            code_ = code_.substring(0, m.start()) + code_.substring(m.end(), code_.length());
        }
        Matcher matcher = readln.matcher(code_);
        while (matcher.find()) {
            code_ = code_.substring(0, matcher.start()) + "waitEnter;" + code_.substring(matcher.end(), code_.length());
            matcher = readln.matcher(code_);
        }
        if (DEBUG) {
            Log.d(TAG, "normalCode: " + code_);
        }
        return code_;
    }

    public static String localCode(String code) {
        code = code.replaceAll(Pattern.quote("waitEnter"), "readln");
        return code;
    }


}
