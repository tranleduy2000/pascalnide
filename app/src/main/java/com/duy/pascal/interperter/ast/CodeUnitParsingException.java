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

package com.duy.pascal.interperter.ast;

import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

/**
 * Created by Duy on 9/24/2017.
 */

public class CodeUnitParsingException extends Exception {
    private final CodeUnit codeUnit;
    private final ParsingException parseException;

    public CodeUnitParsingException(CodeUnit codeUnit, ParsingException e) {
        this.codeUnit = codeUnit;
        this.parseException = e;
    }

    public CodeUnit getCodeUnit() {
        return codeUnit;
    }

    public ParsingException getParseException() {
        return parseException;
    }

    @Override
    public synchronized Throwable getCause() {
        return parseException;
    }
}
