package com.duy.pascal.backend.lib.templated.abstract_class;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

import java.util.List;

public class TemplatePluginDeclaration extends AbstractFunction {

    TemplatePascalPlugin t;

    public TemplatePluginDeclaration(TemplatePascalPlugin t) {
        this.t = t;
    }

    @Override
    public LineInfo getLine() {
        return new LineInfo(-1, t.getClass().getCanonicalName());
    }

    @Override
    public String getEntityType() {
        return "Template Plugin";
    }

    @Override
    public String name() {
        return t.name();
    }

    @Override
    public ArgumentType[] getArgumentTypes() {
        return t.argumentTypes();
    }

    @Override
    public DeclaredType returnType() {
        return t.return_type();
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               List<ReturnsValue> values, ExpressionContext f)
            throws ParsingException {
        ReturnsValue[] args = this.perfectMatch(values, f);
        if (args == null) {
            return null;
        }
        return t.generatePerfectFitCall(line, args, f);
    }

    @Override
    public FunctionCall generateCall(LineInfo line, List<ReturnsValue> values,
                                     ExpressionContext f) throws ParsingException {
        ReturnsValue[] args = this.formatArgs(values, f);
        if (args == null) {
            return null;
        }
        return t.generateCall(line, args, f);
    }

}
