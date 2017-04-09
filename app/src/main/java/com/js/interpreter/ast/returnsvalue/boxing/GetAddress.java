package com.js.interpreter.ast.returnsvalue.boxing;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class GetAddress extends DebuggableReturnsValue {
    final ReturnsValue target;

    final SetValueExecutable setTarget;
    LineInfo line;

    public GetAddress(ReturnsValue target) throws UnAssignableTypeException {
        this.target = target;
        setTarget = target.createSetValueInstruction(target);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getType(f).declType),
                false);
    }

    @Override
    public LineInfo getline() {
        return target.getline();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(this);
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        final ConstantAccess ca = new ConstantAccess(null, line);
        final VariableContext ff = f;
        final RuntimeExecutable<?> fmain = main;
        setTarget.setAssignedValue(ca);
        return new VariableBoxer() {

            @Override
            public void set(Object value) {
                ca.constant_value = value;
                try {
                    setTarget.execute(ff, fmain);
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object get() throws RuntimePascalException {
                return target.getValue(ff, fmain);
            }

            @Override
            public Reference clone() {
                return this;
            }
        };
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new GetAddress(target.compileTimeExpressionFold(context));
    }
}
