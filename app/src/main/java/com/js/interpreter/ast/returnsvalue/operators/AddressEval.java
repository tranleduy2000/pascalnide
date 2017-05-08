package com.js.interpreter.ast.returnsvalue.operators;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.LeftValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class AddressEval extends DebuggableReturnValue {
    final LeftValue target;
    final LineInfo line;

    public AddressEval(LeftValue target, LineInfo line) {
        this.target = target;
        this.line = line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getType(f).declType), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        return null;
    }

    @Override
    public ReturnValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return this;
    }
}
