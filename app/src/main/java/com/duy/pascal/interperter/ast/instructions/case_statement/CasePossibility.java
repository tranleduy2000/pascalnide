package com.duy.pascal.interperter.ast.instructions.case_statement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

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
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return commands.execute(context, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return conditions[0].getLineNumber();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        return new CasePossibility(conditions, commands.compileTimeConstantTransform(c));
    }
}
