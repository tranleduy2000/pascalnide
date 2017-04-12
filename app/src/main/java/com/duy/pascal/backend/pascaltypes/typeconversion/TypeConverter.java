package com.duy.pascal.backend.pascaltypes.typeconversion;

import com.duy.pascal.backend.exceptions.UnconvertibleTypeException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

import java.util.HashMap;

public class TypeConverter {
    private static HashMap<Class, Integer> precedence = new HashMap<Class, Integer>();

    static {
        precedence.put(Character.class, 0);
        precedence.put(Integer.class, 1);
        precedence.put(Long.class, 2);
        precedence.put(Double.class, 3);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
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
            return new AnyToStringType(target);
        }
        if (intype == BasicType.Character) {
            target = new CharToIntType(target);
            if (outtype == BasicType.Integer) {
                return target;
            } else if (outtype == BasicType.Long) {
                return new NumberToLongType(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (intype == BasicType.Integer) {
            if (outtype == BasicType.Character) {
                return new NumberToCharType(target);
            } else if (outtype == BasicType.Long) {
                return new NumberToLongType(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (intype == BasicType.Long) {
            if (outtype == BasicType.Character) {
                return new NumberToCharType(target);
            } else if (outtype == BasicType.Integer) {
                return new NumberToIntType(target);
            } else if (outtype == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (intype == BasicType.Double) {
            if (outtype == BasicType.Character) {
                return new NumberToCharType(target);
            } else if (outtype == BasicType.Integer) {
                return new NumberToIntType(target);
            } else if (outtype == BasicType.Long) {
                return new NumberToLongType(target);
            }
        }
        return null;
    }

}
