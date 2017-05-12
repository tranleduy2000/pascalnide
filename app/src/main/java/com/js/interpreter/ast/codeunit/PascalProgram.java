package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.codeunit.RuntimePascalProgram;

import java.io.Reader;
import java.util.List;

public class PascalProgram extends ExecutableCodeUnit {
    public Executable main;

    private FunctionOnStack mainRunning;
    private RunnableActivity handler;

    public PascalProgram(Reader program,
                         ListMultimap<String, AbstractFunction> functionTable,
                         String sourceName, List<ScriptSource> includeDirectories,
                         RunnableActivity handler)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories, handler);
        this.handler = handler;
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, RunnableActivity handler) {
        return new PascalProgramExpressionContext(functionTable, handler);
    }

    @Override
    public RuntimeExecutable<PascalProgram> run() {
        return new RuntimePascalProgram(this, handler);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        public PascalProgramExpressionContext(
                ListMultimap<String, AbstractFunction> f, RunnableActivity handler) {
            super(f, handler);
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            if (main != null) {
                throw new MultipleDefinitionsMainException(i.peek().lineInfo);
            }
            main = i.getNextCommand(this);
            if (!(i.peek() instanceof PeriodToken)) {
                throw new ExpectedTokenException(".", i.peek());
            }
            i.take();
        }
    }

}
