package com.js.interpreter.ast.codeunit;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnrecognizedTokenException;
import com.duy.interpreter.pascaltypes.SystemConstants;
import com.duy.interpreter.tokenizer.NewLexer;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.basic.ProgramToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeCodeUnit;

import java.io.Reader;
import java.util.List;

public abstract class CodeUnit {
    public final ExpressionContextMixin context;
    private String program_name;

    private final boolean DEBUG = true;
    public CodeUnit(ListMultimap<String, AbstractFunction> functionTable) {
        prepareForParsing();
        this.context = getExpressionContextInstance(functionTable);
        SystemConstants.addSystemConstant(context);
        SystemConstants.addSystemType(context);
    }

    private void debug() {
        if (DEBUG){
            List<VariableDeclaration> unitVarDefs = context.getUnitVarDefs();
            for (VariableDeclaration variableDeclaration: unitVarDefs){
                System.out.println(variableDeclaration.get_name());
            }
        }
    }

    public CodeUnit(Reader program,
                    ListMultimap<String, AbstractFunction> functionTable,
                    String sourcename, List<ScriptSource> includeDirectories)
            throws ParsingException {
        this(functionTable);
        NewLexer grouper = new NewLexer(program, sourcename, includeDirectories);
//        new Thread(grouper).start();
        grouper.parse();
        parse_tree(grouper.token_queue);
        debug();
    }

    protected CodeUnitExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> ftable) {
        return new CodeUnitExpressionContext(ftable);
    }

    void parse_tree(GrouperToken tokens) throws ParsingException {
        while (tokens.hasNext()) {
            context.add_next_declaration(tokens);
        }
    }

    private void prepareForParsing() {
    }

    public abstract RuntimeCodeUnit<? extends CodeUnit> run();

    protected class CodeUnitExpressionContext extends ExpressionContextMixin {
        protected CodeUnitExpressionContext(
                ListMultimap<String, AbstractFunction> function) {
            super(CodeUnit.this, null, function);
        }

        @Override
        protected Executable handleUnrecognizedStatementImpl(Token next,
                                                             GrouperToken container) throws ParsingException {
            throw new UnrecognizedTokenException(next);
        }

        @Override
        protected boolean handleUnrecognizedDeclarationImpl(Token next,
                                                            GrouperToken i) throws ParsingException {
            if (next instanceof ProgramToken) {
                CodeUnit.this.program_name = i.next_word_value();
                i.assert_next_semicolon();
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
