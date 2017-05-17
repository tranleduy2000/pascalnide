package com.duy.pascal.backend.pascaltypes;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.runtime_value.RuntimeValue;

import java.util.Iterator;

public interface ArgumentType {

    RuntimeValue convertArgType(Iterator<RuntimeValue> args,
                                ExpressionContext f) throws ParsingException;

    RuntimeValue perfectFit(Iterator<RuntimeValue> types,
                            ExpressionContext e) throws ParsingException;

    @SuppressWarnings("rawtypes")
    Class getRuntimeClass();

}
