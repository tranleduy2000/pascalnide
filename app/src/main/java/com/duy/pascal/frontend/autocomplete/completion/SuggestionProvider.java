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

package com.duy.pascal.frontend.autocomplete.completion;

import android.support.annotation.Nullable;
import android.util.Log;

import com.duy.pascal.frontend.autocomplete.completion.model.ConstantDescription;
import com.duy.pascal.frontend.autocomplete.completion.model.Description;
import com.duy.pascal.frontend.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.frontend.autocomplete.completion.model.FunctionDescription;
import com.duy.pascal.frontend.autocomplete.completion.model.VariableDescription;
import com.duy.pascal.frontend.editor.view.CodeSuggestsEditText;
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
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Duy on 17-Aug-17.
 */

public class SuggestionProvider {
    private static final String TAG = "SuggestionProvider";
    private String source;
    private int cursorPos;
    private int cursorLine;
    private int cursorCol;
    private String incomplete;
    private CodeSuggestsEditText.SymbolsTokenizer symbolsTokenizer;
    @Nullable
    private ParsingException parsingException;

    public SuggestionProvider() {
        symbolsTokenizer = new CodeSuggestsEditText.SymbolsTokenizer();
        incomplete = "";
    }

    public ArrayList<Description> getSuggestion(String srcPath, String source,
                                                int cursorPos, int cursorLine, int cursorCol) {
        this.source = source;
        this.cursorPos = cursorPos;
        this.cursorLine = cursorLine;
        this.cursorCol = cursorCol;
        try {
            calculateIncomplete();
            ArrayList<Description> suggestItems = new ArrayList<>();
            PascalProgramDeclaration pascalProgram;
            try {
                DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
                pascalProgram = PascalCompiler.loadPascal(srcPath, new StringReader(source), null, null, diagnosticCollector);

                //the result
                addSuggestFrom(suggestItems, pascalProgram.getContext());
                parsingException = null;
            } catch (CodeUnitParsingException e) { //parsing error
                addSuggestFrom(suggestItems, e.getCodeUnit().getContext());
                parsingException = e.getParseException();
            } catch (Exception e) {
            }

            suggestItems.addAll(sort(getKeyword()));
            return suggestItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public ParsingException getParsingException() {
        return parsingException;
    }

    private void addSuggestFrom(ArrayList<Description> suggestItems, ExpressionContextMixin exprContext) {

        ArrayList<VariableDeclaration> variables = exprContext.getVariables();
        suggestItems.addAll(sort(filterVariables(variables)));

        Map<Name, ConstantDefinition> constants = exprContext.getConstants();
        suggestItems.addAll(sort(filterConst(constants)));

        ArrayListMultimap<Name, AbstractFunction> callableFunctions = exprContext.getCallableFunctions();
        suggestItems.addAll(sort(filterFunctions(callableFunctions)));
    }

    private ArrayList<Description> getKeyword() {
        ArrayList<Description> suggestItems = new ArrayList<>();
        if (incomplete.isEmpty()) return suggestItems;
        for (String s : KeyWord.ALL_KEY_WORD) {
            if (s.toLowerCase().startsWith(incomplete.toLowerCase())
                    && !s.equalsIgnoreCase(incomplete)) {
                suggestItems.add(new DescriptionImpl(DescriptionImpl.KIND_KEYWORD, s));
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
        if (incomplete.isEmpty()) return new ArrayList<>();

        ArrayList<Description> suggestItems = new ArrayList<>();

        for (Map.Entry<Name, ConstantDefinition> entry : constants.entrySet()) {
            ConstantDefinition constant = entry.getValue();
            LineInfo line = constant.getLineNumber();
            if (constant.getName().isPrefix(incomplete)) {
                if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
                    suggestItems.add(new ConstantDescription(constant));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterVariables(ArrayList<VariableDeclaration> variables) {
        if (incomplete.isEmpty()) return new ArrayList<>();
        ArrayList<Description> suggestItems = new ArrayList<>();
        for (VariableDeclaration variable : variables) {
            if (variable.getName().isPrefix(incomplete)) {
                LineInfo line = variable.getLineNumber();
                if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
                    Name name = variable.getName();
                    suggestItems.add(new VariableDescription(name, variable.getDescription(), variable.getType()));
                }
            }
        }
        return suggestItems;
    }

    private ArrayList<Description> filterFunctions(
            ArrayListMultimap<Name, AbstractFunction> allFunctions) {
        if (incomplete.isEmpty()) return new ArrayList<>();
        ArrayList<Description> suggestItems = new ArrayList<>();
        Collection<ArrayList<AbstractFunction>> values = allFunctions.values();
        for (ArrayList<AbstractFunction> list : values) {
            for (AbstractFunction function : list) {
                if (function.getName().isPrefix(incomplete)) {
                    LineInfo line = function.getLineNumber();
                    if (line != null && line.getLine() <= cursorLine && line.getColumn() <= cursorCol) {
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
}
