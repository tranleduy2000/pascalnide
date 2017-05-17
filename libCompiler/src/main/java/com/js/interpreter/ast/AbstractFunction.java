package com.js.interpreter.ast;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.runtime_value.RuntimeValue;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFunction implements NamedEntity {

    public static final String TAG = AbstractFunction.class.getSimpleName();

    @Override
    public abstract String name();

    @Override
    public String description() {
        return null;
    }

    public abstract ArgumentType[] argumentTypes();

    public abstract DeclaredType returnType();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name());
        result.append('(');
        for (ArgumentType c : argumentTypes()) {
            result.append(c);
            result.append(',');
        }
        result.append(')');
        return result.toString();
    }

    /**
     * @return converted arguments, or null, if they do not fit.
     */
    public RuntimeValue[] formatArgs(List<RuntimeValue> values,
                                     ExpressionContext expressionContext) throws ParsingException {
        ArgumentType[] accepted_types = argumentTypes();
        RuntimeValue[] result = new RuntimeValue[accepted_types.length];
        Iterator<RuntimeValue> iterator = values.iterator();
        for (int i = 0; i < accepted_types.length; i++) {
            result[i] = accepted_types[i].convertArgType(iterator, expressionContext);
            if (result[i] == null) {
                //This indicates that it cannot fit.
                return null;
            }
        }
        if (iterator.hasNext()) {
            return null;
        }
        return result;
    }

    public RuntimeValue[] perfectMatch(List<RuntimeValue> arguments,
                                       ExpressionContext context) throws ParsingException {
        ArgumentType[] acceptedTypes = argumentTypes();

        //check array
        boolean isArray = false;
        if (acceptedTypes.length > 0) {
            if (acceptedTypes[0] instanceof VarargsType)
                isArray = true;
        }

        if (!isArray && (acceptedTypes.length != arguments.size())) {
            return null;
        }

        Iterator<RuntimeValue> iterator = arguments.iterator();
        RuntimeValue[] result = new RuntimeValue[acceptedTypes.length];
        for (int i = 0; i < acceptedTypes.length; i++) {
            result[i] = acceptedTypes[i].perfectFit(iterator, context);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public abstract RuntimeValue generatePerfectFitCall(LineInfo line,
                                                        List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract RuntimeValue generateCall(LineInfo line,
                                              List<RuntimeValue> values, ExpressionContext f)
            throws ParsingException;

}
