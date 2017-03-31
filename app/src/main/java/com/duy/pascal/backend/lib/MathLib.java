package com.duy.pascal.backend.lib;

import com.duy.pascal.backend.exceptions.WrongArgsException;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.Map;
import java.util.Random;

public class MathLib implements PascalLibrary {

    public static int ceil(double d) {
        return (int) Math.ceil(d);
    }

    public static int trunc(double d) {
        return floor(d);
    }

    public static double frac(double d) {
        return d - floor(d);
    }

    public static int floor(double d) {
        return (int) Math.floor(d);
    }

    public static double abs(double d) {
        return Math.abs(d);
    }

    public static int round(double d) {
        return (int) Math.round(d);
    }

    public static void inc(VariableBoxer boxer) throws RuntimePascalException, WrongArgsException {
        if (boxer.get() instanceof Long) {
            boxer.set(((Long) boxer.get()) + 1);
        } else if (boxer.get() instanceof Integer) {
            boxer.set(((Integer) boxer.get()) + 1);
        } else {
            //throw exception
            throw new WrongArgsException("wrong arg inc()");
//            boxer.set((long) boxer.get() - 1);
        }
    }

    public static void dec(VariableBoxer boxer) throws RuntimePascalException, WrongArgsException {
//        if (boxer.get() instanceof Long) {
//            boxer.set((Long) boxer.get() - 1);
//        } else if (boxer.get() instanceof Integer) {
//            boxer.set((Integer) boxer.get() - 1);
//        } else {
//            throw new RuntimeException("Wrong type in function dec");
//        }
        if (boxer.get() instanceof Long) {
            boxer.set(((Long) boxer.get()) - 1);
        } else if (boxer.get() instanceof Integer) {
            boxer.set(((Integer) boxer.get()) - 1);
        } else {
            //throw exception
            throw new WrongArgsException("wrong arg dec()");
        }
    }


//    public static int getsystemtime() {
//        return (int) System.currentTimeMillis();
//    }

//    public static double pow(double base, double exponent) {
//        return Math.pow(base, exponent);
//    }

    public static double sin(double d) {
        return Math.sin(d);
    }

    public static double cos(double d) {
        return Math.cos(d);
    }

    public static double sqr(double d) {
        return d * d;
    }

    public static double sqrt(double d) {
        return Math.sqrt(d);
    }

    public static int pred(int d) {
        return d - 1;
    }

    public static int succ(int d) {
        return d + 1;
    }

    public static double ln(double d) {
        return Math.log(d);
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    public static int min(int a, int b) {
        return a < b ? a : b;
    }

//    public static double maxE(double a, double b) {
//        return a > b ? a : b;
//    }
//
//    public static double minE(double a, double b) {
//        return a < b ? a : b;
//    }

//    public static double intpow(double base, int exp) {
//        double result = base;
//        while (exp != 0) {
//            if ((exp & 1) == 1) {
//                exp--;
//                result *= base;
//            } else {
//                exp = exp >> 2;
//                result *= result;
//            }
//        }
//        return result;
//    }


    public static double arctan(double a) {
        return Math.atan(a);
    }

    public static double exp(double a) {
        return Math.exp(a);
    }


    public static int Int(double x) {
        return (int) x;
    }

    public static boolean odd(long x) {
        return x % 2 == 0;
    }


    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
