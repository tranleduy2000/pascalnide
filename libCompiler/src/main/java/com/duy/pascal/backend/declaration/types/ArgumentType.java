package com.duy.pascal.backend.declaration.types;


import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.parse_exception.ParsingException;

import java.util.Iterator;

public interface ArgumentType {

    RuntimeValue convertArgType(Iterator<RuntimeValue> args,
                                ExpressionContext f) throws ParsingException;

    RuntimeValue perfectFit(Iterator<RuntimeValue> types,
                            ExpressionContext e) throws ParsingException;

    @SuppressWarnings("rawtypes")
    Class getRuntimeClass();

}
