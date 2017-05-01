package com.js.interpreter.ast.returnsvalue;


import com.duy.pascal.backend.debugable.DebuggableLValue;
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

public class StringIndex extends DebuggableLValue {
    RValue string;
    RValue index;

    public StringIndex(RValue string, RValue index) {
        this.index = index;
        this.string = string;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return str.charAt(ind);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        StringBuilder str = (StringBuilder) string.getValue(f, main);
        int ind = (int) index.getValue(f, main);
        return new StringIndexReference(str, ind);
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        boolean writable = string.get_type(f).writable;
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
        return str.charAt(ind);
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RValue cstr = string.compileTimeExpressionFold(context);
        RValue cind = index.compileTimeExpressionFold(context);
        return new StringIndex(cstr, cind);
    }
}
