package com.duy.pascal.interperter.ast.expressioncontext;

import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.codeunit.RuntimeUnitPascal;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.declaration.lang.types.Type;

import java.util.List;
import java.util.Map;

public abstract class HierarchicalExpressionContext implements ExpressionContext {
    protected ExpressionContext parent;

    public ExpressionContext getParentContext() {
        return parent;
    }

    protected CodeUnit root;

    public HierarchicalExpressionContext(CodeUnit root, ExpressionContext parent) {
        this.parent = parent;
        this.root = root;
    }

    /**
     * This method will be check duplicate identifier
     */
    abstract void verifyNonConflictingSymbolLocal(NamedEntity n)
            throws DuplicateIdentifierException;

    @Override
    public ConstantDefinition getConstantDefinition(String identifier) {
        ConstantDefinition result = getConstantDefinitionLocal(identifier);
        if (result == null && parent != null) {
            result = parent.getConstantDefinition(identifier);
        }
        return result;
    }


    @Override
    public Type getTypeDef(String identifier) {
        Type result = getTypedefTypeLocal(identifier);
        if (result == null && parent != null) {
            result = parent.getTypeDef(identifier);
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
        for (Map.Entry<PascalUnitDeclaration, RuntimeUnitPascal> unit : context.getRuntimeUnitMap().entrySet()) {
            RuntimeUnitPascal value = unit.getValue();
            ExpressionContextMixin libContext = value.getDeclaration().getContext();
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
