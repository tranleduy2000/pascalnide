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

package com.duy.pascal.ui.code;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;

import com.duy.pascal.interperter.exceptions.Localized;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.define.BadFunctionCallException;
import com.duy.pascal.interperter.exceptions.parsing.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.MainProgramNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.define.MultipleDefaultValuesException;
import com.duy.pascal.interperter.exceptions.parsing.define.MultipleDefinitionsMainException;
import com.duy.pascal.interperter.exceptions.parsing.define.OverridingFunctionBodyException;
import com.duy.pascal.interperter.exceptions.parsing.define.TypeIdentifierExpectException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.VariableExpectedException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.StrayCharacterException;
import com.duy.pascal.interperter.exceptions.parsing.index.LowerGreaterUpperBoundException;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;
import com.duy.pascal.interperter.exceptions.parsing.index.NonIntegerIndexException;
import com.duy.pascal.interperter.exceptions.parsing.io.LibraryNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingCommaTokenException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingSemicolonTokenException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingTokenException;
import com.duy.pascal.interperter.exceptions.parsing.operator.BadOperationTypeException;
import com.duy.pascal.interperter.exceptions.parsing.operator.ConstantCalculationException;
import com.duy.pascal.interperter.exceptions.parsing.operator.DivisionByZeroException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.NotAStatementException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.WrongIfElseStatement;
import com.duy.pascal.interperter.exceptions.parsing.value.ChangeValueConstantException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonConstantExpressionException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonIntegerException;
import com.duy.pascal.interperter.exceptions.parsing.value.UnAssignableTypeException;
import com.duy.pascal.interperter.exceptions.runtime.InvalidNumericFormatException;
import com.duy.pascal.interperter.exceptions.runtime.MethodCallException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.StackOverflowException;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.libraries.exceptions.CanNotReadVariableException;
import com.duy.pascal.interperter.libraries.file.exceptions.FileException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.Patterns;

import java.util.regex.Matcher;

/**
 * Created by Duy on 11-Mar-17.
 */


public class ExceptionManager {
    public static final String TAG = ExceptionManager.class.getSimpleName();
    private Context mContext;

    public ExceptionManager(Context context) {
        this.mContext = context;
    }

