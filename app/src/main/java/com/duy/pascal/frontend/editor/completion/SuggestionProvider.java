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

package com.duy.pascal.frontend.editor.completion;

import android.content.Context;
import android.util.Log;

import com.duy.pascal.frontend.editor.completion.model.KeyWord;
import com.duy.pascal.frontend.editor.completion.model.SuggestItem;
import com.duy.pascal.frontend.editor.view.CodeSuggestsEditText;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.core.PascalCompiler;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractCallableFunction;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.google.common.collect.ArrayListMultimap;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Duy on 17-Aug-17.
 */

public class SuggestionProvider {
    private static final String TAG = "SuggestionProvider";
    private Context context;
    private String srcPath;
    private String source;
    private int cursorPos;
    private int cursorLine;
    private int cursorCol;
    private String incomplete;
    private CodeSuggestsEditText.SymbolsTokenizer symbolsTokenizer;

    public SuggestionProvider() {
        symbolsTokenizer = new CodeSuggestsEditText.SymbolsTokenizer();
        incomplete = "";
    }

    public ArrayList<SuggestItem> getSuggestion(Context context, String srcPath, String source,
                                                int cursorPos, int cursorLine, int cursorCol) {
        this.context = context;
        this.srcPath = srcPath;
        this.source = source;
        this.cursorPos = cursorPos;
        this.cursorLine = cursorLine;
        this.cursorCol = cursorCol;
        try {
            calculateIncomplete();
            //the result
            ArrayList<SuggestItem> suggestItems = new ArrayList<>();
            try {
                DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
                PascalProgramDeclaration pascalProgram =
                        PascalCompiler.loadPascal(srcPath, new StringReader(source), null, null, diagnosticCollector);
                ExpressionContextMixin exprContext = pascalProgram.getContext();

                ArrayList<VariableDeclaration> variables = exprContext.getVariables();
                suggestItems.addAll(sort(filterVariables(variables)));

                Map<Name, ConstantDefinition> constants = exprContext.getConstants();
                suggestItems.addAll(sort(filterConst(constants)));

                ArrayListMultimap<Name, AbstractFunction> callableFunctions = exprContext.getCallableFunctions();
                suggestItems.addAll(sort(filterFunctions(callableFunctions)));
            } catch (Exception e) { //parsing error
            }

//            HashMap<Name, Type> typedefs = exprContext.getTypedefs();
//            addTypeDef();

            suggestItems.addAll(sort(addKeyword()));
            return suggestItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<SuggestItem> addKeyword() {
        ArrayList<SuggestItem> suggestItems = new ArrayList<>();
        if (incomplete.isEmpty()) return suggestItems;
        for (String s : KeyWord.ALL_KEY_WORD) {
            if (s.toLowerCase().startsWith(incomplete.toLowerCase())
                    && !s.equalsIgnoreCase(incomplete)) {
                suggestItems.add(new SuggestItem(SuggestItem.KIND_KEYWORD, s));
            }
        }
        return suggestItems;
    }

    private void calculateIncomplete() {
        incomplete = "";
        int start = symbolsTokenizer.findTokenStart(source, cursorPos);
        incomplete = source.substring(start, cursorPos);
        incomplete = incomplete.trim();
        Log.d(TAG, "calculateIncomplete incomplete = " + incomplete);

    }

    private ArrayList<SuggestItem> sort(ArrayList<SuggestItem> items) {
        //sort by type -> name
        Collections.sort(items, new Comparator<SuggestItem>() {
            @Override
            public int compare(SuggestItem o1, SuggestItem o2) {
                if (!o1.getType().equals(o2.getType())) {
                    return o1.getType().compareTo(o2.getType());
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
        for (SuggestItem suggestItem : items) {
            System.out.println(suggestItem);
        }
        return items;
    }

    private ArrayList<SuggestItem> filterConst(Map<Name, ConstantDefinition> constants) {
        if (incomplete.isEmpty()) return new ArrayList<>();

        ArrayList<SuggestItem> suggestItems = new ArrayList<>();

        for (Map.Entry<Name, ConstantDefinition> entry : constants.entrySet()) {
            ConstantDefinition constant = entry.getValue();
            LineInfo line = constant.getLineNumber();
            if (constant.getName().isPrefix(incomplete)) {
                if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
                    Name name = constant.getName();
                    suggestItems.add(new SuggestItem(SuggestItem.KIND_VARIABLE, name, constant.getDescription()));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<SuggestItem> filterVariables(ArrayList<VariableDeclaration> variables) {
        if (incomplete.isEmpty()) return new ArrayList<>();
        ArrayList<SuggestItem> suggestItems = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            if (variable.getName().isPrefix(incomplete)) {
                LineInfo line = variable.getLineNumber();
                if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
                    Name name = variable.getName();
                    suggestItems.add(new SuggestItem(SuggestItem.KIND_VARIABLE, name, variable.getDescription()));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<SuggestItem> filterFunctions(
            ArrayListMultimap<Name, AbstractFunction> functions) {
        if (incomplete.isEmpty()) return new ArrayList<>();
        ArrayList<SuggestItem> suggestItems = new ArrayList<>();

        for (Map.Entry<Name, AbstractFunction> entry : functions.entries()) {
            AbstractCallableFunction function = (AbstractCallableFunction) entry.getValue();
            if (function.getName().isPrefix(incomplete)) {
                LineInfo line = function.getLineNumber();
                if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
                    Name name = function.getName();
                    ArgumentType[] args = function.argumentTypes();
                    Type type = function.returnType();

                }
            }
        }
        return suggestItems;
    }
}
