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

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.define.BadFunctionCallException;
import com.duy.pascal.backend.parse_exception.define.MainProgramNotFoundException;
import com.duy.pascal.backend.parse_exception.define.MultipleDefaultValuesException;
import com.duy.pascal.backend.parse_exception.define.MultipleDefinitionsMainException;
import com.duy.pascal.backend.parse_exception.define.UnknownIdentifierException;
import com.duy.pascal.backend.parse_exception.define.OverridingFunctionBodyException;
import com.duy.pascal.backend.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.backend.parse_exception.define.TypeIdentifierExpectException;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.parse_exception.grouping.StrayCharacterException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.parse_exception.index.NonIntegerIndexException;
import com.duy.pascal.backend.parse_exception.index.LowerGreaterUpperBoundException;
import com.duy.pascal.backend.parse_exception.io.LibraryNotFoundException;
import com.duy.pascal.backend.parse_exception.missing.MissingCommaTokenException;
import com.duy.pascal.backend.parse_exception.missing.MissingSemicolonTokenException;
import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.operator.ConstantCalculationException;
import com.duy.pascal.backend.parse_exception.operator.DivisionByZeroException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.syntax.NotAStatementException;
import com.duy.pascal.backend.parse_exception.syntax.WrongIfElseStatement;
import com.duy.pascal.backend.parse_exception.value.ChangeValueConstantException;
import com.duy.pascal.backend.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.backend.parse_exception.value.NonIntegerException;
import com.duy.pascal.backend.parse_exception.value.UnAssignableTypeException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.DiskReadErrorException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotAssignException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.backend.builtin_libraries.runtime_exceptions.CanNotReadVariableException;
import com.duy.pascal.backend.runtime_exception.InvalidNumericFormatException;
import com.duy.pascal.backend.runtime_exception.PascalArithmeticException;
import com.duy.pascal.backend.runtime_exception.MethodCallException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.StackOverflowException;
import com.duy.pascal.backend.utils.ArrayUtils;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_editor.completion.Patterns;

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
                return getMessageResource(e, R.string.MissingSemicolonTokenException,
                        ((MissingSemicolonTokenException) e).getLineInfo().getLine());

            if (e instanceof MissingCommaTokenException)
                return getMessageResource(e, R.string.MissingCommaTokenException,
                        ((MissingCommaTokenException) e).getLineInfo().getLine());

            if (e instanceof StrayCharacterException)
                return getMessageResource(e, R.string.StrayCharacterException,
                        ((StrayCharacterException) e).getCharCode());

            if (e instanceof UnknownIdentifierException) {
                return getMessageResource(e, R.string.NoSuchFunctionOrVariableException, ((UnknownIdentifierException) e).getName());
            }
            if (e instanceof BadFunctionCallException) return getBadFunctionCallException(e);

            if (e instanceof MultipleDefinitionsMainException)
                return new SpannableString(context.getString(string.multi_define_main));

            if (e instanceof GroupingException)
                return getEnumeratedGroupingException((GroupingException) e);

            if (e instanceof UnrecognizedTokenException)
                return getUnrecognizedTokenException((UnrecognizedTokenException) e);

            if (e instanceof MainProgramNotFoundException) {
                return new SpannableString(context.getString(string.main_program_not_define));
            }
            if (e instanceof BadOperationTypeException) {
                return getBadOperationTypeException((BadOperationTypeException) e);
            }
            if (e instanceof FileException) return getFileException((FileException) e);
            if (e instanceof MethodCallException) return getPluginCallException(e);
            if (e instanceof NonIntegerIndexException) return getNonIntegerIndexException(e);
            if (e instanceof NonIntegerException) return getNonIntegerException(e);
            if (e instanceof ConstantCalculationException)
                return getConstantCalculationException(e);

            if (e instanceof ChangeValueConstantException) {
                return getMessageResource(e, R.string.ChangeValueConstantException);
            }

            if (e instanceof UnConvertibleTypeException) {
                UnConvertibleTypeException exception = (UnConvertibleTypeException) e;
                if (exception.getIdentifier() == null) {
                    return getMessageResource(e, R.string.UnConvertibleTypeException,
                            exception.getValue(), exception.getValueType(), exception.getTargetType());
                } else {

                    return getMessageResource(e, R.string.UnConvertibleTypeException2,
                            exception.getValue(), exception.getValueType(), exception.getTargetType(), exception.getIdentifier());
                }
            }
            if (e instanceof LibraryNotFoundException) {
                return getMessageResource(e, R.string.LibraryNotFoundException,
                        ((LibraryNotFoundException) e).getName());
            }
            if (e instanceof MultipleDefaultValuesException) {
                return getMessageResource(e, R.string.MultipleDefaultValuesException);
            }
            if (e instanceof NonArrayIndexed) {
                return getMessageResource(e, R.string.NonArrayIndexed, ((NonArrayIndexed) e).getT().toString());
            }
            if (e instanceof NonConstantExpressionException) {
                return getMessageResource(e, R.string.NonConstantExpressionException);
            }
            if (e instanceof NotAStatementException) {
                return getMessageResource(e, R.string.NotAStatementException,
                        ((NotAStatementException) e).getRuntimeValue().toString());
            }
            if (e instanceof DuplicateIdentifierException) {
                DuplicateIdentifierException exception = (DuplicateIdentifierException) e;
                return getMessageResource(e, R.string.SameNameException, exception.getType(),
                        exception.getName(), exception.getPreType(), exception.getPreLine());
            }
            if (e instanceof UnAssignableTypeException) {
                return getMessageResource(e, R.string.UnAssignableTypeException,
                        ((UnAssignableTypeException) e).getRuntimeValue().toString());
            }
            if (e instanceof TypeIdentifierExpectException) {
                return getMessageResource(e, R.string.UnrecognizedTypeException,
                        ((TypeIdentifierExpectException) e).getMissingType());
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
            if (e instanceof LowerGreaterUpperBoundException) {
                return getMessageResource(e, R.string.SubRangeException,
                        ((LowerGreaterUpperBoundException) e).getHigh(), ((LowerGreaterUpperBoundException) e).getLow());
            }
            if (e instanceof OverridingFunctionBodyException) {
                if (!((OverridingFunctionBodyException) e).isMethod()) {
                    return getMessageResource(e, R.string.OverridingFunctionException,
                            ((OverridingFunctionBodyException) e).getFunctionDeclaration().getName(),
                            ((OverridingFunctionBodyException) e).getFunctionDeclaration().getLineNumber());
                } else {
                    return getMessageResource(e, R.string.OverridingFunctionException);
                }
            }
            if (e instanceof ParsingException) {
                return new SpannableString(((ParsingException) e).getLineInfo() + "\n\n" + e.getLocalizedMessage());
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
            stringBuilder.append(((ParsingException) e).getLineInfo().toString());
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
        stringBuilder.append(exception.getLineInfo().toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(context.getString(string.ConstantCalculationException),
                exception.getE().getLocalizedMessage());
        stringBuilder.append(format);
        return stringBuilder;

    }


    private Spanned getNonIntegerException(Throwable e) {
        NonIntegerIndexException exception = (NonIntegerIndexException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.getLineInfo().toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(
                context.getString(string.NonIntegerException),
                exception.getValue().toString());
        stringBuilder.append(format);
        return stringBuilder;
    }

    private Spanned getNonIntegerIndexException(Throwable e) {
        NonIntegerIndexException exception = (NonIntegerIndexException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(exception.getLineInfo().toString());
        stringBuilder.append("\n").append("\n");
        String format = String.format(context.getString(string.NonIntegerIndexException), exception.getValue().toString());
        stringBuilder.append(format);
        return highlight(stringBuilder);
    }

    private Spanned getPluginCallException(Throwable e) {
        MethodCallException exception = (MethodCallException) e;
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
        } else if (e instanceof com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotFoundException) {
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
        if (e.getValue1() == null) {
            source = String.format(context.getString(string.BadOperationTypeException2), e.getOperatorTypes());
        } else {
            source = String.format(context.getString(string.BadOperationTypeException), e.getOperatorTypes(), e.getValue1(), e.getValue2(), e.getDeclaredType(), e.getDeclaredType1());
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(e.getLineInfo().toString());
        spannable.append("\n\n").append(source);
        return highlight(spannable);
    }

    private Spannable getUnrecognizedTokenException(UnrecognizedTokenException e) {
        String msg = context.getString(string.token_not_belong) + " ";
        Spannable span = new SpannableString(msg + e.getToken().toString());
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                msg.length(), msg.length() + e.getToken().toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    private Spannable getEnumeratedGroupingException(GroupingException e) {
        GroupingException.Type exceptionTypes = e.getExceptionTypes();
        if (exceptionTypes == GroupingException.Type.IO_EXCEPTION) {
            return new SpannableString(context.getString(string.IO_EXCEPTION));
        } else if (exceptionTypes == GroupingException.Type.EXTRA_END) {
            return new SpannableString(context.getString(string.EXTRA_END));
        } else if (exceptionTypes == GroupingException.Type.INCOMPLETE_CHAR) {
            return new SpannableString(context.getString(string.INCOMPLETE_CHAR));
        } else if (exceptionTypes == GroupingException.Type.MISMATCHED_BEGIN_END) {
            return new SpannableString(context.getString(string.MISMATCHED_BEGIN_END));
        } else if (exceptionTypes == GroupingException.Type.MISMATCHED_BRACKETS) {
            return new SpannableString(context.getString(string.MISMATCHED_BRACKETS));
        } else if (exceptionTypes == GroupingException.Type.MISMATCHED_PARENTHESES) {
            return new SpannableString(context.getString(string.MISMATCHED_PARENTHESES));
        } else if (exceptionTypes == GroupingException.Type.UNFINISHED_BEGIN_END) {
            return new SpannableString(context.getString(string.UNFINISHED_BEGIN_END));
        } else if (exceptionTypes == GroupingException.Type.UNFINISHED_PARENTHESES) {
            return new SpannableString(context.getString(string.UNFINISHED_PARENTHESES));
        } else if (exceptionTypes == GroupingException.Type.UNFINISHED_BRACKETS) {
            return new SpannableString(context.getString(string.UNFINISHED_BRACKETS));
        } else if (exceptionTypes == GroupingException.Type.MISSING_INCLUDE) {
            return new SpannableString(context.getString(string.MISSING_INCLUDE));
        } else if (exceptionTypes == GroupingException.Type.NEWLINE_IN_QUOTES) {
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
            result.append(e.getLineInfo().toString()).append("\n\n");
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
