package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.tokenizer.NewLexer;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
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
    private String programName;

    public CodeUnit(ListMultimap<String, AbstractFunction> functionTable,
                    RunnableActivity handler) {
        this.context = getExpressionContextInstance(functionTable, handler);
    }

    public CodeUnit(Reader program, ListMultimap<String, AbstractFunction> functionTable,
                    String sourceName, List<ScriptSource> includeDirectories,
                    RunnableActivity handler)
            throws ParsingException {
        this(functionTable, handler);
        NewLexer grouper = new NewLexer(program, sourceName, includeDirectories);
        grouper.parse();
        parseTree(grouper.tokenQueue);
    }

    public ExpressionContextMixin getProgram() {
        return context;
    }

    protected CodeUnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler) {
        return new CodeUnitExpressionContext(functionTable, handler);
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
                                         RunnableActivity handler) {
            super(CodeUnit.this, null, functionTable, handler);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
                throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken grouperToken)
                throws ParsingException {
            if (next instanceof ProgramToken) {
                CodeUnit.this.programName = grouperToken.next_word_value();
                grouperToken.assert_next_semicolon();
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
