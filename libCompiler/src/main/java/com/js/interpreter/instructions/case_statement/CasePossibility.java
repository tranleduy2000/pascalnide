package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

class CasePossibility extends DebuggableExecutable {
    private final Executable commands;
    /**
     * This class represents a line in a case statement.
     */
    CaseCondition[] conditions;

    CasePossibility(CaseCondition[] conditions, Executable commands) {
        this.conditions = conditions;
        this.commands = commands;
    }

    /**
     * Executes the contained commands in this branch.
     *
     * @return Whether or not it has broken.
     */
    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        return commands.execute(context, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return conditions[0].getLine();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new CasePossibility(conditions, commands.compileTimeConstantTransform(c));
    }
}
