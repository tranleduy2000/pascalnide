package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.tokenizer.NewLexer;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeCodeUnit;

import java.io.Reader;
import java.util.List;

public abstract class CodeUnit {
    public final ExpressionContextMixin context;
    private final boolean DEBUG = false;
    private String programName;
    private ExecuteActivity executeActivity;

    public CodeUnit(ListMultimap<String, AbstractFunction> functionTable,
                    ExecuteActivity executeActivity) {
        this.context = getExpressionContextInstance(functionTable, executeActivity);
    }


    public CodeUnit(Reader program, ListMultimap<String, AbstractFunction> functionTable,
                    String sourceName, List<ScriptSource> includeDirectories,
                    ExecuteActivity executeActivity)
            throws ParsingException {
        this(functionTable, executeActivity);
        this.executeActivity = executeActivity;
        NewLexer grouper = new NewLexer(program, sourceName, includeDirectories);
        grouper.parse();
        parseTree(grouper.tokenQueue);
    }

    protected CodeUnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, ExecuteActivity executeActivity) {
        return new CodeUnitExpressionContext(functionTable, executeActivity);
    }

    private void parseTree(GrouperToken tokens) throws ParsingException {
        while (tokens.hasNext()) {
            context.addNextDeclaration(tokens);
        }
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> run();

    public String getProgramName() {
        return programName;
    }

    protected class CodeUnitExpressionContext extends ExpressionContextMixin {
        public CodeUnitExpressionContext(ListMultimap<String, AbstractFunction> functionTable,
                                         ExecuteActivity executeActivity) {
            super(CodeUnit.this, null, functionTable, executeActivity);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken i)
                throws ParsingException {
            if (next instanceof ProgramToken) {
                CodeUnit.this.programName = i.next_word_value();
                i.assertNextSemicolon();
                return true;
            }
            return false;
        }

        @Override
        protected void handleBeginEnd(GrouperToken i) throws ParsingException {
            i.take();
        }

    }

}
