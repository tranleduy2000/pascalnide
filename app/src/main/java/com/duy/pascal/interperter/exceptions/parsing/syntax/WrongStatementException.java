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

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.tokens.Token;

/**
 * Created by Duy on 9/24/2017.
 */

public class WrongStatementException extends ParsingException {

    private final Statement statement;
    private final Token token;

    public WrongStatementException(Statement statement, Token token) {
        super(token.getLineNumber());
        this.statement = statement;
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Syntax error, Wrong statement " + statement.statement + ". Current is " + token;
    }

    public enum Statement {
        VAR_DECLARE("var <variable_list> : <type>;"),
        CONST_DECLARE("const <identifier> = <constant_value>;"),
        FOR_TO_DO("for <variable-name> := <initial_value> to [down to] <final_value> do <statement>;"),
        WHILE_DO("while <condition> do <statement>;"),
        IF_THEN("if <condition> then <statement>;"),
        IF_THEN_ELSE("if <condition> then <statement> else <statement>;"),
        TYPE_DECLARE("type\n" +
                "record-name = record\n" +
                "   field-1: field-type1;\n" +
                "   field-2: field-type2;\n" +
                "   ...\n" +
                "   field-n: field-typen;\n" +
                "end;");

        String statement;

        Statement(String statement) {
            this.statement = statement;
        }
    }
}
