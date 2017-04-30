package com.duy.pascal.backend.pascaltypes;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.RValue;

import java.util.Iterator;

public interface ArgumentType {

    RValue convertArgType(Iterator<RValue> args,
                          ExpressionContext f) throws ParsingException;

    RValue perfectFit(Iterator<RValue> types,
                      ExpressionContext e) throws ParsingException;

    @SuppressWarnings("rawtypes")
    Class getRuntimeClass();

}
