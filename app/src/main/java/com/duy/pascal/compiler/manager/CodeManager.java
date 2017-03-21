package com.duy.pascal.compiler.manager;

import com.google.firebase.crash.FirebaseCrash;

import java.util.regex.Matcher;

import static com.duy.pascal.compiler.data.KeyWordAndPattern.readln;
import static com.duy.pascal.compiler.data.KeyWordAndPattern.waitEnter;

/**
 * Created by Duy on 28-Feb-17.
 */

public class CodeManager {
    private static final String TAG = CodeManager.class.getSimpleName();

    public static String normalCode(String code_) {
        if (code_.isEmpty()) return code_;
//        for (Matcher m = uses.matcher(code_); m.find(); ) {
//            code_ = code_.substring(0, m.start()) + code_.substring(m.end(), code_.length());
//        }
        Matcher matcher = readln.matcher(code_);
        try {
            while (matcher.find()) {
                code_ = code_.substring(0, matcher.start()) + "waitEnter;" +
                        code_.substring(matcher.end(), code_.length());
                matcher = readln.matcher(code_);
            }
        } catch (Exception ignored) {
            FirebaseCrash.report(new Throwable("Error when parse normal code: " + code_));
        }

        return code_;
    }

    public static String localCode(String code) {
        code = code.replace(waitEnter.toString(), "readln");
//        Log.d(TAG, "localCode: " + code);
        return code;
    }


}
