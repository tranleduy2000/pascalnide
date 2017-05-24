package com.js.interpreter.codeunit;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.backend.tokenizer.NewLexer;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.source_include.ScriptSource;

import java.io.Reader;
import java.util.List;

public abstract class CodeUnit {
    public final ExpressionContextMixin mContext;
    protected String programName;

    public CodeUnit(ListMultimap<String, AbstractFunction> functionTable,
                    RunnableActivity handler) {
        this.mContext = getExpressionContextInstance(functionTable, handler);
    }

    public CodeUnit(Reader program, ListMultimap<String, AbstractFunction> functionTable,
                    String sourceName, List<ScriptSource> includeDirectories,
                    @Nullable RunnableActivity handler)
            throws ParsingException {
        this(functionTable, handler);
        NewLexer lexer = new NewLexer(program, sourceName, includeDirectories);
        lexer.parse();
        parseTree(lexer.getTokenQueue());
    }

    public ExpressionContextMixin getContext() {
        return mContext;
    }

    public ExpressionContextMixin getProgram() {
        return mContext;
    }

    protected abstract CodeUnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler);

    private void parseTree(GrouperToken tokens) throws ParsingException {
        while (tokens.hasNext()) {
            mContext.addNextDeclaration(tokens);
        }
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> run();

    public String getProgramName() {
        return programName;
    }

    protected abstract class CodeUnitExpressionContext extends ExpressionContextMixin {
        public CodeUnitExpressionContext(ListMultimap<String, AbstractFunction> functionTable,
                                         @Nullable RunnableActivity handler,
                                         boolean isLibrary) {
            super(CodeUnit.this, null, functionTable, handler, isLibrary);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }


        @Override
        protected void handleBeginEnd(GrouperToken i) throws ParsingException {
            i.take();
        }

    }

}
