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

package com.duy.pascal.interperter.exceptions.parsing.index;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;

/**
 * This exception will be throw if the first > the upper of array
 * <p>
 * <p>
 * Created by Duy on 14-Apr-17.
 */
public class LowerGreaterUpperBoundException extends ParsingException {
    @NonNull
    private final Comparable low;
    @NonNull
    private final Comparable high;

    public LowerGreaterUpperBoundException(@NonNull Comparable low, @NonNull Comparable high,
                                           @NonNull LineNumber lineNumber) {
        super(lineNumber);
        this.low = low;
        this.high = high;
    }

    @Override
    public String getMessage() {
        return getLineNumber() + "Lower greater than upper bound " + low + " > " + high;
    }

    @NonNull
    public Comparable getLow() {
        return this.low;
    }


    @NonNull
    public Comparable getHigh() {
        return this.high;
    }


    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        return formatMessageFromResource(this, context,
                R.string.SubRangeException, getHigh(), getLow());
    }
}