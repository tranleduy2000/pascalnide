package com.js.interpreter.ast.returnsvalue.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.value.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.AssignableValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class GetAddress extends DebuggableReturnValue {
    private final AssignableValue target;
    private LineInfo line;

    public GetAddress(AssignableValue target) throws UnAssignableTypeException {
        this.line = target.getLineNumber();
        this.target = target;
        this.outputFormat = target.getOutputFormat();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getType(f).declType),
                false);
    }

    @Override
    public LineInfo getLineNumber() {
        return target.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public ReturnValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }
}
