package com.js.interpreter.ast.codeunit;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.ExecuteActivity;
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
    private ExecuteActivity executeActivity;

    public PascalProgram(Reader program,
                         ListMultimap<String, AbstractFunction> functionTable,
                         String sourceName, List<ScriptSource> includeDirectories,
                         ExecuteActivity executeActivity)
            throws ParsingException {
        super(program, functionTable, sourceName, includeDirectories, executeActivity);
        this.executeActivity = executeActivity;
    }

    @Override
    protected PascalProgramExpressionContext getExpressionContextInstance(
            ListMultimap<String, AbstractFunction> functionTable, ExecuteActivity executeActivity) {
        return new PascalProgramExpressionContext(functionTable, executeActivity);
    }

    @Override
    public RuntimeExecutable<PascalProgram> run() {
        return new RuntimePascalProgram(this, executeActivity);
    }

    protected class PascalProgramExpressionContext extends CodeUnitExpressionContext {
        protected PascalProgramExpressionContext(
                ListMultimap<String, AbstractFunction> f, ExecuteActivity executeActivity) {
            super(f, executeActivity);
        }

        @Override
        public void handleBeginEnd(GrouperToken i) throws ParsingException {
            if (main != null) {
                throw new MultipleDefinitionsMainException(i.peek().lineInfo, "Multiple definitions of main.");
            }
            main = i.getNextCommand(this);
            if (!(i.peek() instanceof PeriodToken)) {
                throw new ExpectedTokenException(".", i.peek());
            }
            i.take();
        }
    }

}
