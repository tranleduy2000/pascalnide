package com.js.interpreter.ast.returnsvalue;


import com.duy.pascal.backend.debugable.DebuggableLeftValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringIndex extends DebuggableLeftValue {
    ReturnValue string;
    ReturnValue index;

    public StringIndex(ReturnValue string, ReturnValue index) {
        this.index = index;
        this.string = string;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return str.charAt(ind - 1);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return new StringIndexReference(str, ind);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        boolean writable = string.getType(f).writable;
        return new RuntimeType(BasicType.Character, writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        StringBuilder str = (StringBuilder) string.compileTimeValue(context);
        int ind = (int) index.compileTimeValue(context);
        return str.charAt(ind - 1);
    }

    @Override
    public ReturnValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        ReturnValue cstr = string.compileTimeExpressionFold(context);
        ReturnValue cind = index.compileTimeExpressionFold(context);
        return new StringIndex(cstr, cind);
    }
}
