package com.js.interpreter.ast.returnsvalue;

import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface LValue extends RValue {

    public Reference getReference(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException;

}
