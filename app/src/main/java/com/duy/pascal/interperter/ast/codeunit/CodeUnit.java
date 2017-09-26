package com.duy.pascal.interperter.ast.codeunit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.config.ProgramConfig;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.exceptions.DiagnosticsListener;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokenizer.GroupParser;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import java.io.Reader;
import java.util.List;

public abstract class CodeUnit {
    public ExpressionContextMixin context;
    @Nullable
    protected Name programName;
    protected ProgramConfig config = new ProgramConfig();
    private String sourceName;
    private List<ScriptSource> includeDirectories;

    public CodeUnit() {

    }

    public CodeUnit(@Nullable ProgramHandler handler) {
        this.context = createExpressionContext(handler);
    }

    public CodeUnit(@NonNull Reader program, @NonNull String sourceName,
                    @Nullable List<ScriptSource> includeDirectories,
                    @Nullable ProgramHandler handler, @Nullable DiagnosticCollector diagnosticCollector)
            throws Exception {
        this(handler);
        this.context = createExpressionContext(handler);
        this.context.put(DiagnosticsListener.class, diagnosticCollector);

        this.sourceName = sourceName;
        this.includeDirectories = includeDirectories;

        long time = System.currentTimeMillis();
        GroupParser lexer;
        try {
            lexer = new GroupParser(program, sourceName, includeDirectories);
        } catch (ParsingException e) {
            throw new CodeUnitParsingException(this, e);
        }
        lexer.parse();
        parseTree(lexer.getTokenQueue());
        System.out.println("parse time " + (System.currentTimeMillis() - time));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ProgramConfig getConfig() {
        return config;
    }

    public String getSourceName() {
        return sourceName;
    }

    public ExpressionContextMixin getContext() {
        return context;
    }

    public ExpressionContextMixin getProgram() {
        return context;
    }

    protected abstract ExpressionContextMixin createExpressionContext(ProgramHandler handler);

    private void parseTree(GrouperToken tokens) throws Exception {
        try {
            while (tokens.hasNext()) {
                context.addNextDeclaration(tokens);
            }
        } catch (ParsingException e) {
            throw new CodeUnitParsingException(this, e);
        }
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> generate();

    @Nullable
    public Name getProgramName() {
        return programName;
    }

    public List<ScriptSource> getIncludeDirectories() {
        return includeDirectories;
    }

    protected abstract class CodeUnitExpressionContext extends ExpressionContextMixin {
        public CodeUnitExpressionContext(@NonNull ProgramHandler handler) {
            super(CodeUnit.this, null, handler);
        }

        public CodeUnitExpressionContext(ExpressionContext parent) {
            super(CodeUnit.this, parent);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws Exception {
            throw new UnrecognizedTokenException(next);
        }


        @Override
        protected void handleBeginEnd(GrouperToken i) throws Exception {
            i.take();
        }

    }

}
