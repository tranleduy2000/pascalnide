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

package com.duy.pascal.interperter.exceptions.parsing.define;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.utils.ArrayUtil;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.ExceptionManager;

import java.util.ArrayList;

/**
 * Created by Duy on 11/15/2017.
 */

public class UnknownFunctionException extends ParsingException {
    private final WordToken name;
    private final ArrayList<String> argsType;
    private final ExpressionContext scope;

    public UnknownFunctionException(WordToken name, ArrayList<String> argsType, ExpressionContext scope) {
        super(name.getLineNumber());
        this.name = name;
        this.argsType = argsType;
        this.scope = scope;
    }

    @Override
    public String getMessage() {
        String params = ArrayUtil.argToString(argsType.toArray(), false);
        return "Can not resolve function or procedure '" + name + "(" + params + ")'";
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        String params = ArrayUtil.argToString(argsType.toArray(), false);
        String str = context.getString(R.string.UnknownFunctionException, name.toString(), params);
        return ExceptionManager.highlight(context, new SpannableString(str));
    }
}
