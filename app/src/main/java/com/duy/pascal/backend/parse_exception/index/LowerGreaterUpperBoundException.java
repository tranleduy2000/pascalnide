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

package com.duy.pascal.backend.parse_exception.index;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;

import org.jetbrains.annotations.NotNull;

/**
 * This exception will be throw if the first > the upper of array
 * <p>
 * <p>
 * Created by Duy on 14-Apr-17.
 */
public class LowerGreaterUpperBoundException extends ParsingException {
    private Comparable low;
    private Comparable high;
    private int size;

    public LowerGreaterUpperBoundException(Comparable low, Comparable high,
                                           @NotNull LineInfo lineInfo) {
        super(lineInfo);
        this.low = low;
        this.high = high;
    }

    @Override
    public String getMessage() {
        return getLineInfo() + "Lower greater than upper bound " + low + " > " + high;
    }

    public Comparable getLow() {
        return this.low;
    }

    public void setLow(int var1) {
        this.low = var1;
    }

    public Comparable getHigh() {
        return this.high;
    }

    public void setHigh(int var1) {
        this.high = var1;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int var1) {
        this.size = var1;
    }
}