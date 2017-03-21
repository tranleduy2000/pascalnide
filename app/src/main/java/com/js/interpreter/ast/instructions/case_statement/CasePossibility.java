package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.instructions.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class CasePossibility extends DebuggableExecutable {
    final Executable commands;
    /**
     * This class represents a line in a case statement.
     */
    CaseCondition[] conditions;

    public CasePossibility(CaseCondition[] conditions, Executable commands) {
        this.conditions = conditions;
        this.commands = commands;
    }

    /**
     * Executes the contained commands in this branch.
     *
     * @param value the value being examined in this case statement.
     * @return Whether or not it has broken.
     */
    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        return commands.execute(f, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return conditions[0].getLineNumber();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new CasePossibility(conditions,
                commands.compileTimeConstantTransform(c));
    }
}
