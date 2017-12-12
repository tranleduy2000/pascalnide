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

package com.duy.pascal.ui.autocomplete.completion;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.ignore.CommentToken;
import com.duy.pascal.ui.autocomplete.completion.ast.PascalStatement;

/**
 * Created by Duy on 12-Dec-17.
 */

public class PascalCompleteConfident {
    public static boolean shouldDisplayPopup(PascalStatement statement, int line, int column) {
        return isInComment(statement, line, column);
    }


    /**
     * @param line   - current line of cursor, start at 0
     * @param column - current column of cursor, start at 0
     * @return true of cursor in comment
     */
    public static boolean isInComment(PascalStatement statement, int line, int column) {
        for (Token token : statement.getStatement()) {
            if (token instanceof CommentToken) {
                LineInfo lineNumber = token.getLineNumber();
                if (lineNumber.getLine() != line) return false;

                int length = lineNumber.getLength();
                return (column > lineNumber.getColumn()) && (column <= lineNumber.getLine());
            }
        }
        return false;
    }


}
