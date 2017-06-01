package com.duy.pascal.backend.ast.runtime_value.value;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.data_types.set.ArrayType;
import com.duy.pascal.backend.data_types.set.EnumElementValue;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.runtime_exception.IndexOutOfBoundsException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.ast.runtime_value.references.ArrayIndexReference;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;

import java.lang.reflect.Array;

public class ArrayIndexAccess extends DebuggableAssignableValue {
    private RuntimeValue container;
    private RuntimeValue index;
    private int offset;

    public ArrayIndexAccess(RuntimeValue container, RuntimeValue index, int offset) {
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
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Object i = index.getValue(f, main);
        int ind;
        if (i instanceof EnumElementValue) {
            ind = ((EnumElementValue) i).getIndex();
        } else {
            ind = Integer.parseInt(i.toString());
        }
        try {
            return Array.get(cont, ind - offset);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(this.getLineNumber(),
                    ind, offset, offset + ((Object[]) cont).length - 1);
        }
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Object value = index.getValue(f, main);
        int ind = 0;
        if (value instanceof Integer) {
            ind = (int) value;
        } else if (value instanceof EnumElementValue) {
            ind = ((EnumElementValue) value).getIndex();
        }
        return new ArrayIndexReference(cont, ind, offset);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new ArrayIndexAccess(container.compileTimeExpressionFold(context),
                index.compileTimeExpressionFold(context), offset);
    }

}
