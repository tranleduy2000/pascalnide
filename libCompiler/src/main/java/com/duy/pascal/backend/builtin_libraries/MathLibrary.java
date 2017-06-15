package com.duy.pascal.backend.builtin_libraries;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;

import java.util.Map;

/**
 * Created by Duy on 08-May-17.
 */

public class MathLibrary implements IPascalLibrary {
    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    /**
     * @return the arc cosine of x value;
     */
    @PascalMethod(description = "Return the arc cosine of x value;")
    public double arccos(double x) {
        return Math.acos(x);
    }
}
