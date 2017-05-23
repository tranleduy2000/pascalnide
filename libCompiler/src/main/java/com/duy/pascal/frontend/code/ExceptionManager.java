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

package com.duy.pascal.frontend.code;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.define.BadFunctionCallException;
import com.duy.pascal.backend.exceptions.define.MainProgramNotFoundException;
import com.duy.pascal.backend.exceptions.define.MultipleDefaultValuesException;
import com.duy.pascal.backend.exceptions.define.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.define.OverridingFunctionBodyException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.exceptions.define.UnrecognizedTypeException;
import com.duy.pascal.backend.exceptions.grouping.GroupingExceptionType;
import com.duy.pascal.backend.exceptions.grouping.StrayCharacterException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.index.NonIntegerIndexException;
import com.duy.pascal.backend.exceptions.index.SubRangeException;
import com.duy.pascal.backend.exceptions.io.LibraryNotFoundException;
import com.duy.pascal.backend.exceptions.operator.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.operator.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.operator.DivisionByZeroException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.syntax.MissingCommaTokenException;
import com.duy.pascal.backend.exceptions.syntax.MissingSemicolonTokenException;
import com.duy.pascal.backend.exceptions.syntax.NotAStatementException;
import com.duy.pascal.backend.exceptions.syntax.WrongIfElseStatement;
import com.duy.pascal.backend.exceptions.value.ChangeValueConstantException;
import com.duy.pascal.backend.exceptions.value.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.value.NonIntegerException;
import com.duy.pascal.backend.exceptions.value.UnAssignableTypeException;
import com.duy.pascal.backend.lib.file.exceptions.DiskReadErrorException;
import com.duy.pascal.backend.lib.file.exceptions.FileException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotAssignException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.backend.lib.runtime_exceptions.CanNotReadVariableException;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_editor.completion.Patterns;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.StackOverflowException;

import java.util.regex.Matcher;

