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
import com.duy.pascal.interperter.ast.runtime.value.NullValue;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.datastructure.ArrayListMultimap;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.source.FileScriptSource;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.AssignmentToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.ForToken;
import com.duy.pascal.interperter.tokens.basic.ToToken;
import com.duy.pascal.interperter.tokens.basic.UsesToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.ui.autocomplete.completion.ast.PascalStatement;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.KeyWordDescription;
import com.duy.pascal.ui.autocomplete.completion.util.KeyWord;
import com.duy.pascal.ui.editor.view.CodeSuggestsEditText;
import com.duy.pascal.ui.utils.DLog;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.duy.pascal.ui.autocomplete.completion.CompleteContext.CONTEXT_EMPTY;
import static com.duy.pascal.ui.autocomplete.completion.CompleteContext.CONTEXT_NONE;
import static com.duy.pascal.ui.autocomplete.completion.PascalSuggestionProvider.completeSuggestType;
import static com.duy.pascal.ui.autocomplete.completion.PascalSuggestionProvider.completeUses;
import static com.duy.pascal.ui.autocomplete.completion.PascalSuggestionProvider.sort;

/**
 * Created by Duy on 17-Aug-17.
 */

public class PascalSuggestionOperation {
    private static final String TAG = "PascalSuggestionOperation";
    private static final int LIMIT_CHAR = 1000;
    private String mSource;
    private int mCursorPos;
    private int mCursorLine;
    private int mCursorCol;

    private CodeSuggestsEditText.SymbolsTokenizer mSymbolsTokenizer;
    private LinkedList<Token> mSourceTokens;

    private PascalStatement mStatement;
    private String mIncomplete, mPreWord;

    private Name mIdentifierType;
    private CompleteContext mCompleteContext = CONTEXT_NONE;
    private ParsingException mParsingException;
    private FileScriptSource mScriptSource;


    public PascalSuggestionOperation() {
        mSymbolsTokenizer = new CodeSuggestsEditText.SymbolsTokenizer();
        mIncomplete = "";
    }

