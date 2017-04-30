package com.duy.pascal.backend.pascaltypes.typeconversion;

import com.duy.pascal.backend.exceptions.UnConvertibleTypeException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.js.interpreter.ast.returnsvalue.RValue;

import java.util.HashMap;

public class TypeConverter {
    private static HashMap<Class, Integer> precedence = new HashMap<>();

    static {
        precedence.put(Character.class, 1);
        precedence.put(char.class, 1);

        precedence.put(Integer.class, 1);
        precedence.put(int.class, 1);

        precedence.put(Long.class, 1);
        precedence.put(long.class, 1);

        precedence.put(Double.class, 2);
        precedence.put(double.class, 2);
    }

    public static RValue autoConvert(BasicType outtype,
                                     RValue target, BasicType intype) {
        if (intype == outtype) {
            return target;
        }
        Integer inprecedence = precedence.get(intype.getTransferClass());
        Integer outprecedence = precedence.get(outtype.getTransferClass());
        if (inprecedence != null && outprecedence != null) {
            if (inprecedence <= outprecedence) {
                return forceConvert(outtype, target, intype);
            }
        }
        if (outtype == BasicType.StringBuilder && intype == BasicType.Character) {
            return forceConvert(outtype, target, intype);
        }
        return null;
    }

    public static RValue autoConvertRequired(BasicType outtype,
                                             RValue target, BasicType intype)
            throws UnConvertibleTypeException {
        RValue result = autoConvert(outtype, target, intype);
        if (result == null) {
            throw new UnConvertibleTypeException(target, outtype, intype, true);
        }
        return result;
    }

    public static RValue forceConvertRequired(BasicType outtype,
                                              RValue target, BasicType intype)
            throws UnConvertibleTypeException {
        RValue result = forceConvert(outtype, target, intype);
        if (result == null) {
            throw new UnConvertibleTypeException(target, outtype, intype, false);
        }
        return result;
    }

    public static RValue forceConvert(BasicType outtype,
                                      RValue target, BasicType intype) {
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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
