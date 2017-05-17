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
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 26-Feb-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OperationNotSupportedException extends RuntimePascalException {
    public OperationNotSupportedException() {
    }

    public OperationNotSupportedException(LineInfo line) {
        super(line);
    }

    public OperationNotSupportedException(LineInfo line, String mes) {
        super(line, mes);
    }

    public OperationNotSupportedException(String mes) {
        super(null, mes);
    }
}
