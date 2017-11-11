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
import com.duy.pascal.interperter.tokens.basic.SemicolonToken;
import com.duy.pascal.interperter.tokens.closing.EndToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.ui.utils.DLog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Duy on 11/11/2017.
 */

public class SourceHelper {
    private static final String TAG = "SourceHelper";

    /**
     * Search back from the cursor position till meeting 'begin', 'end' or ';'.
     */
    public static List<Token> getStatement(LinkedList<Token> source, int line, int column) {
        LineInfo cursor = new LineInfo(line, column, "");
        int last = 0;
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).getLineNumber().compareTo(cursor) < 0) {
                last = i;
            } else {
                break;
            }
        }
        int index = last;
        while (index >= 0) {
            if (isStatementSeparator(source.get(index))) {
                index++;
                break;
            } else {
                index--;
            }
        }
        if (index < 0) index = 0;
        List<Token> tokens = source.subList(index, last + 1);
        DLog.d(TAG, "getStatement() returned: " + tokens);
        return tokens;
    }

    private static boolean isStatementSeparator(Token token) {
        return token instanceof EndToken || token instanceof BeginEndToken
                /*|| token instanceof VarToken || token instanceof UsesToken
                || token instanceof ConstToken || token instanceof ProgramToken*/
                || token instanceof SemicolonToken;
    }
}
