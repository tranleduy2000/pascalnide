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

package com.duy.pascal.interperter.exceptions.parsing.syntax;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 9/28/2017.
 */

public class WrongForStatementException extends ParsingException {
    private Type type;

    public WrongForStatementException(@NonNull LineNumber lineNumber, Type type) {
        super(lineNumber);
        this.type = type;
    }

    @Override
    public String getMessage() {
        return getLineNumber() + "\n" +
                "Syntax error: wrong for statement";
    }

    public enum Type {
        EXPECTED_DO, EXPECTED_TO
    }
}
