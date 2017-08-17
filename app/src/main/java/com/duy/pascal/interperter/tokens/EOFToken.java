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

package com.duy.pascal.interperter.tokens;

import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException.Type;
import com.duy.pascal.interperter.tokens.closing.ClosingToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.interperter.tokens.grouping.BracketedToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.grouping.ParenthesizedToken;

public class EOFToken extends ClosingToken {
    public EOFToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
//        return Character.valueOf((char) 3).toString();
        return "";
    }

    @Override
    public GroupingException getClosingException(GrouperToken t) {
        if (t instanceof ParenthesizedToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_PARENTHESES);
        } else if (t instanceof BeginEndToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_BEGIN_END);
        } else if (t instanceof BracketedToken) {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_BRACKETS);
        } else {
            return new GroupingException(t.getLineNumber(), Type.UNFINISHED_CONSTRUCT);
        }
    }

}