    /**
     * @param srcName    - file name
     * @param source     - current source code
     * @param cursorPos  - cursor index
     * @param cursorLine - line
     * @param cursorCol  - column
     * @return a list items contains some suggestion
     */
    @Nullable
    public ArrayList<Description> getSuggestion(@NonNull String srcName, @NonNull String source,
                                                int cursorPos, int cursorLine, int cursorCol) {
        long time = System.currentTimeMillis();
        this.mSource = source;
        this.mCursorPos = cursorPos;
        this.mCursorLine = cursorLine;
        this.mCursorCol = cursorCol;
        try {
            FileScriptSource scriptSource = new FileScriptSource(new StringReader(mSource), srcName);
            init(scriptSource);

            //the result
            ArrayList<Description> suggestItems = new ArrayList<>();

            if (source.length() <= LIMIT_CHAR) {
                try {
                    CodeUnit codeUnit = PascalCompiler.loadPascal(scriptSource, null, null);
                    //the result
                    addSuggestFromContext(suggestItems, codeUnit.getContext());
                    mParsingException = null;
                } catch (CodeUnitParsingException e) { //parsing error
                    addSuggestFromContext(suggestItems, e.getCodeUnit().getContext());
                    mParsingException = e.getParseException();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            suggestItems.addAll(sort(filterKeyword(mIncomplete)));
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
        mScriptSource = scriptSource;
        mSourceTokens = scriptSource.toTokens();
        calculateIncomplete();
        int column = mCursorCol - mIncomplete.length();
        mStatement = SourceHelper.getStatement(mSourceTokens, mCursorLine, column);
        defineContext();
    }

    private void calculateIncomplete() {
        int start = mSymbolsTokenizer.findTokenStart(mSource, mCursorPos);
        mIncomplete = start >= 0 ? mSource.substring(start, mCursorPos) : "";
        System.out.println("mIncomplete = " + mIncomplete + " length = " + mIncomplete.length());
        mPreWord = null;
    }

    private void addSuggestFromContext(@NonNull ArrayList<Description> toAdd, @NonNull ExpressionContextMixin exprContext) {
        System.out.println("mCompleteContext = " + mCompleteContext);
        switch (mCompleteContext) {
            case CONTEXT_AFTER_FOR:
                completeVariable(mIncomplete, toAdd, exprContext, BasicType.Long);
                completeWord(mIncomplete, toAdd, exprContext);
                break;
            case CONTEXT_NEED_TYPE_INTEGER:
                completeNeedType(mIncomplete, toAdd, exprContext, BasicType.Long);
                break;
            case CONTEXT_NEED_TYPE:
                Type type = findTypeOf(exprContext, mIdentifierType);
                completeNeedType(mIncomplete, toAdd, exprContext, type);
                break;
            case CONTEXT_ASSIGN:
                completeWord(mIncomplete, toAdd, exprContext);
                break;
            case CONTEXT_USES:
                completeUses(mIncomplete, toAdd, exprContext);
                break;
            case CONTEXT_AFTER_COLON:
                //most of case, behind the colon is data type
                //var i: integer; function a(v: integer): integer;
                completeSuggestType(mIncomplete, toAdd, exprContext);
                break;
            case CONTEXT_INSERT_DO:
                PascalSuggestionProvider.completeAddKeyWordToken(mIncomplete, toAdd, exprContext, "do");
                break;
            case CONTEXT_INSERT_TO:
                PascalSuggestionProvider.completeAddKeyWordToken(mIncomplete, toAdd, exprContext, "to");
                break;
            case CONTEXT_AFTER_BEGIN:
                PascalSuggestionProvider.completeAddKeyWordToken(mIncomplete, toAdd, exprContext, "end");
                completeWord(mIncomplete, toAdd, exprContext);
                break;
            case CONTEXT_EMPTY:
                PascalSuggestionProvider.completeEmpty(mScriptSource, toAdd);
                break;
            case CONTEXT_INSERT_ASSIGN:
            case CONTEXT_COMMA_SEMICOLON:
                break;
            case CONTEXT_CONST:
            case CONTEXT_TYPE:
            case CONTEXT_VAR:
            case CONTEXT_NONE:
            default:
                completeWord(mIncomplete, toAdd, exprContext);
                break;
        }
    }

    private Type findTypeOf(ExpressionContextMixin exprContext, Name id) {
        for (VariableDeclaration variable : exprContext.getVariables()) {
            if (variable.getName().equals(id)) {
                if (beforeCursor(variable.getLineNumber())) {
                    return variable.getType();
                }
            }
        }
        for (Map.Entry<Name, ConstantDefinition> entry : exprContext.getConstants().entrySet()) {
            ConstantDefinition constant = entry.getValue();
            if (constant.getName().equals(id)) {
                if (beforeCursor(constant.getLineNumber())) {
                    return constant.getType();
                }
            }
        }
        Collection<ArrayList<AbstractFunction>> values = exprContext.getCallableFunctions().values();
        for (ArrayList<AbstractFunction> list : values) {
            for (AbstractFunction function : list) {
                if (function.getName().equals(id)) {
                    if (beforeCursor(function.getLineNumber())) {
                        return function.returnType();
                    }
                }
            }
        }
        return null;
    }

    private void completeNeedType(String mIncomplete, ArrayList<Description> toAdd,
                                  ExpressionContextMixin exprContext, Type type) {
        ArrayList<VariableDeclaration> variables = exprContext.getVariables();
        toAdd.addAll(sort(filterVariables(mIncomplete, variables, type)));

        Map<Name, ConstantDefinition> constants = exprContext.getConstants();
        toAdd.addAll(sort(filterConst(mIncomplete, constants, type)));

        ArrayListMultimap<Name, AbstractFunction> callableFunctions = exprContext.getCallableFunctions();
        toAdd.addAll(sort(filterFunctions(mIncomplete, callableFunctions, type)));
    }

    /**
     * Add suggestion for "for" statement, only accept integer variable
     *
     * @param prefix - incomplete
     */
    private void completeVariable(@NonNull String prefix,
                                  @NonNull ArrayList<Description> toAdd,
                                  @NonNull ExpressionContextMixin exprContext,
                                  @NonNull BasicType needType) {
        for (VariableDeclaration var : exprContext.getVariables()) {
            Type type = var.getType();
            if (TypeConverter.isLowerThanPrecedence(type.getStorageClass(), needType.getStorageClass())) {
                if (var.getName().isPrefix(prefix)) {
                    toAdd.add(CompletionFactory.makeVariable(var));
                }
            }
        }
    }


    private ArrayList<Description> filterConst(String mIncomplete, Map<Name, ConstantDefinition> constants,
                                               @Nullable Type type) {
        if (mIncomplete.isEmpty() && type == null) return new ArrayList<>();

        ArrayList<Description> suggestItems = new ArrayList<>();

        for (Map.Entry<Name, ConstantDefinition> entry : constants.entrySet()) {
            ConstantDefinition constant = entry.getValue();
            if (constant.getName().isPrefix(mIncomplete) && canConvertType(constant.getType(), type)) {
                if (beforeCursor(constant.getLineNumber())) {
                    suggestItems.add(CompletionFactory.makeConstant(constant));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterVariables(String mIncomplete, ArrayList<VariableDeclaration> variables,
                                                   @Nullable Type type) {
        if (mIncomplete.isEmpty() && type == null) return new ArrayList<>();
        ArrayList<Description> suggestItems = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            if (variable.getName().isPrefix(mIncomplete) && canConvertType(variable.getType(), type)) {
                if (beforeCursor(variable.getLineNumber())) {
                    suggestItems.add(CompletionFactory.makeVariable(variable));
                }
            }
        }
        return suggestItems;
    }

    private boolean canConvertType(@Nullable Type from, @Nullable Type to) {
        if (to == null) return true;
        if (from == null) return false;
        if (from instanceof BasicType && to instanceof BasicType) {
            if (TypeConverter.isInteger(from) && TypeConverter.isInteger(to)) {
                return true;
            }
            return TypeConverter.autoConvert(to, NullValue.get(), from) != null;
        }
        return from.getStorageClass().equals(to.getStorageClass());
    }

    private ArrayList<Description> filterFunctions(String mIncomplete, ArrayListMultimap<Name, AbstractFunction> allFunctions,
                                                   @Nullable Type type) {
        if (mIncomplete.isEmpty() && type == null) {
            return new ArrayList<>();
        }
        ArrayList<Description> suggestItems = new ArrayList<>();
        Collection<ArrayList<AbstractFunction>> values = allFunctions.values();
        for (ArrayList<AbstractFunction> list : values) {
            for (AbstractFunction function : list) {
                if (function.getName().isPrefix(mIncomplete) && canConvertType(function.returnType(), type)) {
                    if (beforeCursor(function.getLineNumber())) {
                        suggestItems.add(CompletionFactory.makeFunction(function));
                    }
                }
            }
        }
        return suggestItems;
    }

    private void completeWord(String mIncomplete, ArrayList<Description> toAdd, ExpressionContextMixin exprContext) {
        ArrayList<VariableDeclaration> variables = exprContext.getVariables();
        toAdd.addAll(sort(filterVariables(mIncomplete, variables, null)));

        Map<Name, ConstantDefinition> constants = exprContext.getConstants();
        toAdd.addAll(sort(filterConst(mIncomplete, constants, null)));

        ArrayListMultimap<Name, AbstractFunction> callableFunctions = exprContext.getCallableFunctions();
        toAdd.addAll(sort(filterFunctions(mIncomplete, callableFunctions, null)));
    }

    private ArrayList<Description> filterKeyword(String mIncomplete) {
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
        if (!PascalCompleteConfident.shouldDisplayPopup(mStatement, mCursorLine, mCursorCol)) {
            mCompleteContext = CONTEXT_EMPTY;
            return; //in comment
        }
        if (mSource.trim().isEmpty()) { //program const uses var begin end.
            mCompleteContext = CONTEXT_EMPTY;
            return;
        }
        mCompleteContext = CONTEXT_NONE; //default context, suggest keyword
        List<Token> statement = mStatement.getStatement();


        if (!statement.isEmpty()) {
            Token last = statement.get(statement.size() - 1);
            Token first = statement.get(0);
            System.out.println("first = " + first);
            System.out.println("last = " + last);

            if (last instanceof ForToken) {
                //for to do
                mCompleteContext = CompleteContext.CONTEXT_AFTER_FOR;
            } else if (first instanceof ForToken) {
                int isFor = SourceHelper.isForNumberStructure(statement);
                if (isFor >= 0) {
                    switch (isFor) {
                        case 1:// {ValueToken}, suggest variable integer
                            mCompleteContext = CompleteContext.CONTEXT_AFTER_FOR;
                            break;
                        case 3://after assign, as before assign, suggest variable integer
                        case 5://after to, as after 'for'
                            mCompleteContext = CompleteContext.CONTEXT_NEED_TYPE_INTEGER;
                            break;
                        case 2: //after value, assign token,
                            mCompleteContext = CompleteContext.CONTEXT_INSERT_ASSIGN;
                            break;
                        case 4: //after value, suggest to
                            mCompleteContext = CompleteContext.CONTEXT_INSERT_TO;
                            break;
                        case 6: //after value, insert do
                            mCompleteContext = CompleteContext.CONTEXT_INSERT_DO;
                            break;
                    }
                }
            } else if (last instanceof ToToken) {
                mCompleteContext = CompleteContext.CONTEXT_NEED_TYPE_INTEGER;
            } else if (first instanceof UsesToken) {
                if (statement.size() == 1 || last instanceof CommaToken) { //only uses token or after ','
                    mCompleteContext = CompleteContext.CONTEXT_USES;
                }
            } else if (last instanceof ColonToken) {
                if (statement.size() >= 2) {
                    if (statement.get(statement.size() - 2) instanceof WordToken) {
                        mCompleteContext = CompleteContext.CONTEXT_AFTER_COLON;
                    }
                }
            } else if (last instanceof AssignmentToken) {
                if (statement.size() >= 2) {
                    Token token = statement.get(statement.size() - 2);
                    if (token instanceof WordToken) {
                        mIdentifierType = ((WordToken) token).getName();
                        mCompleteContext = CompleteContext.CONTEXT_NEED_TYPE;
                    }
                }
            }
        } else {
            Token token = mStatement.getSeparator();
            if (token instanceof BeginEndToken) {
                mCompleteContext = CompleteContext.CONTEXT_AFTER_BEGIN;
            }
        }

    }

    private boolean beforeCursor(LineInfo line) {
        return line != null && line.getLine() <= mCursorLine && line.getColumn() <= mCursorCol;
    }

    @Nullable
    public ParsingException getParsingException() {
        return mParsingException;
    }

}
