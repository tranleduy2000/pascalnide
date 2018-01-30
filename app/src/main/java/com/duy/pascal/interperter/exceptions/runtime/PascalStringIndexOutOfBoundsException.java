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

package com.duy.pascal.interperter.exceptions.runtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;

import com.duy.pascal.ui.R;

/**
 * Created by Duy on 13-Dec-17.
 */

public class PascalStringIndexOutOfBoundsException extends RuntimePascalException {
    private int index;

    /**
     * Constructs a new {@code PascalStringIndexOutOfBoundsException}
     * class with an argument indicating the illegal index.
     *
     * @param index the illegal index.
     */
    public PascalStringIndexOutOfBoundsException(int index) {
        super("String index out of range: " + index);
        this.index = index;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        return new SpannableString(context.getString(R.string.string_index_out_of_range) + index);
    }
}
