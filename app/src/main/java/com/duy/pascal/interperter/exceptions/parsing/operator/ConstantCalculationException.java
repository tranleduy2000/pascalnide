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

package com.duy.pascal.interperter.exceptions.parsing.operator;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import android.support.annotation.NonNull;

public  class ConstantCalculationException extends ParsingException {
    @NonNull
    private RuntimePascalException e;

    public ConstantCalculationException(@NonNull RuntimePascalException e) {
        super(e.line, "Error while computing constant value: " + e.getMessage());
        this.e = e;
    }

    @NonNull
    public final RuntimePascalException getE() {
        return this.e;
    }

    public final void setE(@NonNull RuntimePascalException var1) {
        this.e = var1;
    }
}
