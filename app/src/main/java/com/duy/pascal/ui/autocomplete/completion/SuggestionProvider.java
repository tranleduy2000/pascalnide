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

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.datastructure.ArrayListMultimap;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.autocomplete.completion.model.ConstantDescription;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.autocomplete.completion.model.FunctionDescription;
import com.duy.pascal.ui.autocomplete.completion.model.VariableDescription;
import com.duy.pascal.ui.editor.view.CodeSuggestsEditText;
import com.duy.pascal.ui.utils.DLog;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static com.duy.pascal.ui.autocomplete.completion.SuggestionProvider.CompleteContext.CONTEXT_WORD;

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

    private CompleteContext mCompleteContext = CONTEXT_WORD;
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
                    PascalProgramDeclaration pascalProgram = PascalCompiler.loadPascal(srcPath,
                            new StringReader(source), null, null);

                    //the result
                    addSuggestFromContext(suggestItems, pascalProgram.getContext());
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
                completeFor(toAdd, exprContext);
                completeWord(toAdd, exprContext);
                break;
            case CONTEXT_AFTER_TO:
                completeWord(toAdd, exprContext);
                break;
            case CONTEXT_ASSIGN:
                completeWord(toAdd, exprContext);
                break;
            default:
            case CONTEXT_WORD:
                completeWord(toAdd, exprContext);
                break;
        }
    }

    private void completeFor(ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        String identifier = mPreWord;
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
        for (String s : KeyWord.ALL_KEY_WORD) {
            if (s.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !s.equalsIgnoreCase(mIncomplete)) {
                suggestItems.add(new DescriptionImpl(DescriptionImpl.KIND_KEYWORD, s));
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
        mCompleteContext = CONTEXT_WORD;
        //complete assign
       /* if (END_ASSIGN.matcher(beforeIncomplete).find()) { //:=|
            mCompleteContext = CompleteContext.CONTEXT_ASSIGN;
            Matcher matcher = ID_ASSIGN.matcher(beforeIncomplete);
            if (matcher.find()) {
                mPreWord = matcher.group(1);
            }
        } else {
            start = mSymbolsTokenizer.findTokenEnd(beforeIncomplete, start);
            if (start >= 0) {
                //get previous word
                mPreWord = mSource.substring(start);
                //for keyword
                //syntax "for <var>=integer_value to|downto integer_value do
                if (mPreWord.equalsIgnoreCase("for")) {
                    mCompleteContext = CompleteContext.CONTEXT_AFTER_FOR;
                }
                //syntax "for <var>=integer_value to|downto integer_value do
                else if (mPreWord.equalsIgnoreCase("to")) {
                    mCompleteContext = CompleteContext.CONTEXT_AFTER_TO;
                }

            }
        }
        DLog.d(TAG, "calculateIncomplete mIncomplete = '" + mIncomplete + "'");
        DLog.d(TAG, "calculateIncomplete: mPreWord = '" + mPreWord + "'");*/
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
            LineInfo line = constant.getLineNumber();
            if (constant.getName().isPrefix(mIncomplete)) {
                if (line != null && line.getLine() <= mCursorLine && line.getColumn() <= mCursorCol) {
                    suggestItems.add(new ConstantDescription(constant));
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
                LineInfo line = variable.getLineNumber();
                if (line != null && line.getLine() <= mCursorLine && line.getColumn() <= mCursorCol) {
                    Name name = variable.getName();
                    suggestItems.add(new VariableDescription(name, variable.getDescription(), variable.getType()));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterFunctions(
            ArrayListMultimap<Name, AbstractFunction> allFunctions) {
        if (mIncomplete.isEmpty()) return new ArrayList<>();
        ArrayList<Description> suggestItems = new ArrayList<>();
        Collection<ArrayList<AbstractFunction>> values = allFunctions.values();
        for (ArrayList<AbstractFunction> list : values) {
            for (AbstractFunction function : list) {
                if (function.getName().isPrefix(mIncomplete)) {
                    LineInfo line = function.getLineNumber();
                    if (line != null && line.getLine() <= mCursorLine && line.getColumn() <= mCursorCol) {
                        Name name = function.getName();
                        ArgumentType[] args = function.argumentTypes();
                        Type type = function.returnType();
                        suggestItems.add(new FunctionDescription(name, args, type));
                    }
                }
            }
        }
        return suggestItems;
    }

    enum CompleteContext {
        CONTEXT_AFTER_FOR, CONTEXT_AFTER_TO, CONTEXT_ASSIGN, CONTEXT_WORD
    }
}
