package com.duy.interpreter.pascaltypes.typeconversion;

import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.exceptions.UnconvertibleTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.BasicType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.HashMap;

public class TypeConverter {
    static HashMap<Class, Integer> precedence = new HashMap<Class, Integer>();

    static {
        precedence.put(Character.class, 0);
        precedence.put(Integer.class, 1);
        precedence.put(Long.class, 2);
        precedence.put(Double.class, 3);
    }

    public static ReturnsValue autoConvert(BasicType outtype,
                                           ReturnsValue target, BasicType intype) {
        if (intype == outtype) {
            return target;
        }
        Integer inprecedence = precedence.get(intype.getTransferClass());
        Integer outprecedence = precedence.get(outtype.getTransferClass());
        if (inprecedence != null && outprecedence != null) {
            if (inprecedence < outprecedence) {
                return forceConvert(outtype, target, intype);
            }
        }
        if (outtype == BasicType.StringBuilder && intype == BasicType.Character) {
            return forceConvert(outtype, target, intype);
        }
        return null;
    }

    public static ReturnsValue autoConvertRequired(BasicType outtype,
                                                   ReturnsValue target, BasicType intype)
            throws UnconvertibleTypeException {
        ReturnsValue result = autoConvert(outtype, target, intype);
        if (result == null) {
            throw new UnconvertibleTypeException(target, outtype, intype, true);
        }
        return result;
    }

    public static ReturnsValue forceConvertRequired(BasicType outtype,
                                                    ReturnsValue target, BasicType intype)
            throws UnconvertibleTypeException {
        ReturnsValue result = forceConvert(outtype, target, intype);
        if (result == null) {
            throw new UnconvertibleTypeException(target, outtype, intype, false);
        }
        return result;
    }

    public static ReturnsValue forceConvert(BasicType outtype,
                                            ReturnsValue target, BasicType intype) {
        if (outtype == intype) {
            return target;
        }
        if (outtype.equals(BasicType.StringBuilder)) {
            return new AnyToString(target);
        }
        if (intype == BasicType.Character) {
            target = new CharToInt(target);
            if (outtype == BasicType.Integer) {
                return target;
            } else if (outtype == BasicType.Long) {
                return new NumberToLong(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToReal(target);
            }
        }
        if (intype == BasicType.Integer) {
            if (outtype == BasicType.Character) {
                return new NumberToChar(target);
            } else if (outtype == BasicType.Long) {
                return new NumberToLong(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToReal(target);
            }
        }
        if (intype == BasicType.Long) {
            if (outtype == BasicType.Character) {
                return new NumberToChar(target);
            } else if (outtype == BasicType.Integer) {
                return new NumberToInt(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToReal(target);
            }
        }
        if (intype == BasicType.Double) {
            if (outtype == BasicType.Character) {
                return new NumberToChar(target);
            } else if (outtype == BasicType.Integer) {
                return new NumberToInt(target);
            } else if (outtype == BasicType.Long) {
                return new NumberToLong(target);
            }
        }
        return null;
    }

    static class NumberToReal implements ReturnsValue {
        ReturnsValue other;

        public NumberToReal(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Number i = (Number) other.getValue(f, main);
            return i.doubleValue();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.Double, false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return ((Number) o).doubleValue();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NumberToReal(other.compileTimeExpressionFold(context));
        }
    }

    static class NumberToLong implements ReturnsValue {
        ReturnsValue other;

        public NumberToLong(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Number i = (Number) other.getValue(f, main);
            return i.longValue();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.Long, false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return ((Number) o).longValue();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NumberToLong(other.compileTimeExpressionFold(context));
        }
    }

    static class NumberToChar implements ReturnsValue {
        ReturnsValue other;

        public NumberToChar(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Number i = (Number) other.getValue(f, main);
            return (char) i.longValue();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.Character, false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return (char) ((Number) o).longValue();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NumberToChar(other.compileTimeExpressionFold(context));
        }
    }

    static class NumberToInt implements ReturnsValue {
        ReturnsValue other;

        public NumberToInt(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Number i = (Number) other.getValue(f, main);
            return i.intValue();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.Integer, false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return ((Number) o).intValue();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new NumberToInt(other.compileTimeExpressionFold(context));
        }
    }

    static class CharToInt implements ReturnsValue {
        ReturnsValue other;

        public CharToInt(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            Character i = (Character) other.getValue(f, main);
            return (int) i.charValue();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.Integer, false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return (int) ((Character) o).charValue();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new CharToInt(other.compileTimeExpressionFold(context));
        }
    }

    public static class AnyToString implements ReturnsValue {
        ReturnsValue other;

        public AnyToString(ReturnsValue other) {
            this.other = other;
        }

        @Override
        public Object getValue(VariableContext f, RuntimeExecutable<?> main)
                throws RuntimePascalException {
            return other.getValue(f, main).toString();
        }

        @Override
        public RuntimeType get_type(ExpressionContext f)
                throws ParsingException {
            return new RuntimeType(BasicType.anew(String.class), false);
        }

        @Override
        public LineInfo getLineNumber() {
            return other.getLineNumber();
        }

        @Override
        public Object compileTimeValue(CompileTimeContext context)
                throws ParsingException {
            Object o = other.compileTimeValue(context);
            if (o != null) {
                return o.toString();
            } else {
                return null;
            }
        }

        @Override
        public SetValueExecutable createSetValueInstruction(ReturnsValue r)
                throws UnassignableTypeException {
            throw new UnassignableTypeException(this);
        }

        @Override
        public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
                throws ParsingException {
            return new AnyToString(other.compileTimeExpressionFold(context));
        }
    }
}
