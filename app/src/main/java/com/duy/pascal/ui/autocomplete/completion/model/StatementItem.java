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

package com.duy.pascal.ui.autocomplete.completion.model;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.tokens.Token;

import java.util.List;

/**
 * Created by Duy on 11/15/2017.
 */

public class StatementItem {
    /**
     * List token of statement
     */
    private final List<Token> statement;
    /**
     * Token separator statement, 'begin', ';', 'end'
     */
    @Nullable
    private final Token separator;

    public StatementItem(List<Token> statement, @Nullable Token separator) {
        this.statement = statement;
        this.separator = separator;
    }

    public List<Token> getStatement() {
        return statement;
    }


    @Nullable
    public Token getSeparator() {
        return separator;
    }


    @Override
    public String toString() {
        return "StatementItem{" +
                "statement=" + statement +
                ", separator=" + separator +
                '}';
    }
}
