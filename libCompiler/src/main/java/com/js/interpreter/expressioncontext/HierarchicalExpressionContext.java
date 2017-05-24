package com.js.interpreter.expressioncontext;

import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.CodeUnit;
import com.js.interpreter.codeunit.library.RuntimeUnitPascal;
import com.js.interpreter.codeunit.library.UnitPascal;

import java.util.List;
import java.util.Map;

public abstract class HierarchicalExpressionContext implements
        ExpressionContext {
    protected ExpressionContext parent;
    protected CodeUnit root;


    public HierarchicalExpressionContext(CodeUnit root, ExpressionContext parent) {
        this.parent = parent;
        this.root = root;
    }


    protected abstract ConstantDefinition getConstantDefinitionLocal(String indent);

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
    public VariableDeclaration getVariableDefinition(String indent) {

        VariableDeclaration result = getVariableDefinitionLocal(indent);
        if (result != null) return result;
        if (parent != null) {
            result = parent.getVariableDefinition(indent);
            if (result != null) return result;
        }

        //find variable in library
        ExpressionContextMixin context = (ExpressionContextMixin) this;

        //check all library
        for (Map.Entry<UnitPascal, RuntimeUnitPascal> unit : context.getUnitsMap().entrySet()) {
            RuntimeUnitPascal value = unit.getValue();
            ExpressionContextMixin libContext = value.getDefinition().getContext();
            result = libContext.getVariableDefinition(indent);
        }

        return result;
    }

    protected abstract List<AbstractFunction> getCallableFunctionsLocal(
            String name);

    @Override
    public void getCallableFunctions(String name,
                                     List<List<AbstractFunction>> sofar) {
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
