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

package com.duy.pascal.interperter.tokens.ignore;

import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

/**
 * Comment token
 * Created by Duy on 21-Mar-17.
 */
public class CommentToken extends Token {
    public String comment;


    public CommentToken(LineNumber line, String cmt) {
        super(line);
        this.comment = cmt;
    }

    @Override
    public String toString() {
//        if (comment.endsWith())
//            return comment;
//        else {
        return comment;
//        }
    }
}
