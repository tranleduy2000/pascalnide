package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

class CasePossibility extends DebuggableExecutable {
    private final Executable commands;
    /**
     * This class represents a line in a case statement.
     */
    Containable[] conditions;

    CasePossibility(Containable[] conditions, Executable commands) {
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
            throws ParsingException {
        return new CasePossibility(conditions, commands.compileTimeConstantTransform(c));
    }
}
