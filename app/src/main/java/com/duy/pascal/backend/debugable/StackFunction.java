package com.duy.pascal.backend.debugable;

import android.util.Log;

import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 29-Mar-17.
 */

public class StackFunction {
    /**
     * max stack size
     */
    public static final int MAX_STACK = 500;

    /**
     * Log tag
     */
    private static final String TAG = StackFunction.class.getSimpleName();

    /**
     * current stack size
     */
    public static int CURRENT_STACK = 0;

    public static int inc(LineInfo lineInfo) throws StackOverflowException {
        CURRENT_STACK++;
        if (CURRENT_STACK > MAX_STACK) {

            throw new StackOverflowException(lineInfo);
        }
        Log.d(TAG, "inc: " + CURRENT_STACK);
        return CURRENT_STACK;
    }

    public static int dec() {
        if (CURRENT_STACK > 0) {
            CURRENT_STACK--;
        }
        return CURRENT_STACK;
    }

    public static void reset() {
        CURRENT_STACK = 0;
    }
}

