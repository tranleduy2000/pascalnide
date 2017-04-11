package com.duy.pascal.backend.lib.templated;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class SetLengthLib implements TemplatePascalPlugin {

    private ArgumentType[] argumentTypes = {new RuntimeType(new ArrayType<>(
                    BasicType.anew(Object.class), new SubrangeType(0, 0)), true),
            new RuntimeType(BasicType.Integer, false)
    };

    @Override
    public String name() {
        return "setlength";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnsValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnsValue array = arguments[0];
        ReturnsValue size = arguments[1];
        @SuppressWarnings("rawtypes")
        DeclaredType elemtype = ((ArrayType) ((PointerType) array.getType(f).declaredType).pointedToType).element_type;
        LineInfo l = line;
        return new SetLengthCall(array, size, elemtype, l);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, ReturnsValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType return_type() {
        return null;
    }

}
