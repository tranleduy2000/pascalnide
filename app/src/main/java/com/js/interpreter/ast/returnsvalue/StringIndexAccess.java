package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetCharAt;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringIndexAccess extends DebuggableReturnsValue {
    private ReturnsValue container;
    private ReturnsValue index;

    public StringIndexAccess(ReturnsValue container, ReturnsValue index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Character,
                container.getType(f).writable);
    }

    @Override
    public LineInfo getline() {
        return container.getline();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        StringBuilder c = (StringBuilder) container.compileTimeValue(context);
        Integer i = (Integer) index.compileTimeValue(context);
        if (c != null && i != null) {
            return c.charAt(i - 1);
        } else {
            return null;
        }
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        return new SetCharAt(container, index, r);
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        StringBuilder c = (StringBuilder) container.getValue(f, main);
        Integer i = (Integer) index.getValue(f, main);
        return c.charAt(i - 1);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, container.getline());
        } else {
            return new StringIndexAccess(
                    container.compileTimeExpressionFold(context),
                    index.compileTimeExpressionFold(context));
        }
    }

}
