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
import com.duy.pascal.interperter.source.FileScriptSource;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.ForToken;
import com.duy.pascal.interperter.tokens.basic.ToToken;
import com.duy.pascal.interperter.tokens.basic.UsesToken;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.autocomplete.completion.model.KeyWordDescription;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.editor.view.CodeSuggestsEditText;
import com.duy.pascal.ui.utils.DLog;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.duy.pascal.ui.autocomplete.completion.CompleteContext.CONTEXT_NONE;

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
    private LinkedList<Token> mSourceTokens;
    private List<Token> mStatement;

    public SuggestionProvider() {
        mSymbolsTokenizer = new CodeSuggestsEditText.SymbolsTokenizer();
        mIncomplete = "";
    }

    @Nullable
    public ArrayList<Description> getSuggestion(@Nullable String srcPath, @NonNull String source,
                                                int cursorPos, int cursorLine, int cursorCol) {
        long time = System.currentTimeMillis();
        this.mSource = source;
        this.mCursorPos = cursorPos;
        this.mCursorLine = cursorLine;
        this.mCursorCol = cursorCol;
        try {
            FileScriptSource scriptSource = new FileScriptSource(new StringReader(mSource), srcPath);
            init(scriptSource);
            ArrayList<Description> suggestItems = new ArrayList<>();

            if (source.length() <= MAX_CHAR) {
                try {
                    CodeUnit codeUnit = PascalCompiler.loadPascal(scriptSource, null, null);

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

    /**
     * Prepare for parsing
     *
     * @param scriptSource - source code from editor
     */
    private void init(FileScriptSource scriptSource) throws IOException {
        calculateIncomplete();
        mSourceTokens = scriptSource.toTokens();
        int column = mCursorCol - mIncomplete.length() ;
        mStatement = SourceHelper.getStatement(mSourceTokens, mCursorLine, column);
        defineContext();
    }

    private void calculateIncomplete() {
        int start = mSymbolsTokenizer.findTokenStart(mSource, mCursorPos);
        mIncomplete = mSource.substring(start, mCursorPos).trim();
        System.out.println("mIncomplete = " + mIncomplete);
        mPreWord = null;
    }


    private void addSuggestFromContext(@NonNull ArrayList<Description> toAdd, @NonNull ExpressionContextMixin exprContext) {
        System.out.println("mCompleteContext = " + mCompleteContext);
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
            case CONTEXT_USES:
                completeUses(toAdd, exprContext);
                break;
            case CONTEXT_COMMA_SEMICOLON:
                completeAddToken(toAdd, exprContext, ",", ";");
                break;
            case CONTEXT_CONST:
            case CONTEXT_TYPE:
            case CONTEXT_VAR:
            default:
            case CONTEXT_NONE:
                completeWord(toAdd, exprContext);
                break;
        }
    }

    private void completeAddToken(ArrayList<Description> toAdd, ExpressionContextMixin exprContext, String... token) {
        for (String str : token) {
            toAdd.add(new DescriptionImpl(DescriptionImpl.KIND_UNDEFINED, str));
        }
    }

    private void completeUses(ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        for (String str : KeyWord.BUILTIN_LIBS) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete)) {
                toAdd.add(new KeyWordDescription(str, null));
            }
        }
    }

    /**
     * Add suggestion for "for" statement, only accept integer variable
     *
     * @param prefix - incomplete
     */
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
        if (mIncomplete.isEmpty()) {
            return suggestItems;
        }
        for (String str : KeyWord.ALL_KEY_WORD) {
            if (str.toLowerCase().startsWith(mIncomplete.toLowerCase())
                    && !str.equalsIgnoreCase(mIncomplete)) {
                suggestItems.add(new KeyWordDescription(str, null));
            }
        }
        return suggestItems;
    }

    /**
     * Define context, incomplete word
     */
    private void defineContext() {
        mCompleteContext = CONTEXT_NONE;
        if (mStatement.isEmpty()) {
            return;
        }

        Token last = mStatement.get(mStatement.size() - 1);
        Token first = mStatement.get(0);
        System.out.println("first = " + first);
        System.out.println("last = " + last);

        if (last instanceof ForToken) {
            //for to do
            mCompleteContext = CompleteContext.CONTEXT_AFTER_FOR;
        } else if (last instanceof ToToken) {
            mCompleteContext = CompleteContext.CONTEXT_AFTER_TO;
        } else if (first instanceof UsesToken) {
//            if (last instanceof CommaToken || mStatement.size() == 1) {
                mCompleteContext = CompleteContext.CONTEXT_USES;
//            } /*else {
//                mCompleteContext = CompleteContext.CONTEXT_COMMA_SEMICOLON;
//            }*/
        }

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

    @Nullable
    public ParsingException getParsingException() {
        return mParsingException;
    }

}
