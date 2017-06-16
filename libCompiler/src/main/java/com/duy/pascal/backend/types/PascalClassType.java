package com.duy.pascal.backend.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.ConstantDefinition;
import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.codeunit.classunit.ClassExpressionContext;
import com.duy.pascal.backend.ast.codeunit.classunit.PascalClassDeclaration;
import com.duy.pascal.backend.ast.codeunit.classunit.RuntimePascalClass;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.frontend.DLog;

import java.util.ArrayList;
import java.util.List;

public class PascalClassType extends ObjectType {

    private static final String TAG = "ClassType";
    private PascalClassDeclaration mPascalClass;
    private ExpressionContext parent;

    public PascalClassType(CodeUnit root, ExpressionContext parent) throws ParsingException {
        this.parent = parent;
        DLog.d(TAG, "ClassType() called with: root = [" + root + "], parent = [" + parent + "]");
        mPascalClass = new PascalClassDeclaration(root, parent, null);
    }

    public ClassExpressionContext getClassContext() {
        return (ClassExpressionContext) mPascalClass.getContext();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @NonNull
    @Override
    public Object initialize() {
        return new RuntimePascalClass(mPascalClass);
    }

    @Nullable
    @Override
    public Class getTransferClass() {
        return RuntimePascalClass.class;
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws ParsingException {
        return null;
    }

    @Override
    public boolean equals(DeclaredType other) {
        return false;
    }

    @Nullable
    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return r;
    }

    @Nullable
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(lineInfo, this);
    }

    @Nullable
    @Override
    public Class<?> getStorageClass() {
        return RuntimePascalClass.class;
    }

    public void addPrivateFunction(FunctionDeclaration functionDeclaration) {
        mPascalClass.context.declareFunction(functionDeclaration);
    }

    public void addPublicFunction(FunctionDeclaration functionDeclaration) {
        mPascalClass.context.declareFunction(functionDeclaration);
    }

    @Override
    public DeclaredType getMemberType(String name) throws ParsingException {
        ExpressionContextMixin context = mPascalClass.getContext();

        VariableDeclaration var = context.getVariableDefinitionLocal(name);
        if (var != null) return var.getType();

        ConstantDefinition constant = context.getConstantDefinitionLocal(name);
        if (constant != null) return constant.getType();

        return null;
    }

    @Override
    public String getEntityType() {
        return "class";
    }


    public void addPrivateField(ArrayList<VariableDeclaration> vars) {
        for (VariableDeclaration var : vars) {
            mPascalClass.context.declareVariable(var);
        }
        // TODO: 16-Jun-17
    }

    public void addPublicFields(List<VariableDeclaration> vars) {
        // TODO: 16-Jun-17
        for (VariableDeclaration var : vars) {
            mPascalClass.context.declareVariable(var);
        }
    }


}
