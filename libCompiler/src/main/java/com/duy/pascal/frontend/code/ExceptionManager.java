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
import com.duy.pascal.backend.exceptions.missing.MissingCommaTokenException;
import com.duy.pascal.backend.exceptions.missing.MissingSemicolonTokenException;
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
import com.duy.pascal.backend.utils.ArrayUtils;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_editor.completion.Patterns;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;
import com.js.interpreter.runtime.exception.PascalArithmeticException;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.StackOverflowException;

import java.util.List;
import java.util.regex.Matcher;

import static com.duy.pascal.frontend.R.string;

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
            if (e instanceof ExpectedTokenException)
                return getExpectedTokenException((ExpectedTokenException) e);

            if (e instanceof StackOverflowException)
                return getMessageResource(e, R.string.StackOverflowException);

            if (e instanceof MissingSemicolonTokenException)
                return getMessageResource(e, R.string.MissingSemicolonTokenException, ((MissingSemicolonTokenException) e).line.line);

            if (e instanceof MissingCommaTokenException)
                return getMessageResource(e, R.string.MissingCommaTokenException,
                        ((MissingCommaTokenException) e).line.line);

            if (e instanceof StrayCharacterException)
                return getMessageResource(e, R.string.StrayCharacterException,
                        ((StrayCharacterException) e).charCode);

            if (e instanceof NoSuchFunctionOrVariableException) {
                return getMessageResource(e, R.string.NoSuchFunctionOrVariableException, ((NoSuchFunctionOrVariableException) e).getName());
            }
            if (e instanceof BadFunctionCallException) return getBadFunctionCallException(e);

            if (e instanceof MultipleDefinitionsMainException)
                return new SpannableString(context.getString(string.multi_define_main));

            if (e instanceof GroupingExceptionType)
                return getEnumeratedGroupingException((GroupingExceptionType) e);

            if (e instanceof UnrecognizedTokenException)
                return getUnrecognizedTokenException((UnrecognizedTokenException) e);

            if (e instanceof MainProgramNotFoundException) {
                return new SpannableString(context.getString(string.main_program_not_define));
            }
            if (e instanceof BadOperationTypeException) {
                return getBadOperationTypeException((BadOperationTypeException) e);
            }
            if (e instanceof FileException) return getFileException((FileException) e);
            if (e instanceof PluginCallException) return getPluginCallException(e);
            if (e instanceof NonIntegerIndexException) return getNonIntegerIndexException(e);
            if (e instanceof NonIntegerException) return getNonIntegerException(e);
            if (e instanceof ConstantCalculationException)
                return getConstantCalculationException(e);

            if (e instanceof ChangeValueConstantException) {
                return getMessageResource(e, R.string.ChangeValueConstantException);
            }

            if (e instanceof UnConvertibleTypeException) {
                UnConvertibleTypeException exception = (UnConvertibleTypeException) e;
                if (exception.targetValue == null) {
                    return getMessageResource(e, R.string.UnConvertibleTypeException,
                            exception.value, exception.valueType, exception.targetType);
                } else {

                    return getMessageResource(e, R.string.UnConvertibleTypeException2,
                            exception.value, exception.valueType, exception.targetType, exception.targetValue);
                }
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
                            ((OverridingFunctionBodyException) e).functionDeclaration.getName(),
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
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        if (e instanceof ParsingException) {
            stringBuilder.append(((ParsingException) e).line.toString());
            stringBuilder.append("\n").append("\n");
            String format = String.format(context.getString(resourceID), arg);
            stringBuilder.append(format);
            return highlight(stringBuilder);
        } else if (e instanceof RuntimePascalException) {
            stringBuilder.append(((RuntimePascalException) e).line.toString());
            stringBuilder.append("\n").append("\n");
            String format = String.format(context.getString(resourceID), arg);
            stringBuilder.append(format);
            return highlight(stringBuilder);
        }
        return new SpannableString(e.getLocalizedMessage());
    }


    private Spanned getConstantCalculationException(Throwable e) {
        ConstantCalculationException exception = (ConstantCalculationException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.line.toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(context.getString(string.ConstantCalculationException),
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
                context.getString(string.NonIntegerException),
                exception.value.toString());
        stringBuilder.append(format);
        return stringBuilder;
    }

    private Spanned getNonIntegerIndexException(Throwable e) {
        NonIntegerIndexException exception = (NonIntegerIndexException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.line.toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(context.getString(string.NonIntegerIndexException), exception.value.toString());
        stringBuilder.append(format);
        return highlight(stringBuilder);
    }

    private Spanned getPluginCallException(Throwable e) {
        PluginCallException exception = (PluginCallException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String format = String.format(
                context.getString(string.PluginCallException),
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
        stringBuilder.append(context.getString(string.file));
        stringBuilder.append(": ");
        stringBuilder.append(e.filePath);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0,
                stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        if (e instanceof DiskReadErrorException) {
            stringBuilder.append(context.getString(string.DiskReadErrorException));
        } else if (e instanceof FileNotAssignException) {
            stringBuilder.append(context.getString(string.FileNotAssignException));
        } else if (e instanceof com.duy.pascal.backend.lib.file.exceptions.FileNotFoundException) {
            stringBuilder.append(context.getString(string.FileNotFoundException));
        } else if (e instanceof FileNotOpenException) {
            stringBuilder.append(context.getString(string.FileNotOpenException));
        } else if (e instanceof FileNotOpenForInputException) {
            stringBuilder.append(context.getString(string.FileNotOpenForInputException));
        }
        return stringBuilder;
    }

    private Spannable getBadOperationTypeException(BadOperationTypeException e) {
        String source;
        if (e.value1 == null) {
            source = String.format(context.getString(string.BadOperationTypeException2), e.operatorTypes);
        } else {
            source = String.format(context.getString(string.BadOperationTypeException), e.operatorTypes, e.value1, e.value2, e.declaredType, e.declaredType1);
        }
        return new SpannableString(source);
    }

    private Spannable getUnrecognizedTokenException(UnrecognizedTokenException e) {
        String msg = context.getString(string.token_not_belong) + " ";
        Spannable span = new SpannableString(msg + e.token.toString());
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                msg.length(), msg.length() + e.token.toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    private Spannable getEnumeratedGroupingException(GroupingExceptionType e) {
        GroupingExceptionType.GroupExceptionType exceptionTypes = e.exceptionTypes;
        if (exceptionTypes == GroupingExceptionType.GroupExceptionType.IO_EXCEPTION) {
            return new SpannableString(context.getString(string.IO_EXCEPTION));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.EXTRA_END) {
            return new SpannableString(context.getString(string.EXTRA_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.INCOMPLETE_CHAR) {
            return new SpannableString(context.getString(string.INCOMPLETE_CHAR));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_BEGIN_END) {
            return new SpannableString(context.getString(string.MISMATCHED_BEGIN_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_BRACKETS) {
            return new SpannableString(context.getString(string.MISMATCHED_BRACKETS));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISMATCHED_PARENTHESES) {
            return new SpannableString(context.getString(string.MISMATCHED_PARENTHESES));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_BEGIN_END) {
            return new SpannableString(context.getString(string.UNFINISHED_BEGIN_END));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_PARENTHESES) {
            return new SpannableString(context.getString(string.UNFINISHED_PARENTHESES));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.UNFINISHED_BRACKETS) {
            return new SpannableString(context.getString(string.UNFINISHED_BRACKETS));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.MISSING_INCLUDE) {
            return new SpannableString(context.getString(string.MISSING_INCLUDE));
        } else if (exceptionTypes == GroupingExceptionType.GroupExceptionType.NEWLINE_IN_QUOTES) {
            return new SpannableString(context.getString(string.NEWLINE_IN_QUOTES));
        }
        return new SpannableString(e.getLocalizedMessage());
    }

    private Spannable getBadFunctionCallException(Throwable throwable) {
        BadFunctionCallException e = (BadFunctionCallException) throwable;
        boolean functionExists = e.getFunctionExists();
        boolean argsMatch = e.getArgsMatch();
        if (functionExists) { //function is exist, but wrong argument
            SpannableStringBuilder result = new SpannableStringBuilder();
            result.append(e.line.toString()).append("\n\n");
            if (argsMatch) { //wrong type
                String msg = String.format(context.getString(string.BadFunctionCallException_1), e.getFunctionName());
                result.append(msg);
            } else { //wrong size of args
                String msg = String.format(context.getString(string.BadFunctionCallException_2), e.getFunctionName());
                result.append(msg);
            }

            //add list function
            List<String> functions = e.getFunctions();
            if (functions != null) {
                result.append("\n\n");
                result.append("Accept functions: ").append("\n");
                for (String function : functions) {
                    result.append(function).append("\n");
                }
            }

            return highlight(result);
        } else {
            String msg = String.format(context.getString(string.BadFunctionCallException_3), e.getFunctionName());
            SpannableString spannableString = new SpannableString(msg);
            return highlight(spannableString);
        }
    }

    private Spanned getExpectedTokenException(ExpectedTokenException e) {
        String msg = String.format(context.getString(string.ExpectedTokenException_3),
                ArrayUtils.expectToString(e.getExpected(), context), e.getCurrent());
        SpannableString spannableString = new SpannableString(msg);
        return highlight(spannableString);
    }

    private Spannable highlight(Spannable spannable) {
        Matcher matcher = Patterns.REPLACE_HIGHLIGHT.matcher(spannable);
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(Color.YELLOW), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }
}
