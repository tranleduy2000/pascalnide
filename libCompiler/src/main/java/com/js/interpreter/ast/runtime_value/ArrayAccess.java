package com.js.interpreter.ast.runtime_value;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.ArrayReference;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.IndexOutOfBoundsException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayAccess extends DebuggableAssignableValue {
    private RuntimeValue container;
    private RuntimeValue index;
    private int offset;

    public ArrayAccess(RuntimeValue container, RuntimeValue index, int offset) {
        this.container = container;
        this.index = index;
        this.offset = offset;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType r = (container.getType(f));
        return new RuntimeType(((ArrayType<?>) r.declType).elementType,
                r.writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return index.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object cont = container.compileTimeValue(context);
        Object ind = index.compileTimeValue(context);
        if (ind == null || cont == null) {
            return null;
        } else {
            return Array.get(cont, ((int) ind) - offset);
        }
    }


    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        int ind = Integer.valueOf(index.getValue(f, main).toString());
        try {
            return Array.get(cont, ind - offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(this.getLineNumber(),
                    ind, offset, offset + ((Object[]) cont).length - 1);
        }
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        int ind = Integer.valueOf(index.getValue(f, main).toString());
        return new ArrayReference(cont, ind, offset);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new ArrayAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context), offset);
    }

}