    /**
     * Highlight text between " and "
     */
    public static Spannable highlight(Context context, Spannable span) {
        Matcher matcher = Patterns.REPLACE_HIGHLIGHT_PATTERN.matcher(span);
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        while (matcher.find()) {
            span.setSpan(new ForegroundColorSpan(color), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    /**
     * Highlight text between " and "
     */
    public static Spannable highlight(Context context, String spannable) {
        return highlight(context, new SpannableString(spannable));
    }

    public static Spanned getMessageFromResource(Throwable e, Context context, int resourceID, Object... arg) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String message = context.getString(resourceID, arg);
        if (e instanceof ParsingException) {
            builder.append(formatLine(context, ((ParsingException) e).getLineInfo()));
            builder.append("\n\n");
            builder.append(message);
            return highlight(context, builder);
        } else if (e instanceof RuntimePascalException) {
            builder.append(formatLine(context, ((RuntimePascalException) e).getLineNumber()));
            builder.append("\n\n");
            builder.append(message);
            return highlight(context, builder);
        }
        return new SpannableString(e.getLocalizedMessage());
    }

    @NonNull
    public static String formatLine(Context context, @Nullable LineInfo lineInfo) {
        if (lineInfo != null) {
            return context.getString(R.string.line_column, lineInfo.getLine(), lineInfo.getColumn());
        }
        return "";
    }

    public Spanned getMessage(@Nullable Throwable e) {
        if (e == null) {
            return new SpannableString("null");
        }
        try {
            if (e instanceof ExpectedTokenException
                    || e instanceof UnrecognizedTokenException
                    || e instanceof BadFunctionCallException
                    || e instanceof MethodCallException
                    || e instanceof NonIntegerIndexException
                    || e instanceof NonIntegerException
                    || e instanceof StackOverflowException
                    || e instanceof BadOperationTypeException
                    || e instanceof FileException
                    || e instanceof ConstantCalculationException
                    || e instanceof UnConvertibleTypeException) {
                return ((Localized) e).getLocalizedMessage(mContext);
            }

            if (e instanceof MissingSemicolonTokenException) {
                return getMessageFromResource(e, R.string.MissingSemicolonTokenException,
                        ((MissingSemicolonTokenException) e).getLineInfo().getLine());
            } else if (e instanceof MissingCommaTokenException) {
                return getMessageFromResource(e, R.string.MissingCommaTokenException,
                        ((MissingCommaTokenException) e).getLineInfo().getLine());
            } else if (e instanceof MissingTokenException) {
                return getMessageFromResource(e, R.string.MissingTokenException,
                        ((MissingTokenException) e).getMissingToken());
            }
            if (e instanceof StrayCharacterException)
                return getMessageFromResource(e, R.string.StrayCharacterException,
                        ((StrayCharacterException) e).getCharCode());
            if (e instanceof UnknownIdentifierException) {
                return getMessageFromResource(e, R.string.NoSuchFunctionOrVariableException, ((UnknownIdentifierException) e).getName());
            }
            if (e instanceof VariableExpectedException) {
                return getMessageFromResource(e, R.string.VariableIdentifierExpectException, ((VariableExpectedException) e).getName().getOriginName());
            }
            if (e instanceof MultipleDefinitionsMainException) {
                return getMessageFromResource(e, mContext, R.string.multi_define_main);
            }
            if (e instanceof GroupingException) {
                return ((GroupingException) e).getLocalizedMessage(mContext);
            }
            if (e instanceof MainProgramNotFoundException) {
                return getMessageFromResource(e, (R.string.main_program_not_define));
            }
            if (e instanceof ChangeValueConstantException) {
                String name = ((ChangeValueConstantException) e).getConst().getName().getOriginName();
                return getMessageFromResource(e, R.string.ChangeValueConstantException2, name);
            }
            if (e instanceof LibraryNotFoundException) {
                return getMessageFromResource(e, R.string.LibraryNotFoundException,
                        ((LibraryNotFoundException) e).getName());
            }
            if (e instanceof MultipleDefaultValuesException) {
                return getMessageFromResource(e, R.string.MultipleDefaultValuesException);
            }
            if (e instanceof NonArrayIndexed) {
                return getMessageFromResource(e, R.string.NonArrayIndexed, ((NonArrayIndexed) e).getType().toString());
            }
            if (e instanceof NonConstantExpressionException) {
                return getMessageFromResource(e, R.string.NonConstantExpressionException);
            }
            if (e instanceof NotAStatementException) {
                return getMessageFromResource(e, R.string.NotAStatementException2,
                        ((NotAStatementException) e).getRuntimeValue().toString());
            }
            if (e instanceof DuplicateIdentifierException) {
                DuplicateIdentifierException exception = (DuplicateIdentifierException) e;
                return getMessageFromResource(e, R.string.SameNameException, exception.getType(),
                        exception.getName(), exception.getPreType(), exception.getPreLine());
            }
            if (e instanceof UnAssignableTypeException) {
                return getMessageFromResource(e, R.string.UnAssignableTypeException,
                        ((UnAssignableTypeException) e).getRuntimeValue().toString());
            }
            if (e instanceof TypeIdentifierExpectException) {
                return getMessageFromResource(e, R.string.UnrecognizedTypeException,
                        ((TypeIdentifierExpectException) e).getMissingType());
            }
            if (e instanceof InvalidNumericFormatException) {
                return getMessageFromResource(e, R.string.InvalidNumericFormatException);
            }
            if (e instanceof PascalArithmeticException) {
                return getMessageFromResource(e, R.string.PascalArithmeticException, ((PascalArithmeticException) e).error.getLocalizedMessage());
            }
            if (e instanceof CanNotReadVariableException) {
                return getMessageFromResource(e, R.string.CanNotReadVariableException);
            }
            if (e instanceof WrongIfElseStatement) {
                return getMessageFromResource(e, R.string.WrongIfElseStatement);
            }
            if (e instanceof LowerGreaterUpperBoundException) {
                return getMessageFromResource(e, R.string.SubRangeException,
                        ((LowerGreaterUpperBoundException) e).getHigh(), ((LowerGreaterUpperBoundException) e).getLow());
            }
            if (e instanceof OverridingFunctionBodyException) {
                if (!((OverridingFunctionBodyException) e).isMethod()) {
                    return getMessageFromResource(e, R.string.OverridingFunctionException,
                            ((OverridingFunctionBodyException) e).getFunctionDeclaration().getName(),
                            ((OverridingFunctionBodyException) e).getFunctionDeclaration().getLineNumber());
                } else {
                    return getMessageFromResource(e, R.string.OverridingFunctionException);
                }
            }
            if (e instanceof DivisionByZeroException) {
                return getMessageFromResource(e, R.string.DivisionByZeroException);
            }
            if (e instanceof ParsingException) {
                String line = formatLine(mContext, ((ParsingException) e).getLineInfo());
                Spanned message = ((ParsingException) e).getLocalizedMessage(mContext);
                return new SpannableStringBuilder(line).append("\n\n").append(message);
            }
            return new SpannableString(e.getMessage());
        } catch (Exception err) {
            err.printStackTrace();
            return new SpannableString(err.toString());
        }
    }

    public Spanned getMessageFromResource(Throwable e, int resourceID, Object... arg) {
        return getMessageFromResource(e, mContext, resourceID, arg);
    }

}
