/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.debugable;

import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.duy.pascal.backend.linenumber.LineInfo;

/**
 * Created by Duy on 29-Mar-17.
 */

public class StackFunction {
    /**
     * max stack size
     */
    public static final int MAX_STACK = 85;

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
            throw new StackOverflowException(lineInfo, "Stack size " + CURRENT_STACK +
                    "| Max stack size: " + MAX_STACK);
        }
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

