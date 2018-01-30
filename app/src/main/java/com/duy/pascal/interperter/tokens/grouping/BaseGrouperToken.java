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

package com.duy.pascal.interperter.tokens.grouping;


import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;

public class BaseGrouperToken extends GrouperToken {

    public BaseGrouperToken(LineNumber line) {
        super(line);
    }


    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder();
        if (next != null) {
            builder.append(next.toCode());
        }
        for (Token t : this.queue) {
            builder.append(t.toCode()).append(' ');
        }
        return builder.toString();
    }


    @Override
    protected String getClosingText() {
        return "[EOF]";
    }

}
