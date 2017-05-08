package com.js.interpreter.ast;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

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
    public ReturnValue[] formatArgs(List<ReturnValue> values,
                                    ExpressionContext expressionContext) throws ParsingException {
        ArgumentType[] accepted_types = argumentTypes();
        ReturnValue[] result = new ReturnValue[accepted_types.length];
        Iterator<ReturnValue> iterator = values.iterator();
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

    public ReturnValue[] perfectMatch(List<ReturnValue> arguments,
                                      ExpressionContext context) throws ParsingException {
        ArgumentType[] acceptedTypes = argumentTypes();

        //check array
        boolean isArray = false;
        if (acceptedTypes.length > 0) {
            if (acceptedTypes[0] instanceof VarargsType)
                isArray = true;
//            System.out.println("perfectMatch: " + name() + (acceptedTypes[0] instanceof VarargsType));
        }

        if (!isArray && (acceptedTypes.length != arguments.size())) {
//            System.out.println("return null");
            return null;
        }

        Iterator<ReturnValue> iterator = arguments.iterator();
        ReturnValue[] result = new ReturnValue[acceptedTypes.length];
        for (int i = 0; i < acceptedTypes.length; i++) {
            result[i] = acceptedTypes[i].perfectFit(iterator, context);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public abstract ReturnValue generatePerfectFitCall(LineInfo line,
                                                       List<ReturnValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract ReturnValue generateCall(LineInfo line,
                                             List<ReturnValue> values, ExpressionContext f)
            throws ParsingException;

}
