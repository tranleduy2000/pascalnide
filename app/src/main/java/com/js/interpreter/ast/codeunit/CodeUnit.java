package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.pascaltypes.SystemConstants;
import com.duy.pascal.backend.tokenizer.NewLexer;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
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
    private DebugListener debugListener;


    public CodeUnit(ListMultimap<String, AbstractFunction> functionTable) {
        prepareForParsing();
        this.context = getExpressionContextInstance(functionTable);
        SystemConstants.addSystemConstant(context);
        SystemConstants.addSystemType(context);
    }


    public CodeUnit(Reader program,
                    ListMultimap<String, AbstractFunction> functionTable,
                    String sourcename, List<ScriptSource> includeDirectories,
                    DebugListener debugListener)
            throws ParsingException {
        this(functionTable);
        this.debugListener = debugListener;
        NewLexer grouper = new NewLexer(program, sourcename, includeDirectories);
        grouper.parse();
        parseTree(grouper.token_queue);

    }

//    private void debug() {
//        if (DEBUG) {
//            List<VariableDeclaration> unitVarDefs = context.getVariables();
//            for (VariableDeclaration variableDeclaration : unitVarDefs) {
//                System.out.println(variableDeclaration.get_name());
//            }
//        }
//    }

    protected CodeUnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> ftable) {
        return new CodeUnitExpressionContext(ftable);
    }

    void parseTree(GrouperToken tokens) throws ParsingException {
        while (tokens.hasNext()) {
            context.addNextDeclaration(tokens);
        }
    }

    private void prepareForParsing() {
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> run();

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    protected class CodeUnitExpressionContext extends ExpressionContextMixin {
        protected CodeUnitExpressionContext(
                ListMultimap<String, AbstractFunction> function) {
            super(CodeUnit.this, null, function);
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
