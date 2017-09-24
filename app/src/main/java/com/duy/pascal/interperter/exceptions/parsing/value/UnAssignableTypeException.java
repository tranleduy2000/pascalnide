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

package com.duy.pascal.interperter.exceptions.parsing.value;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;


public  class UnAssignableTypeException extends ParsingException {
    @NonNull
    private RuntimeValue runtimeValue;

    public UnAssignableTypeException(@NonNull RuntimeValue runtimeValue) {
        super(runtimeValue.getLineNumber(), "The expression " + runtimeValue + " cannot have a value assigned to it.");
        this.runtimeValue = runtimeValue;
    }

    public boolean getCanAutoFix() {
        return true;
    }

    @NonNull
    public final RuntimeValue getRuntimeValue() {
        return this.runtimeValue;
    }

    public final void setRuntimeValue(@NonNull RuntimeValue var1) {
        this.runtimeValue = var1;
    }
}
