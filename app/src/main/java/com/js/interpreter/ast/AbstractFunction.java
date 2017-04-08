package com.js.interpreter.ast;

import android.util.Log;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.VarargsType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractFunction implements NamedEntity {

    public static final String TAG = AbstractFunction.class.getSimpleName();

    @Override
    public abstract String name();

    public abstract ArgumentType[] getArgumentTypes();

    public abstract DeclaredType returnType();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(name());
        result.append('(');
        for (ArgumentType c : getArgumentTypes()) {
            result.append(c);
            result.append(',');
        }
        result.append(')');
        return result.toString();
    }

    /**
     * @param values
     * @return converted arguments, or null, if they do not fit.
     * @throws ParsingException
     */
    public ReturnsValue[] format_args(List<ReturnsValue> values,
                                      ExpressionContext f) throws ParsingException {
        ArgumentType[] accepted_types = getArgumentTypes();
        ReturnsValue[] result = new ReturnsValue[accepted_types.length];
        Iterator<ReturnsValue> iterator = values.iterator();
        for (int i = 0; i < accepted_types.length; i++) {
            result[i] = accepted_types[i].convertArgType(iterator, f);
            if (result[i] == null) {/*
                                     * This indicates that it cannot fit.
									 */
                return null;
            }
        }
        if (iterator.hasNext()) {
            return null;
        }
        return result;
    }

    public ReturnsValue[] perfectMatch(List<ReturnsValue> arguments,
                                       ExpressionContext context) throws ParsingException {
        ArgumentType[] acceptedTypes = getArgumentTypes();

        //print to console
        StringBuilder stringBuilder = new StringBuilder();
        for (ArgumentType argumentType : acceptedTypes) {
            stringBuilder.append(argumentType.getRuntimeClass().getSimpleName()).append(" ");
        }
        Log.d(TAG, "perfectMatch: name: " + name() + "  " + stringBuilder.toString());
        if (arguments.size() > 0) {
            RuntimeType runtimeType = arguments.get(0).getType(context);
            runtimeType.getRuntimeClass().getSimpleName();
        }
        //end print

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

        Iterator<ReturnsValue> iterator = arguments.iterator();
        ReturnsValue[] result = new ReturnsValue[acceptedTypes.length];
        for (int i = 0; i < acceptedTypes.length; i++) {
            result[i] = acceptedTypes[i].perfectFit(iterator, context);
            if (result[i] == null) {
                return null;
            }
        }
        return result;
    }

    public abstract ReturnsValue generatePerfectFitCall(LineInfo line,
                                                        List<ReturnsValue> values, ExpressionContext f)
            throws ParsingException;

    public abstract ReturnsValue generateCall(LineInfo line,
                                              List<ReturnsValue> values, ExpressionContext f)
            throws ParsingException;

}
