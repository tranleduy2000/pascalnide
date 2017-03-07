package com.ncsa.common.util;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.NoSuchElementException;

import com.ncsa.common.exceptions.TypeConversionException;

/**
 * Various type-checking, primitive conversion and class lookup
 * routines.
 *
 * @author Albert L. Rossi
 */
public class TypeUtils {
    public static final Class[] primitiveAssignable =
            {
                    String.class, Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE,
                    Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE,
                    Boolean.class, Byte.class, Character.class, Double.class,
                    Float.class, Integer.class, Long.class, Short.class, File.class, URI.class
            };

    private static HashMap typeMap = null;
    private static HashMap wrappers = null;
    private static HashMap primTypes = null;
    private static HashMap numTypes = null;
    private static HashMap assignable = null;

    /*
     *  HashMaps holding primitive and numerical type mappings.
     */
    static {
        typeMap = new HashMap();
        String[] keys = new String[]
                {
                        "bool", "boolean", "byte", "char", "character", "double", "float",
                        "int", "integer", "long", "short", "void",
                        "Boolean", "java.lang.Boolean", "Byte", "java.lang.Byte",
                        "Character", "java.lang.Character", "Double", "java.lang.Double",
                        "Float", "java.lang.Float", "Integer", "java.lang.Integer",
                        "Long", "java.lang.Long", "Short", "java.lang.Short",
                        "Void", "java.lang.Void", "string",
                        "String", "java.lang.String", "file", "File", "java.io.File",
                        "uri", "URI", "java.net.URI"
                };
        Class[] clazzes = new Class[]
                {
                        Boolean.TYPE, Boolean.TYPE, Byte.TYPE, Character.TYPE,
                        Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE,
                        Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE,
                        Boolean.class, Boolean.class, Byte.class, Byte.class,
                        Character.class, Character.class, Double.class, Double.class,
                        Float.class, Float.class, Integer.class, Integer.class,
                        Long.class, Long.class, Short.class, Short.class,
                        Void.class, Void.class, String.class, String.class, String.class,
                        File.class, File.class, File.class, URI.class, URI.class, URI.class
                };
        for (int i = 0; i < keys.length; i++) {
            typeMap.put(keys[i], clazzes[i]);
        }

        primTypes = new HashMap();
        wrappers = new HashMap();
        clazzes = new Class[]
                {
                        Boolean.class, Byte.class, Character.class, Double.class,
                        Float.class, Integer.class, Long.class, Short.class,
                        Void.class
                };
        Class[] types = new Class[]
                {
                        Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE,
                        Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE,
                        Void.TYPE
                };
        for (int i = 0; i < clazzes.length; i++) {
            primTypes.put(clazzes[i], types[i]);
            wrappers.put(types[i], clazzes[i]);
        }

        numTypes = new HashMap();
        types = new Class[]
                {
                        Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE,
                        Double.class, Float.class, Integer.class, Long.class, Short.class
                };

        for (int i = 0; i < types.length; i++) {
            numTypes.put(types[i], types[i]);
        }

        assignable = new HashMap();
        for (int i = 0; i < primitiveAssignable.length; i++) {
            assignable.put(primitiveAssignable[i], null);
        }

    } // static

    /**
     * Static utility class; cannot be constructed.
     */
    private TypeUtils() {
    }

    /**
     * @param s fully qualified or primitive name.
     * @return Will return the [Class].Type object for non-wrapped primitives
     * as well as the normal Class objects for all other types.
     * @throws ClassNotFoundException if class not found.
     */
    public static Class getClassForName(String s)
            throws ClassNotFoundException {
        if (typeMap.containsKey(s)) {
            return (Class) typeMap.get(s);
        }
        return Class.forName(s);
    } // getClassForName

