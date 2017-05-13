package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.syntax.MisplacedDeclarationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeLibrary;

import java.io.Reader;
import java.util.List;

public class Library extends CodeUnit {
    public Library(ListMultimap<String, AbstractFunction> functionTable) throws ParsingException {
        super(functionTable, null);
    }

    public Library(Reader program, ListMultimap<String, AbstractFunction> functionTable,
                   String sourcename, List<ScriptSource> includeDirectories)
            throws ParsingException {
        super(program, functionTable, sourcename, includeDirectories, null);
    }

    @Override
    protected LibraryExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler) {
        return new LibraryExpressionContext(functionTable, handler);
    }

    @Override
    public RuntimeLibrary run() {
        return new RuntimeLibrary(this);
    }

    protected class LibraryExpressionContext extends CodeUnitExpressionContext {

        protected LibraryExpressionContext(ListMultimap<String, AbstractFunction> function,
                                           RunnableActivity handler) {
            super(function, handler);
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            throw new MisplacedDeclarationException(i.peek().lineInfo,
                    "main function", Library.this.context);
        }
    }

}
