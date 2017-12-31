package com.duy.pascal.interperter.ast.node.case_statement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

class CasePossibility extends DebuggableNode {
    private final Node commands;
    /**
     * This class represents a line in a case statement.
     */
    CaseCondition[] conditions;

    CasePossibility(CaseCondition[] conditions, Node commands) {
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
        return commands.visit(context, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return conditions[0].getLineNumber();
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        return new CasePossibility(conditions, commands.compileTimeConstantTransform(c));
    }
}