/**
 * Created by Duy on 11-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class ExceptionManager {
    public static final String TAG = ExceptionManager.class
            .getSimpleName();
    private Context context;

    public ExceptionManager(Context context) {
        this.context = context;
    }

    public Spanned getMessage(Throwable e) {
        if (e == null) {
            return new SpannableString("null");
        }
        try {
            if (e instanceof ExpectedTokenException) {
                return getExpectedTokenException((ExpectedTokenException) e);
            }
            if (e instanceof StackOverflowException) {
                return getMessageResource(e, R.string.StackOverflowException);
            }
            if (e instanceof MissingSemicolonTokenException) {
                return getMessageResource(e, R.string.MissingSemicolonTokenException, ((MissingSemicolonTokenException) e).line.line);
            }
            if (e instanceof MissingCommaTokenException) {
                return getMessageResource(e, R.string.MissingCommaTokenException,
                        ((MissingCommaTokenException) e).line.line);
            }
            if (e instanceof StrayCharacterException) {
                return getMessageResource(e, R.string.StrayCharacterException,
                        ((StrayCharacterException) e).charCode);
            }
            if (e instanceof NoSuchFunctionOrVariableException) {
                return getMessageResource(e, R.string.NoSuchFunctionOrVariableException, ((NoSuchFunctionOrVariableException) e).getName());
            }
            if (e instanceof BadFunctionCallException) {
                return getBadFunctionCallException(e);
            }
            if (e instanceof MultipleDefinitionsMainException) {
                return new SpannableString(context.getString(R.string.multi_define_main));
            }
            if (e instanceof GroupingExceptionType) {
                return getEnumeratedGroupingException((GroupingExceptionType) e);
            }
            if (e instanceof UnrecognizedTokenException) {
                return getUnrecognizedTokenException((UnrecognizedTokenException) e);
            }
            if (e instanceof MainProgramNotFoundException) {
                return new SpannableString(context.getString(R.string.main_program_not_define));
            }
            if (e instanceof BadOperationTypeException) {
                return getBadOperationTypeException((BadOperationTypeException) e);
            }
            if (e instanceof FileException) {
                return getFileException((FileException) e);
            }
            if (e instanceof PluginCallException) {
                return getPluginCallException(e);
            }
            if (e instanceof NonIntegerIndexException) {
                return getNonIntegerIndexException(e);
            }
            if (e instanceof NonIntegerException) {
                return getNonIntegerException(e);
            }
            if (e instanceof ConstantCalculationException) {
                return getConstantCalculationException(e);
            }
            if (e instanceof ChangeValueConstantException) {
                return getMessageResource(e, R.string.ChangeValueConstantException);
            }
            if (e instanceof UnConvertibleTypeException) {
                UnConvertibleTypeException exception = (UnConvertibleTypeException) e;
                return getMessageResource(e, R.string.UnConvertibleTypeException,
                        exception.obj, exception.out, exception.in);
            }

            if (e instanceof LibraryNotFoundException) {
                return getMessageResource(e, R.string.LibraryNotFoundException,
                        ((LibraryNotFoundException) e).name);
            }
            if (e instanceof MultipleDefaultValuesException) {
                return getMessageResource(e, R.string.MultipleDefaultValuesException);
            }
            if (e instanceof NonArrayIndexed) {
                return getMessageResource(e, R.string.NonArrayIndexed, ((NonArrayIndexed) e).t.toString());
            }
            if (e instanceof NonConstantExpressionException) {
                return getMessageResource(e, R.string.NonConstantExpressionException);
            }
            if (e instanceof NotAStatementException) {
                return getMessageResource(e, R.string.NotAStatementException,
                        ((NotAStatementException) e).runtimeValue.toString());
            }
            if (e instanceof SameNameException) {
                SameNameException exception = (SameNameException) e;
                return getMessageResource(e, R.string.SameNameException, exception.getType(),
                        exception.getName(), exception.getPreType(), exception.getPreLine());
            }
            if (e instanceof UnAssignableTypeException) {
                return getMessageResource(e, R.string.UnAssignableTypeException,
                        ((UnAssignableTypeException) e).runtimeValue.toString());
            }
            if (e instanceof UnrecognizedTypeException) {
                return getMessageResource(e, R.string.UnrecognizedTypeException,
                        ((UnrecognizedTypeException) e).missingType);
            }
            if (e instanceof InvalidNumericFormatException) {
                return getMessageResource(e, R.string.InvalidNumericFormatException);
            }
            if (e instanceof PascalArithmeticException) {
                return getMessageResource(e, R.string.PascalArithmeticException, ((PascalArithmeticException) e).error.getLocalizedMessage());
            }
            if (e instanceof CanNotReadVariableException) {
                return getMessageResource(e, R.string.CanNotReadVariableException);
            }
            if (e instanceof WrongIfElseStatement) {
                return getMessageResource(e, R.string.WrongIfElseStatement);
            }
            if (e instanceof SubRangeException) {
                return getMessageResource(e, R.string.SubRangeException,
                        ((SubRangeException) e).high, ((SubRangeException) e).low);
            }
            if (e instanceof OverridingFunctionBodyException) {
                if (!((OverridingFunctionBodyException) e).isMethod) {
                    return getMessageResource(e, R.string.OverridingFunctionException,
                            ((OverridingFunctionBodyException) e).functionDeclaration.name(),
                            ((OverridingFunctionBodyException) e).functionDeclaration.getLineNumber());
                } else {
                    return getMessageResource(e, R.string.OverridingFunctionException);
                }
            }
            if (e instanceof ParsingException) {
                return new SpannableString(((ParsingException) e).line + "\n\n" + e.getLocalizedMessage());
            }

            if (e instanceof DivisionByZeroException) {
                return getMessageResource(e, R.string.DivisionByZeroException);
            }
            return new SpannableString(e.getLocalizedMessage());
        } catch (Exception err) {
//            FirebaseCrash.report(new Throwable("Error when get exception msg"));
            return new SpannableString(err.toString());
        }
    }

    private Spanned getMessageResource(Throwable e, int resourceID, Object... arg) {
        if (e instanceof ParsingException) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append(((ParsingException) e).line.toString());
            stringBuilder.append("\n").append("\n");
            String format = String.format(context.getString(resourceID), arg);
            stringBuilder.append(format);
            return stringBuilder;
        } else if (e instanceof RuntimePascalException) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            stringBuilder.append(((RuntimePascalException) e).line.toString());
            stringBuilder.append("\n").append("\n");
            String format = String.format(context.getString(resourceID), arg);
            stringBuilder.append(format);
            return stringBuilder;
        }
        return new SpannableString(e.getLocalizedMessage());
    }


    private Spanned getConstantCalculationException(Throwable e) {
        ConstantCalculationException exception = (ConstantCalculationException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.line.toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(
                context.getString(R.string.ConstantCalculationException),
                exception.e.getLocalizedMessage());
        stringBuilder.append(format);
        return stringBuilder;

    }


    private Spanned getNonIntegerException(Throwable e) {
        NonIntegerIndexException exception = (NonIntegerIndexException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.line.toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(
                context.getString(R.string.NonIntegerException),
                exception.value.toString());
        stringBuilder.append(format);
        return stringBuilder;
    }

    private Spanned getNonIntegerIndexException(Throwable e) {
        NonIntegerIndexException exception = (NonIntegerIndexException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.line.toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(
                context.getString(R.string.NonIntegerIndexException),
                exception.value.toString());
        stringBuilder.append(format);
        return stringBuilder;
    }

    private Spanned getPluginCallException(Throwable e) {
        PluginCallException exception = (PluginCallException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String format = String.format(
                context.getString(R.string.PluginCallException),
                exception.function.toString(),
                exception.cause.getClass().getSimpleName());
        stringBuilder.append(format);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        stringBuilder.append(getMessage(exception.cause));
        return stringBuilder;
    }

    private Spanned getFileException(FileException e) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(context.getString(R.string.file));
        stringBuilder.append(": ");
        stringBuilder.append(e.filePath);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0,
                stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        if (e instanceof DiskReadErrorException) {
            stringBuilder.append(context.getString(R.string.DiskReadErrorException));
        } else if (e instanceof FileNotAssignException) {
            stringBuilder.append(context.getString(R.string.FileNotAssignException));
        } else if (e instanceof com.duy.pascal.backend.lib.file.exceptions.FileNotFoundException) {
            stringBuilder.append(context.getString(R.string.FileNotFoundException));
        } else if (e instanceof FileNotOpenException) {
            stringBuilder.append(context.getString(R.string.FileNotOpenException));
        } else if (e instanceof FileNotOpenForInputException) {
            stringBuilder.append(context.getString(R.string.FileNotOpenForInputException));
        }
        return stringBuilder;
    }

    private Spannable getBadOperationTypeException(BadOperationTypeException e) {
        String source;
        if (e.value1 == null) {
            source = String.format(context.getString(R.string.BadOperationTypeException2),
                    e.operatorTypes);
        } else {
            source = String.format(context.getString(R.string.BadOperationTypeException),
                    e.operatorTypes, e.value1, e.value2, e.declaredType, e.declaredType1);
        }
        return new SpannableString(source);
    }

    private Spannable getUnrecognizedTokenException(UnrecognizedTokenException e) {
        String msg = context.getString(R.string.token_not_belong) + " ";
        Spannable span = new SpannableString(msg + e.token.toString());
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                msg.length(), msg.length() + e.token.toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    private Spannable getEnumeratedGroupingException(GroupingExceptionType e) {
        GroupingExceptionType.GroupExceptionType exceptionTypes = e.exceptionTypes;
        if (exceptionTypes == GroupingExceptionType.GroupExceptionType.IO_EXCEPTION) {
            return new SpannableString(context.getString(R.string.IO_EXCEPTION));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.EXTRA_END) {
            return new SpannableString(context.getString(R.string.EXTRA_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.INCOMPLETE_CHAR) {
            return new SpannableString(context.getString(R.string.INCOMPLETE_CHAR));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_BEGIN_END) {
            return new SpannableString(context.getString(R.string.MISMATCHED_BEGIN_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_BRACKETS) {
            return new SpannableString(context.getString(R.string.MISMATCHED_BRACKETS));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_PARENTHESES) {
            return new SpannableString(context.getString(R.string.MISMATCHED_PARENTHESES));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_BEGIN_END) {
            return new SpannableString(context.getString(R.string.UNFINISHED_BEGIN_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_PARENTHESES) {
            return new SpannableString(context.getString(R.string.UNFINISHED_PARENTHESES));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_BRACKETS) {
            return new SpannableString(context.getString(R.string.UNFINISHED_BRACKETS));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISSING_INCLUDE) {
            return new SpannableString(context.getString(R.string.MISSING_INCLUDE));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.NEWLINE_IN_QUOTES) {
            return new SpannableString(context.getString(R.string.NEWLINE_IN_QUOTES));
        }
        return new SpannableString(e.getLocalizedMessage());
    }

    private Spannable getBadFunctionCallException(Throwable throwable) {
        BadFunctionCallException e = (BadFunctionCallException) throwable;
        String functionName = e.functionName + " ";
        boolean functionExists = e.functionExists;
        boolean numargsMatch = e.numargsMatch;
        if (functionExists) {
            if (numargsMatch) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append(e.line.toString());
                spannableStringBuilder.append("\n\n");

                String msg = context.getString(R.string.bad_function_msg1) + " ";
                Spannable span = new SpannableString(msg + functionName);
                span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        msg.length(), msg.length() + functionName.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(span);
                return spannableStringBuilder;
            } else {
                String msg = context.getString(R.string.bad_function_msg2) + " ";
                Spannable span = new SpannableString(msg + functionName);
                span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        msg.length(), msg.length() + functionName.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return span;
            }
        } else {
            String msg1 = context.getString(R.string.can_not_call_func) + " ";
            String msg2 = context.getString(R.string.func_not_define) + " ";
            Spannable span = new SpannableString(msg1 + functionName + msg2);
            span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    msg1.length(), msg1.length() + functionName.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
    }


    private Spanned getExpectedTokenException(ExpectedTokenException e) {
        String msg = String.format(context.getString(R.string.ExpectedTokenException_3), e.expected, e.current);
        SpannableString spannableString = new SpannableString(msg);
        Matcher matcher = Patterns.REPLACE_HIGHLIGHT.matcher(spannableString);
        while (matcher.find()) {
            spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
