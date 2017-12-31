/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.interperter.utils;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.runtime.value.NullValue;

/**
 * Created by Duy on 13-Jun-17.
 */

public class NullSafety {
    public static Object zReturn(@Nullable Object o) {
        if (o == null) {
            return NullValue.get();
        }
        return o;
    }

    public static boolean isNullValue(Object value) {
        return value == null || value instanceof NullValue;
    }

    public static boolean isNullPointer(Object value) {
        return value == null ;
    }
}
