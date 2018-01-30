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

package com.duy.pascal.interperter.exceptions.parsing;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.IRichFormatException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.VariableExpectedException;
import com.duy.pascal.interperter.linenumber.ISourcePosition;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.OperatorToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.AssignmentToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;


public class ParsingException extends Exception implements IRichFormatException, ISourcePosition {
    @Nullable
    private final LineInfo lineNumber;

    public ParsingException(@Nullable LineInfo lineNumber, @NonNull String message) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public ParsingException(@Nullable LineInfo lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static VariableExpectedException makeVariableIdentifierExpectException(@NonNull UnknownIdentifierException e, GrouperToken group,
                                                                                  ExpressionContext context) throws Exception {
        VariableExpectedException exception = new VariableExpectedException(e);
        try {
            Token next = group.take();
            if ((next instanceof AssignmentToken || next instanceof OperatorToken)) {
                RuntimeValue firstValue = group.getNextExpression(context);
                Type declType = firstValue.getRuntimeType(context).declType;
                exception.setExpectedType(declType);
                return exception;
            }
        } catch (Exception ignored) {
        }
        return exception;
    }

    @Nullable
    public LineInfo getLineNumber() {
        return this.lineNumber;
    }

    @NonNull
    public String toString() {
        return this.lineNumber + ":" + this.getMessage();
    }

    public boolean canQuickFix() {
        return false;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        String line = formatLine(context, getLineNumber());
        String message = getLocalizedMessage();
        return new SpannableStringBuilder(line)
                .append("\n\n")
                .append(message);
    }

}
