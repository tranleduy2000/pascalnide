package com.duy.pascal.interperter.declaration.lang.types;


import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.parse_exception.ParsingException;

import java.util.Iterator;

public interface ArgumentType {

    RuntimeValue convertArgType(Iterator<RuntimeValue> args,
                                ExpressionContext f) throws ParsingException;

    RuntimeValue perfectFit(Iterator<RuntimeValue> types,
                            ExpressionContext e) throws ParsingException;

    Class<?> getRuntimeClass();

}
