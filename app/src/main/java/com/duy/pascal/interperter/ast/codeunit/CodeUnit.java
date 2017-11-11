package com.duy.pascal.interperter.ast.codeunit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.config.ProgramConfig;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokenizer.GroupParser;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.ui.runnable.ProgramHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class CodeUnit {
    public ExpressionContextMixin mContext;
    protected ProgramConfig config = new ProgramConfig();
    private ScriptSource mSource;
    @NonNull
    private List<ScriptSource> mIncludeDirectories = new ArrayList<>();

    public CodeUnit() {
    }

    public CodeUnit(@Nullable ProgramHandler handler) {
        this.mContext = createExpressionContext(handler);
    }

    public CodeUnit(@NonNull ScriptSource source, @Nullable List<ScriptSource> include,
                    @Nullable ProgramHandler handler)
            throws Exception {
        this.mSource = source;
        this.mContext = createExpressionContext(handler);
        if (include != null) {
            this.mIncludeDirectories = include;
        }

        long time = System.currentTimeMillis();
        GroupParser lexer;
        try {
            lexer = new GroupParser(source, include);
            lexer.parse();
        } catch (ParsingException e) {
            throw new CodeUnitParsingException(this, e);
        }
        parseTree(lexer.getTokenQueue());
        System.out.println("parse time " + (System.currentTimeMillis() - time));
    }

    public ScriptSource getSource() {
        return mSource;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ProgramConfig getConfig() {
        return config;
    }

    public String getSourceName() {
        return mSource.getName();
    }

    public ExpressionContextMixin getContext() {
        return mContext;
    }

    public ExpressionContextMixin getProgram() {
        return mContext;
    }

    protected abstract ExpressionContextMixin createExpressionContext(ProgramHandler handler);

    private void parseTree(GrouperToken tokens) throws Exception {
        try {
            while (tokens.hasNext()) {
                mContext.addNextDeclaration(tokens);
            }
        } catch (ParsingException e) {
            throw new CodeUnitParsingException(this, e);
        }
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> generate();

    @Nullable
    public Name getProgramName() {
        return Name.create(mSource.getName());
    }

    public List<ScriptSource> getIncludeDirectories() {
        return mIncludeDirectories;
    }

    protected static abstract class CodeUnitExpressionContext extends ExpressionContextMixin {
        public CodeUnitExpressionContext(@NonNull CodeUnit root, @NonNull ProgramHandler handler) {
            super(root, null, handler);
        }

        @Override
        protected Node handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws Exception {
            throw new UnrecognizedTokenException(next);
        }


        @Override
        protected void handleBeginEnd(GrouperToken i) throws Exception {
            i.take();
        }

    }

}
