package com.js.interpreter.ast.expressioncontext;

import com.duy.pascal.backend.exceptions.SameNameException;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;

import java.util.List;

public abstract class HeirarchicalExpressionContext implements
        ExpressionContext {
    ExpressionContext parent;
    CodeUnit root;


    public HeirarchicalExpressionContext(CodeUnit root, ExpressionContext parent) {
        this.parent = parent;
        this.root = root;
    }


    protected abstract ConstantDefinition getConstantDefinitionLocal(
            String ident);

    @Override
    public ConstantDefinition getConstantDefinition(String ident) {
        ConstantDefinition result = getConstantDefinitionLocal(ident);
        if (result == null && parent != null) {
            result = parent.getConstantDefinition(ident);
        }
        return result;
    }

    protected abstract DeclaredType getTypedefTypeLocal(String ident);

    @Override
    public DeclaredType getTypedefType(String ident) {
        DeclaredType result = getTypedefTypeLocal(ident);
        if (result == null && parent != null) {
            result = parent.getTypedefType(ident);
        }
        return result;
    }

    protected abstract void verifyNonConflictingSymbolLocal(NamedEntity n)
            throws SameNameException;

    @Override
    public void verifyNonConflictingSymbol(NamedEntity n)
            throws SameNameException {
        verifyNonConflictingSymbolLocal(n);
        //Don't check with parent, because pascal allows nested conflicting symbols.
    }

    protected abstract VariableDeclaration getVariableDefinitionLocal(
            String ident);

    @Override
    public VariableDeclaration getVariableDefinition(String ident) {
        VariableDeclaration result = getVariableDefinitionLocal(ident);
        if (result == null && parent != null) {
            result = parent.getVariableDefinition(ident);
        }
        return result;
    }

    protected abstract List<AbstractFunction> getCallableFunctionsLocal(String name);

    @Override
    public void getCallableFunctions(String name, List<List<AbstractFunction>> sofar) {
        List<AbstractFunction> mine = getCallableFunctionsLocal(name);
        if (mine.size() != 0) {
            sofar.add(mine);
        }
        if (parent != null) {
            parent.getCallableFunctions(name, sofar);
        }
    }

    protected abstract boolean functionExistsLocal(String name);

    @Override
    public boolean functionExists(String name) {
        return functionExistsLocal(name)
                || (parent != null && parent.functionExists(name));
    }
}
