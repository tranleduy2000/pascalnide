package com.duy.pascal.backend.lib;

import java.util.Map;

public class MathLib implements PascalLibrary {

//    public int max(int a, int b) {
//        return a > b ? a : b;
//    }

//    public  int getsystemtime() {
//        return (int) System.currentTimeMillis();
//    }

//    public  double pow(double base, double exponent) {
//        return Math.pow(base, exponent);
//    }

//    public int min(int a, int b) {
//        return a < b ? a : b;
//    }

    public double arccos(double x) {
        return Math.acos(x);
    }


//    public  double maxE(double a, double b) {
//        return a > b ? a : b;
//    }
//
//    public  double minE(double a, double b) {
//        return a < b ? a : b;
//    }

//    public  double intpow(double base, int exp) {
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

    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
