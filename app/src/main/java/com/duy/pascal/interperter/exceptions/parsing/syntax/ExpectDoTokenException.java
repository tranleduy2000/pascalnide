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
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.exceptions.parsing.missing.MissingTokenException;
import com.duy.pascal.interperter.linenumber.LineNumber;

/**
 * Created by Duy on 31-May-17.
 */

public class ExpectDoTokenException extends MissingTokenException {
    private WrongStatementException.Statement statement;

    public ExpectDoTokenException(@Nullable LineNumber lineNumber, WrongStatementException.Statement statement) {
        super(lineNumber);
        this.statement = statement;
    }

    public WrongStatementException.Statement getStatement() {
        return statement;
    }

    @Override
    public boolean canQuickFix() {
        return true;
    }

    @NonNull
    @Override
    public String getMissingToken() {
        return "do";
    }
}
