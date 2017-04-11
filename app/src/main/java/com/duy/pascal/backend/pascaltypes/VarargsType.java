package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.boxing.ArrayBoxer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VarargsType implements ArgumentType {

    private RuntimeType elementType;

    public VarargsType(RuntimeType elementType) {
        this.elementType = elementType;
    }

    @Override
    public ReturnsValue convertArgType(Iterator<ReturnsValue> args, ExpressionContext f) throws ParsingException {
        List<ReturnsValue> convertedargs = new ArrayList<>();
        LineInfo line = null;
        while (args.hasNext()) {
            ReturnsValue tmp = elementType.convert(args.next(), f);
            if (tmp == null) {
                return null;
            }
            line = tmp.getLine();
            convertedargs.add(tmp);
        }
        return new ArrayBoxer(convertedargs.toArray(new ReturnsValue[convertedargs.size()]), elementType, line);
    }

    @Override
    public Class getRuntimeClass() {
        return elementType.getClass();
    }

    @Override
    public ReturnsValue perfectFit(Iterator<ReturnsValue> types,
                                   ExpressionContext e) throws ParsingException {
        LineInfo line = null;
        List<ReturnsValue> converted = new ArrayList<>();
        while (types.hasNext()) {
            ReturnsValue fit = elementType.perfectFit(types, e);
            if (fit == null) {
                return null;
            }
            if (line == null) {
                line = fit.getLine();
            }
            converted.add(fit);
        }
        ReturnsValue[] convert = converted.toArray(new ReturnsValue[converted.size()]);
        return new ArrayBoxer(convert, elementType, line);
    }
}
