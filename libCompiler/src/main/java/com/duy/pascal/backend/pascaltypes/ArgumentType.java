package com.duy.pascal.backend.pascaltypes;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

import java.util.Iterator;

public interface ArgumentType {

    ReturnValue convertArgType(Iterator<ReturnValue> args,
                               ExpressionContext f) throws ParsingException;

    ReturnValue perfectFit(Iterator<ReturnValue> types,
                           ExpressionContext e) throws ParsingException;

    @SuppressWarnings("rawtypes")
    Class getRuntimeClass();

}
