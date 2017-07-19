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

package com.duy.pascal.interperter.builtin_libraries.file.exceptions;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;

/**
 * Created by Duy on 09-Jun-17.
 */

public class PascalIOException extends RuntimePascalException {
    public PascalIOException(LineInfo line, Exception e) {
        super(line);
    }

    public PascalIOException(LineInfo line, String msg) {
        super(line);
    }

    public PascalIOException(Exception e) {
        super(e);
    }
}
