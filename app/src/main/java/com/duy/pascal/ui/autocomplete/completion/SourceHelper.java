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

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.other.EOFToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.AssignmentToken;
import com.duy.pascal.interperter.tokens.basic.DoToken;
import com.duy.pascal.interperter.tokens.basic.DowntoToken;
import com.duy.pascal.interperter.tokens.basic.ForToken;
import com.duy.pascal.interperter.tokens.basic.SemicolonToken;
import com.duy.pascal.interperter.tokens.basic.ToToken;
import com.duy.pascal.interperter.tokens.closing.EndToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.interperter.tokens.value.ValueToken;
import com.duy.pascal.ui.autocomplete.completion.ast.PascalStatement;
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
    public static PascalStatement getStatement(LinkedList<Token> source, int line, int column) {
        DLog.d(TAG, "getStatement() called with: line = [" + line + "], column = [" + column + "]");

        LineNumber cursor = new LineNumber(line, column, "");
        int last = -1;
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).getLineNumber().compareTo(cursor) < 0) {
                last = i;
            } else {
                break;
            }
        }
        Token separator = null;
        int index = last;
        while (index >= 0) {
            if (isStatementSeparator(source.get(index))) {
                separator = source.get(index);
                index++;
                break;
            } else {
                index--;
            }
        }
        if (index < 0) index = 0;
        List<Token> tokens = source.subList(index, last + 1);
        if (tokens.size() >= 1 && tokens.get(tokens.size() - 1) instanceof EOFToken) {
            tokens.remove(tokens.size() - 1);
        }
        PascalStatement pascalStatement = new PascalStatement(tokens, separator);
        DLog.d(TAG, "getStatement() returned: " + pascalStatement);
        return pascalStatement;
    }

    private static boolean isStatementSeparator(Token token) {
        return token instanceof EndToken || token instanceof BeginEndToken
                /*|| token instanceof VarToken || token instanceof UsesToken
                || token instanceof ConstToken || token instanceof ProgramToken*/
                || token instanceof SemicolonToken;
    }

    /**
     * Syntax
     * For {@link WordToken} := {@link ValueToken} [To|Downto] {@link ValueToken} do
     */
    public static int isForNumberStructure(List<Token> tokens) {
        for (int i = 0, tokensSize = tokens.size(); i < tokensSize; i++) {
            Token token = tokens.get(i);
            switch (i) {
                case 0:
                    if (!(token instanceof ForToken)) return -1;
                    break;
                case 1:
                    if (!(token instanceof WordToken || token instanceof ValueToken)) return -1;
                    break;
                case 2:
                    if (!(token instanceof AssignmentToken)) return -1;
                    break;
                case 3:
                    if (!(token instanceof WordToken || token instanceof ValueToken)) return -1;
                    break;
                case 4:
                    if (!(token instanceof ToToken || token instanceof DowntoToken)) return -1;
                    break;
                case 5:
                    if (!(token instanceof WordToken)) return -1;
                    break;
                case 6:
                    if (!(token instanceof DoToken)) return -1;
                    break;
                default:
                    return i;
            }
        }
        return tokens.size();
    }

}
