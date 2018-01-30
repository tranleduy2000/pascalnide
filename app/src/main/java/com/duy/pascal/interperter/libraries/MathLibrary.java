package com.duy.pascal.interperter.libraries;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;

/**
 * Created by Duy on 08-May-17.
 */
@SuppressWarnings("unused")
public class MathLibrary extends PascalLibrary {
    public MathLibrary() {

    }

    @Override
    public void onFinalize() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }

    /**
     * @return the arc cosine of x value;
     */
    @PascalMethod(description = "Return the arc cosine of x value;")
    public double arccos(double x) {
        return Math.acos(x);
    }
}
