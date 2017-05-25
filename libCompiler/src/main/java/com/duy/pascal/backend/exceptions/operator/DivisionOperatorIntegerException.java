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

package com.duy.pascal.backend.exceptions.operator;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime.exception.internal.InternalInterpreterException;

/**
 * Created by Duy on 01-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class DivisionOperatorIntegerException extends InternalInterpreterException {
    public DivisionOperatorIntegerException(LineInfo line) {
        super(line);
    }

    @Override
    public String getInternalError() {
        return "Can not uses / (division) operator with integer";
    }

    @Override
    public String getMessage() {
        return getInternalError();
    }
}
