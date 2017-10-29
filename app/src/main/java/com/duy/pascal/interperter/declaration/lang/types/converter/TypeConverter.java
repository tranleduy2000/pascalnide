package com.duy.pascal.interperter.declaration.lang.types.converter;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;

import java.util.HashMap;

public class TypeConverter {
    private static final HashMap<Class, Integer> PRECEDENCE = new HashMap<>();
    private static final String TAG = "TypeConverter";

    static {
        //region integer
        PRECEDENCE.put(Character.class, 1);
        PRECEDENCE.put(char.class, 1);

        PRECEDENCE.put(Byte.class, 1);
        PRECEDENCE.put(byte.class, 1);

        PRECEDENCE.put(Short.class, 1);
        PRECEDENCE.put(short.class, 1);

        PRECEDENCE.put(Integer.class, 1);
        PRECEDENCE.put(int.class, 1);

        PRECEDENCE.put(Long.class, 1);
        PRECEDENCE.put(long.class, 1);
        //end region

        //region real
        PRECEDENCE.put(Float.class, 2);
        PRECEDENCE.put(float.class, 2);

        PRECEDENCE.put(Double.class, 2);
        PRECEDENCE.put(double.class, 2);
        //end region
    }

    public static RuntimeValue autoConvert(Type outType,
                                           RuntimeValue target, Type inType) {
        if (inType == outType) {
            return target;
        }
        Integer inPrecedence = PRECEDENCE.get(inType.getTransferClass());
        Integer outPrecedence = PRECEDENCE.get(outType.getTransferClass());
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

    /**
     * @param first  - primitive type
     * @param second - primitive type
     * @return true if precede of first lower precede of second
     */
    public static boolean isLowerThanPrecedence(@NonNull Class first, @NonNull Class second) {
        Integer inPrecedence = PRECEDENCE.get(first);
        Integer outPrecedence = PRECEDENCE.get(second);
        if (inPrecedence != null && outPrecedence != null) {
            if (inPrecedence <= outPrecedence) {
                return true;
            }
        }
        return false;
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

    public static RuntimeValue forceConvertRequired(Type outType, RuntimeValue value,
                                                    Type inType, ExpressionContext c)
            throws UnConvertibleTypeException {
        RuntimeValue result = forceConvert(outType, value, inType);
        if (result == null) {
            throw new UnConvertibleTypeException(value, outType, inType, c);
        }
        return result;
    }

    public static RuntimeValue forceConvert(Type outType,
                                            RuntimeValue target, Type inType) {
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
            } else if (outType == BasicType.Short) {
                return new NumberToShortType(target);
            } else if (outType == BasicType.Byte) {
                return new NumberToByteType(target);
            }
        }
        if (inType.equals(BasicType.Integer)) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Long)) {
                return new NumberToLongType(target);
            } else if (outType == BasicType.Double) {
                return new NumberToRealType(target);
            } else if (outType == BasicType.Short) {
                return new NumberToShortType(target);
            } else if (outType == BasicType.Byte) {
                return new NumberToByteType(target);
            }
        }
        if (inType.equals(BasicType.Long)) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Integer)) {
                return new NumberToIntType(target);
            } else if (outType == BasicType.Double) {
                return new NumberToRealType(target);
            } else if (outType == BasicType.Short) {
                return new NumberToShortType(target);
            } else if (outType == BasicType.Byte) {
                return new NumberToByteType(target);
            }
        }
        if (inType == BasicType.Double) {
            if (outType.equals(BasicType.Character)) {
                return new NumberToCharType(target);
            } else if (outType.equals(BasicType.Integer)) {
                return new NumberToIntType(target);
            } else if (outType.equals(BasicType.Long)) {
                return new NumberToLongType(target);
            } else if (outType == BasicType.Short) {
                return new NumberToShortType(target);
            } else if (outType == BasicType.Byte) {
                return new NumberToByteType(target);
            }
        }
        return null;
    }

    public static boolean autoConvert(Object[] targetObjects, Object[] convertedObject, java.lang.reflect.Type[] types) {
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

    /**
     * @param clazz - any class
     * @return true if class is primitive, include object String.class, Long.class,
     * Integer.class, Double.class, ...
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return PRECEDENCE.get(clazz) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