    /**
     * @param c the primitive wrapper class.
     * @return The primitive Type object matching the wrapper class.
     */
    public static Class getTypeForClass(Class c) {
        try {
            return (Class) primTypes.get(c);
        } catch (NoSuchElementException nse) {
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    } // getTypeForClass

    /**
     * @param c the primitive Type object.
     * @return The matching primitive wrapper class.
     */
    public static Class getClassForType(Class c) {
        try {
            return (Class) wrappers.get(c);
        } catch (NoSuchElementException nse) {
            return null;
        } catch (NullPointerException npe) {
            return null;
        }
    } // getTypeForClass

    /**
     * Checks to see if the type string designates a primitive, java.lang.String
     * or java.io.File.
     *
     * @param s fully qualified or primitive name.
     * @return true if one of the above types.
     */
    public static boolean isPrimVal(String s) {
        return typeMap.containsKey(s);
    }

    /**
     * Checks to see if the class is a Primitive WRAPPER.
     * NOTE: to check for true primitive, use Class.isPrimitive().
     *
     * @param c class object.
     * @return true if one of the primitive types or its wrappers.
     */
    public static boolean isPrimitiveWrapper(Class c) {
        return primTypes.containsKey(c);
    }

    /**
     * Checks to see if the class or type qualifies as a primitive-assignable
     * class.
     *
     * @param c class object.
     * @return true if primitive-assignable
     */
    public static boolean isPrimitiveAssignable(Class c) {
        return assignable.containsKey(c);
    }

    /**
     * @param s fully qualified or primitive name.
     * @return true if type represents a numerical value.
     */
    public static boolean isNumber(String s) throws ClassNotFoundException {
        return numTypes.containsKey(getClassForName(s));
    }

    /**
     * @param c object's Class.
     * @return true if Class represents a numerical value.
     */
    public static boolean isNumber(Class c) {
        return numTypes.containsKey(c);
    }

    /**
     * See isTypeOf( Class, Class )
     *
     * @param s       type to check.
     * @param toMatch type to check against.
     * @return true if Class of s is a (sub)type of Class of toMatch.
     * @throws ClassNotFoundException if class not found.
     */
    public static boolean isTypeOf(String s, String toMatch)
            throws ClassNotFoundException {
        return isTypeOf(getClassForName(s), getClassForName(toMatch));
    } // isTypeOf

    /**
     * @param c       class to check.
     * @param toMatch type to check against.
     * @return true if c is a (sub)type of Class of toMatch.
     * @throws ClassNotFoundException if class not found.
     */
    public static boolean isTypeOf(Class c, String toMatch)
            throws ClassNotFoundException {
        return isTypeOf(c, getClassForName(toMatch));
    } // isTypeOf

    /**
     * @param s       type to check.
     * @param toMatch class to check against.
     * @return true if Class of s is a (sub)type of toMatch.
     * @throws ClassNotFoundException if class not found.
     */
    public static boolean isTypeOf(String s, Class toMatch)
            throws ClassNotFoundException {
        return isTypeOf(getClassForName(s), toMatch);
    } // isTypeOf

    /**
     * Ascends the hierarchy tree of c, checking for equality with toMatch.
     *
     * @param c       class to check.
     * @param toMatch class to check against.
     * @return true if c is a (sub)type of toMatch.
     */
    public static boolean isTypeOf(Class c, Class toMatch) {
        boolean matches = false;
        while (c != null) {
            matches = c.equals(toMatch)
                    || (toMatch.equals(Boolean.TYPE)
                    && c.equals(Boolean.class))
                    || (toMatch.equals(Byte.TYPE)
                    && c.equals(Byte.class))
                    || (toMatch.equals(Character.TYPE)
                    && c.equals(Character.class))
                    || (toMatch.equals(Double.TYPE)
                    && c.equals(Double.class))
                    || (toMatch.equals(Float.TYPE)
                    && c.equals(Float.class))
                    || (toMatch.equals(Integer.TYPE)
                    && c.equals(Integer.class))
                    || (toMatch.equals(Long.TYPE)
                    && c.equals(Long.class))
                    || (toMatch.equals(Short.TYPE)
                    && c.equals(Short.class));
            if (matches) {
                break;
            }
            c = c.getSuperclass();
        }
        return matches;
    } // isTypeOf

    /**
     * Does type-checking to make sure the two objects are comparable.
     * This method checks the objects against the putative classes provided.
     *
     * @param o1 object to compare.
     * @param o2 object to which o1 is compared.
     * @param c1 class which should match the class of o1.
     * @param c2 class which should match the class of o2.
     * @return true if match, false if not
     */
    public static boolean typesMatch(Object o1,
                                     Object o2,
                                     Class c1,
                                     Class c2) {
        // we'll consider a null to match any type
        if (o1 == null || o2 == null) {
            return true;
        }
        Class c3 = o1.getClass();
        Class c4 = o2.getClass();
        return ((c3.equals(c1) && c4.equals(c1)) ||
                (c3.equals(c2) && c4.equals(c2)) ||
                (c3.equals(c1) && c4.equals(c2)) ||
                (c3.equals(c2) && c4.equals(c1)));
    } // typesMatch

    /**
     * Does type-checking to make sure the two objects are comparable.
     * The classes of the objects are directly compared.
     *
     * @param o1 object to compare.
     * @param o2 object to which o1 is compared.
     * @return true if match, false if not.
     */
    public static boolean typesMatch(Object o1, Object o2) {
        // we'll consider a null to match any type
        if (o1 == null || o2 == null) {
            return true;
        }
        Class c1 = o1.getClass();
        Class c2 = o2.getClass();
        return c1.equals(c2);
    } // typesMatch

    public static boolean arePrimitiveAnalogous(Class c1, Class c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        return c1.equals(c2) ||
                c1.equals(wrappers.get(c2)) ||
                c1.equals(primTypes.get(c2));
    }

    public static Object cast(String castTo, Object value)
            throws TypeConversionException {
        Class clzz = null;
        if (castTo != null) {
            try {
                clzz = TypeUtils.getClassForName(castTo);
                value = TypeUtils.convertPrim(clzz, value);
            } catch (ClassNotFoundException t) {
                throw new TypeConversionException
                        ("could not get class for " + castTo);
            } catch (TypeConversionException t) {
                throw t;
            }
        }
        return value;
    }

    /**
     * If the class of value is a (sub)type of type, returns value.
     * Else if the class of value is a number and type is also, a
     * cast is performed by doing toString on the value and using it
     * as the parameter to the constructor of the new numerical type.
     * This is also done for all primitives in the case that value is a String.
     * Otherwise, the unconverted value is returned.
     *
     * @param type  to convert to.
     * @param value to convert.
     * @return the new type of object.
     * @throws TypeConversionException if the constructors of the new type
     *                                 throw an exception.
     */
    public static Object convertPrim(Class type, Object value)
            throws TypeConversionException {
        if (value == null) {
            return null;
        }

        Class c = value.getClass();
        if (isTypeOf(c, type)) {
            return value;
        }

        String s = null;
        if (isNumber(type) && isNumber(c)) {
            s = value.toString();
        } else if (!c.equals(String.class)) {
            return value;
        } else {
            s = (String) value;
        }

        try {
            if (type.equals(Boolean.class)
                    || type.equals(Boolean.TYPE)) {
                return new Boolean(s);
            } else if (type.equals(Byte.class)
                    || type.equals(Byte.TYPE)) {
                return new Byte(s);
            } else if (type.equals(Character.class)
                    || type.equals(Character.TYPE)) {
                return new Character(s.charAt(0));
            } else if (type.equals(Double.class)
                    || type.equals(Double.TYPE)) {
                return new Double(s);
            } else if (type.equals(Float.class)
                    || type.equals(Float.TYPE)) {
                return new Float(s);
            } else if (type.equals(Integer.class)
                    || type.equals(Integer.TYPE)) {
                try {
                    s = s.substring(0, s.indexOf("."));
                } catch (IndexOutOfBoundsException ioobe) {
                }
                return new Integer(s);
            } else if (type.equals(Long.class)
                    || type.equals(Long.TYPE)) {
                try {
                    s = s.substring(0, s.indexOf("."));
                } catch (IndexOutOfBoundsException ioobe) {
                }
                return new Long(s);
            } else if (type.equals(Short.class)
                    || type.equals(Short.TYPE)) {
                try {
                    s = s.substring(0, s.indexOf("."));
                } catch (IndexOutOfBoundsException ioobe) {
                }
                return new Short(s);
            } else if (type.equals(File.class)) {
                return new File(s);
            } else if (type.equals(URI.class)) {
                return new URI(s);
            } else {
                return s;
            }
        } catch (Throwable t) {
            throw new TypeConversionException("convertPrim", t);
        }
    } // convertPrim

}
