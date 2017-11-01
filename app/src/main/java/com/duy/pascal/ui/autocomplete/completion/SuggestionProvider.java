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

package com.duy.pascal.ui.autocomplete.completion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.datastructure.ArrayListMultimap;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.autocomplete.completion.model.KeyWordDescription;
import com.duy.pascal.ui.editor.view.CodeSuggestsEditText;
import com.duy.pascal.ui.utils.DLog;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.pascal.ui.autocomplete.completion.CompleteContext.CONTEXT_AFTER_FOR;
import static com.duy.pascal.ui.autocomplete.completion.CompleteContext.CONTEXT_NONE;
import static com.duy.pascal.ui.autocomplete.completion.Patterns.END_ASSIGN;
import static com.duy.pascal.ui.autocomplete.completion.Patterns.ID_ASSIGN;

/**
 * Created by Duy on 17-Aug-17.
 */

public class SuggestionProvider {
    private static final String TAG = "SuggestionProvider";
    private static final int MAX_CHAR = 1000;
    private String mSource;
    private int mCursorPos;
    private int mCursorLine;
    private int mCursorCol;
    private String mIncomplete, mPreWord;

    private CompleteContext mCompleteContext = CONTEXT_NONE;
    private CodeSuggestsEditText.SymbolsTokenizer mSymbolsTokenizer;
    private ParsingException mParsingException;

    public SuggestionProvider() {
        mSymbolsTokenizer = new CodeSuggestsEditText.SymbolsTokenizer();
        mIncomplete = "";
    }

