package com.duy.pascal.backend.ast.expressioncontext;

import com.duy.pascal.backend.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.ast.ConstantDefinition;
import com.duy.pascal.backend.ast.NamedEntity;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.codeunit.library.RuntimeUnitPascal;
import com.duy.pascal.backend.ast.codeunit.library.UnitPascal;

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

    abstract void verifyNonConflictingSymbolLocal(NamedEntity n)
            throws DuplicateIdentifierException;
    @Override
    public ConstantDefinition getConstantDefinition(String ident) {
        ConstantDefinition result = getConstantDefinitionLocal(ident);
        if (result == null && parent != null) {
            result = parent.getConstantDefinition(ident);
        }
        return result;
    }


    @Override
    public DeclaredType getTypedefType(String ident) {
        DeclaredType result = getTypedefTypeLocal(ident);
        if (result == null && parent != null) {
            result = parent.getTypedefType(ident);
        }
        return result;
    }

    @Override
    public void verifyNonConflictingSymbol(NamedEntity n)
            throws DuplicateIdentifierException {
        verifyNonConflictingSymbolLocal(n);
        //Don't check with parent, because pascal allows nested conflicting symbols.
    }

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


    @Override
    public boolean functionExists(String name) {
        return functionExistsLocal(name)
                || (parent != null && parent.functionExists(name));
    }
}
