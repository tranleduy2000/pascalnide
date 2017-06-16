package com.duy.pascal.backend.ast.instructions.case_statement;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

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
                                       RuntimeExecutableCodeUnit<?> main, String contextName) throws RuntimePascalException {
        return commands.execute(context, main, contextName);
    }

    @Override
    public LineInfo getLineNumber() {
        return conditions[0].getLineNumber();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new CasePossibility(conditions, commands.compileTimeConstantTransform(c));
    }
}
