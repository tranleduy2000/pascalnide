package com.duy.pascal.backend.types.converter;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TypeConverter {
    private static final HashMap<Class, Integer> precedence = new HashMap<>();

    static {
        //region integer
        precedence.put(Character.class, 1);
        precedence.put(char.class, 1);

        precedence.put(Byte.class, 1);
        precedence.put(byte.class, 1);

        precedence.put(Short.class, 1);
        precedence.put(short.class, 1);

        precedence.put(Integer.class, 1);
        precedence.put(int.class, 1);

        precedence.put(Long.class, 1);
        precedence.put(long.class, 1);
        //end region

        //region real
        precedence.put(Float.class, 2);
        precedence.put(float.class, 2);

        precedence.put(Double.class, 2);
        precedence.put(double.class, 2);
        //end region
    }

    public static RuntimeValue autoConvert(DeclaredType outType,
                                           RuntimeValue target, DeclaredType inType) {
        if (inType == outType) {
            return target;
        }
        Integer inPrecedence = precedence.get(inType.getTransferClass());
        Integer outPrecedence = precedence.get(outType.getTransferClass());
        if (inPrecedence != null && outPrecedence != null) {
            if (inPrecedence <= outPrecedence) {
                return forceConvert(outType, target, inType);
            }
        }
        if (outType.equals(BasicType.StringBuilder) && inType.equals(BasicType.Character)) {
            return forceConvert(outType, target, inType);
        }
        return null;
    }

    public static RuntimeValue autoConvertRequired(BasicType outType, RuntimeValue target,
                                                   BasicType inType, ExpressionContext c)
            throws UnConvertibleTypeException {
        RuntimeValue result = autoConvert(outType, target, inType);
        if (result == null) {
            throw new UnConvertibleTypeException(target, outType, inType, c);
        }
        return result;
    }

    public static RuntimeValue forceConvertRequired(DeclaredType outType, RuntimeValue value,
                                                    DeclaredType inType, ExpressionContext c)
            throws UnConvertibleTypeException {
        RuntimeValue result = forceConvert(outType, value, inType);
        if (result == null) {
            throw new UnConvertibleTypeException(value, outType, inType, c);
        }
        return result;
    }

    public static RuntimeValue forceConvert(DeclaredType outType,
                                            RuntimeValue target, DeclaredType inType) {
        if (outType.equals(inType)) {
            return target;
        }
        if (outType.equals(BasicType.StringBuilder)) {
            return new AnyToStringType(target);
        }
        if (inType.equals(BasicType.Character)) {
            target = new CharToIntType(target);
            if (outType.equals(BasicType.Integer)) {
                return target;
            } else if (outType.equals(BasicType.Long)) {
                return new NumberToLongType(target);
            } else if (outType == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (inType.equals(BasicType.Integer)) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Long)) {
                return new NumberToLongType(target);
            } else if (outType == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (inType.equals(BasicType.Long)) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Integer)) {
                return new NumberToIntType(target);
            } else if (outType == BasicType.Double) {
                return new NumberToRealType(target);
            }
        }
        if (inType == BasicType.Double) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Integer)) {
                return new NumberToIntType(target);
            } else if (outType.equals(BasicType.Long)) {
                return new NumberToLongType(target);
            }
        }
        return null;
    }

    public static boolean autoConvert(Object[] targetObjects, Object[] convertedObject, Type[] types) {
        if (!(types.length == targetObjects.length)) {
            return false;
        }
        try {
            for (int i = 0; i < types.length; i++) {
                Class<?> classType = (Class<?>) types[i];
                if (classType == String.class) {
                    if (targetObjects[i] instanceof StringBuilder) {
                        convertedObject[i] = targetObjects[i].toString();
                        continue;
                    }
                }
                convertedObject[i] = classType.cast(targetObjects[i]);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
