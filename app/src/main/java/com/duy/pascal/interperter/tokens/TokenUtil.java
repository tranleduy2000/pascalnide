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

import com.duy.pascal.interperter.tokens.basic.ConstToken;
import com.duy.pascal.interperter.tokens.basic.ForToken;
import com.duy.pascal.interperter.tokens.basic.FunctionToken;
import com.duy.pascal.interperter.tokens.basic.IfToken;
import com.duy.pascal.interperter.tokens.basic.ProgramToken;
import com.duy.pascal.interperter.tokens.basic.RepeatToken;
import com.duy.pascal.interperter.tokens.basic.UsesToken;
import com.duy.pascal.interperter.tokens.basic.VarToken;
import com.duy.pascal.interperter.tokens.basic.WhileToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.interperter.tokens.grouping.CaseToken;

/**
 * Created by Duy on 17-Aug-17.
 */

public class TokenUtil {
    public static final Class[] START_OF_STATEMENTS = new Class[]{
            BeginEndToken.class, IfToken.class, ForToken.class,
            CaseToken.class, RepeatToken.class, WhileToken.class, VarToken.class,
            ConstToken.class, UsesToken.class, ProgramToken.class,
            FunctionToken.class, ProgramToken.class
    };

    public static boolean isStartStatement(Token t) {
        for (Class statement : START_OF_STATEMENTS) {
            if (statement.getClass().getName().equals(t.getClass().getName())) return true;
        }
        return false;
    }
}
