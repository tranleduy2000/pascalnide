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

package com.duy.pascal.backend.exceptions.syntax;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.Token;

/**
 * Created by Duy on 07-May-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class WrongIfElseStatement extends ParsingException {
    public WrongIfElseStatement(Token next) {
        super(next.getLineInfo());
    }

    @Override
    public String getMessage() {
        return "if condition then S1 else S2;\n" +
                "Where, S1 and S2 are different statements. Please note that the statement S1 is not followed by a semicolon.";

    }
}
