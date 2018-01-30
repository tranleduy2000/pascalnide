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
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;

import java.util.ArrayList;
import java.util.List;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;


public class BadFunctionCallException extends ParsingException {
    @Nullable
    private final ArrayList<String> args;
    @NonNull
    private final Name functionName;
    @NonNull
    private final ArrayList<String> functions;
    @NonNull
    private final ExpressionContext scope;
    private final boolean functionExists;
    private final boolean argsMatch;

    public BadFunctionCallException(@NonNull LineInfo line, @NonNull Name functionName,
                                    boolean functionExists, boolean numargsMatch,
                                    @NonNull ArrayList<String> args,
                                    @NonNull ArrayList<String> functions, @NonNull ExpressionContext scope) {
        super(line);
        this.functionName = functionName;
        this.functionExists = functionExists;
        this.functions = functions;
        this.scope = scope;
        this.argsMatch = numargsMatch;
        this.args = args;
    }

    public boolean isArgMatched() {
        return this.argsMatch;
    }

    @Nullable
    public String getMessage() {
        return this.functionExists ? (this.argsMatch ? "One or more arguments has an incorrect type when calling function \"" + this.functionName + "\"." : "Either too few or two many arguments are being passed to function \"" + this.functionName + "\".") : "Can not call function or procedure \"" + this.functionName + "\", which is not defined.";
    }

    @NonNull
    public final Name getFunctionName() {
        return this.functionName;
    }

    public final boolean getFunctionExists() {
        return this.functionExists;
    }

    @NonNull
    public final ArrayList<String> getFunctions() {
        return this.functions;
    }

    @NonNull
    public final ExpressionContext getScope() {
        return this.scope;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        BadFunctionCallException e = this;
        boolean functionExists = e.getFunctionExists();
        boolean argsMatch = e.isArgMatched();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(formatLine(context, e.getLineNumber())).append("\n\n");
        if (functionExists) { //function is exist, but wrong argument

            if (argsMatch) { //wrong type
                String msg = String.format(context.getString(R.string.BadFunctionCallException_1), e.getFunctionName());
                builder.append(msg);
            } else { //wrong size of args
                String msg = String.format(context.getString(R.string.BadFunctionCallException_2), e.getFunctionName());
                builder.append(msg);
            }

            //add list function
            List<String> functions = e.getFunctions();
            builder.append("\n\n");
            builder.append("Accept functions: ").append("\n");
            for (String function : functions) builder.append(function).append("\n");
            return highlight(context, builder);
        } else {
            String msg = String.format(context.getString(R.string.BadFunctionCallException_3), e.getFunctionName());
            builder.append(msg);
            return highlight(context, builder);
        }
    }
}