    public ArrayList<Description> getSuggestion(String srcPath, String source,
                                                int cursorPos, int cursorLine, int cursorCol) {
        long time = System.currentTimeMillis();
        this.mSource = source;
        this.mCursorPos = cursorPos;
        this.mCursorLine = cursorLine;
        this.mCursorCol = cursorCol;
        try {
            calculateIncomplete();
            ArrayList<Description> suggestItems = new ArrayList<>();

            if (source.length() <= MAX_CHAR) {
                try {
                    CodeUnit codeUnit;
                    codeUnit = PascalCompiler.loadPascal(srcPath,
                            new StringReader(source), null, null);

                    //the result
                    addSuggestFromContext(suggestItems, codeUnit.getContext());
                    mParsingException = null;
                } catch (CodeUnitParsingException e) { //parsing error
                    addSuggestFromContext(suggestItems, e.getCodeUnit().getContext());
                    mParsingException = e.getParseException();
                } catch (Exception ignored) {
                }
            }

            suggestItems.addAll(sort(getKeyword()));
            DLog.d(TAG, "getSuggestion: time = " + (System.currentTimeMillis() - time));
            return suggestItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public ParsingException getParsingException() {
        return mParsingException;
    }

    private void addSuggestFromContext(ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        switch (mCompleteContext) {
            case CONTEXT_AFTER_FOR:
                completeFor(mIncomplete, toAdd, exprContext);
                completeWord(toAdd, exprContext);
                break;
            case CONTEXT_AFTER_TO:
                completeWord(toAdd, exprContext);
                break;
            case CONTEXT_ASSIGN:
                completeWord(toAdd, exprContext);
                break;
            default:
            case CONTEXT_NONE:
                completeWord(toAdd, exprContext);
                break;
        }
    }

    private void completeFor(@NonNull String prefix,
                             @NonNull ArrayList<Description> toAdd,
                             @NonNull ExpressionContextMixin exprContext) {
        for (VariableDeclaration var : exprContext.getVariables()) {
            Type type = var.getType();
            if (TypeConverter.isInteger(type)) {
                if (var.getName().isPrefix(prefix)) {
                    toAdd.add(CompletionFactory.makeVariable(var));
                }
            }
        }
    }

    private void completeWord(ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        ArrayList<VariableDeclaration> variables = exprContext.getVariables();
        toAdd.addAll(sort(filterVariables(variables)));

        Map<Name, ConstantDefinition> constants = exprContext.getConstants();
        toAdd.addAll(sort(filterConst(constants)));

        ArrayListMultimap<Name, AbstractFunction> callableFunctions = exprContext.getCallableFunctions();
        toAdd.addAll(sort(filterFunctions(callableFunctions)));
    }

    private ArrayList<Description> getKeyword() {
        ArrayList<Description> suggestItems = new ArrayList<>();
        if (mIncomplete.isEmpty()) return suggestItems;
        for (String str : KeyWord.ALL_KEY_WORD) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete)) {
                suggestItems.add(new KeyWordDescription(str, null));
            }
        }
        return suggestItems;
    }

    private void calculateIncomplete() {
        mIncomplete = "";
        int start = mSymbolsTokenizer.findTokenStart(mSource, mCursorPos);
        mIncomplete = mSource.substring(start, mCursorPos);

        mIncomplete = mIncomplete.trim();
        String beforeIncomplete = mSource.substring(0, start);

        mPreWord = null;
        mCompleteContext = CONTEXT_NONE;
        //complete assign
        if (END_ASSIGN.matcher(beforeIncomplete).find()) { //:=|
            mCompleteContext = CompleteContext.CONTEXT_ASSIGN;
            Matcher matcher = ID_ASSIGN.matcher(beforeIncomplete);
            if (matcher.find()) {
                mPreWord = matcher.group(1);
            }
        } else {
            start = mSymbolsTokenizer.findTokenEnd(beforeIncomplete, start);
            if (start >= 0) {
                //get previous word
                mPreWord = mSource.substring(0, start);
                Pattern forStatement = Pattern.compile("\\s+(for)\\s+$");
                Pattern toKeyword = Pattern.compile("\\s+(to)\\s+$");
                //for keyword
                //syntax "for <var>=integer_value to|downto integer_value do
                if (forStatement.matcher(mPreWord).find()) {
                    mCompleteContext = CONTEXT_AFTER_FOR;
                } else if (toKeyword.matcher(mPreWord).find()) {
                    mCompleteContext = CompleteContext.CONTEXT_AFTER_TO;
                }

            }
        }
        System.out.println("mCompleteContext = " + mCompleteContext);
        DLog.d(TAG, "calculateIncomplete mIncomplete = '" + mIncomplete + "'");
        DLog.d(TAG, "calculateIncomplete: mPreWord = '" + mPreWord + "'");
    }

    private ArrayList<Description> sort(ArrayList<Description> items) {
        //sort by type -> name
        Collections.sort(items, new Comparator<Description>() {
            @Override
            public int compare(Description o1, Description o2) {
                if (!o1.getKind().equals(o2.getKind())) {
                    return o1.getKind().compareTo(o2.getKind());
                } else {
                    return o1.getHeader().compareTo(o2.getHeader());
                }
            }
        });
        return items;
    }

    private ArrayList<Description> filterConst(Map<Name, ConstantDefinition> constants) {
        if (mIncomplete.isEmpty()) return new ArrayList<>();

        ArrayList<Description> suggestItems = new ArrayList<>();

        for (Map.Entry<Name, ConstantDefinition> entry : constants.entrySet()) {
            ConstantDefinition constant = entry.getValue();
            if (constant.getName().isPrefix(mIncomplete)) {
                if (beforeCursor(constant.getLineNumber())) {
                    suggestItems.add(CompletionFactory.makeConstant(constant));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterVariables(ArrayList<VariableDeclaration> variables) {
        if (mIncomplete.isEmpty()) return new ArrayList<>();
        ArrayList<Description> suggestItems = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            if (variable.getName().isPrefix(mIncomplete)) {
                if (beforeCursor(variable.getLineNumber())) {
                    suggestItems.add(CompletionFactory.makeVariable(variable));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterFunctions(ArrayListMultimap<Name, AbstractFunction> allFunctions) {
        if (mIncomplete.isEmpty()) {
            return new ArrayList<>();
        }
        ArrayList<Description> suggestItems = new ArrayList<>();
        Collection<ArrayList<AbstractFunction>> values = allFunctions.values();
        for (ArrayList<AbstractFunction> list : values) {
            for (AbstractFunction function : list) {
                if (function.getName().isPrefix(mIncomplete)) {
                    if (beforeCursor(function.getLineNumber())) {
                        suggestItems.add(CompletionFactory.makeFunction(function));
                    }
                }
            }
        }
        return suggestItems;
    }

    private boolean beforeCursor(LineInfo line) {
        return line != null && line.getLine() <= mCursorLine && line.getColumn() <= mCursorCol;
    }


}
