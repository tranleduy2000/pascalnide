package com.duy.pascal.interperter.declaration.lang.types;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.boxing.ArrayBoxer;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VarargsType implements ArgumentType {

    private RuntimeType elementType;

    public VarargsType(RuntimeType elementType) {
        this.elementType = elementType;
    }

    @Override
    public RuntimeValue convertArgType(Iterator<RuntimeValue> args,
                                       ExpressionContext f) throws Exception {
        List<RuntimeValue> convertedArgs = new ArrayList<>();
        LineInfo line = null;
        while (args.hasNext()) {
            RuntimeValue tmp = elementType.convert(args.next(), f);
            if (tmp == null) {
                return null;
            }
            line = tmp.getLineNumber();
            convertedArgs.add(tmp);
        }
        return new ArrayBoxer(convertedArgs.toArray(new RuntimeValue[convertedArgs.size()]),
                elementType, line);
    }

    @Override
    public String toString() {
        return "args...";
    }

    @Override
    public Class getRuntimeClass() {
        return elementType.getClass();
    }

    @Override
    public RuntimeValue perfectFit(Iterator<RuntimeValue> types,
                                   ExpressionContext e) throws Exception {
        LineInfo line = null;
        List<RuntimeValue> converted = new ArrayList<>();
        while (types.hasNext()) {
            RuntimeValue fit = elementType.perfectFit(types, e);
            if (fit == null) {
                return null;
            }
            if (line == null) {
                line = fit.getLineNumber();
            }
            converted.add(fit);
        }
        RuntimeValue[] convert = converted.toArray(new RuntimeValue[converted.size()]);
        return new ArrayBoxer(convert, elementType, line);
    }
}
