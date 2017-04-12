package com.duy.pascal.backend.lib.math;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.WrongArgsException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

import java.util.Map;
import java.util.Random;

public class MathLib implements PascalLibrary {
    Random random = new Random();

    public double arcsin(double d) {
        return Math.asin(d);
    }

    public double arcsinh(double d) {
        return MoreMath.asinh(d);
    }

    public double arccos(double x) {
        return Math.acos(x);
    }

    public double arccosh(double d) {
        return MoreMath.acosh(d);
    }

    public double arctan(double d) {
        return MoreMath.atanh(d);
    }

    public double arctan2(double d) {
        return MoreMath.atanh(d);
    }

    public double arctanh(double d) {
        return MoreMath.atanh(d);
    }

    public int celi(double d) {
        return (int) Math.ceil(d);
    }

    public long celi64(double d) {
        return (long) Math.ceil(d);
    }

    public int CompareValue(int a, int b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }

    public int CompareValue(long a, long b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }

    public int CompareValue(double a, double b, double delta) {
        if (Math.abs(a - b) < delta) return 0;
        else if (a > b) return 1;
        else return -1;
    }

    public double cosh(double d) {
        return Math.cosh(d);
    }

    //Convert cycle angle to radians angle
    public double cycletorad(double cycle) {
        double degree = cycle * 360;
        return Math.toRadians(degree);
    }

    //Convert degree angle to grads angle
    public double degtograd(double deg) {
        return 1.1111111111111 * deg;
    }

    public double degtorad(double deg) {
        return Math.toRadians(deg);
    }

    public void DivMod(long dividend, int divisor, VariableBoxer<Object> result,
                       VariableBoxer<Object> remainder) throws RuntimePascalException, WrongArgsException {
        if (result.get() instanceof Integer) {
            result.set((int) (dividend / divisor));
            remainder.set((int) (dividend % divisor));
        } else if (result.get() instanceof Long) {
            result.set(dividend / divisor);
            remainder.set(dividend % divisor);
        } else throw new WrongArgsException("DivMod(longint, longint, word, word)\n" +
                "DivMod(longint, word, word, word)\n" +
                "DivMod(Dword, dword, dword, dword)\n" +
                "DivMod(longint, longint, longint, longint)");
    }

    //Change value to it falls in specified range.
    public int EnsureRange(int value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public long EnsureRange(long value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public double EnsureRange(double value, double max, double min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public int floor(double d) {
        return (int) Math.floor(d);
    }

    public long floor64(double d) {
        return (int) Math.floor(d);
    }

    public double radtodeg(double d) {
        return Math.toDegrees(d);
    }

    public double RandomFrom(double... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    public int RandomFrom(int... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    public long RandomFrom(long... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    public int RandomRange(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }

    public long RandomRange(long from, long to) {
        Random r = new Random();
        return from + ((long) (r.nextDouble() * (to - from)));
    }

    public int max(int a, int b) {
        return a > b ? a : b;
    }

    public long max(long a, long b) {
        return Math.max(a, b);
    }

    public double max(double a, double b) {
        return Math.max(a, b);
    }

    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public double power(double base, int exponent) {
        return Math.pow(base, exponent);
    }

    public boolean InRange(int value, int max, int min) {
        return value <= min && value <= max;
    }

    public boolean InRange(double value, double max, double min) {
        return value <= min && value <= max;
    }

    public boolean InRange(long value, long max, long min) {
        return value <= min && value <= max;
    }

    public double tanh(double d) {
        return Math.tanh(d);
    }

    public double sumofsquares(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += power(v, 2);
        }
        return res;
    }

    public double sum(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += v;
        }
        return res;
    }

    public int sumInt(int... arr) {
        int res = 0;
        for (int v : arr) {
            res += v;
        }
        return res;
    }

    public long sumInt(long... arr) {
        int res = 0;
        for (long v : arr) {
            res += v;
        }
        return res;
    }

    public double stddev(double... arr) {
        Statistics statistics = new Statistics(arr);
        return statistics.getStdDev();
    }

    public int sign(double val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    public int sign(long val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    public int sign(int val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    public double secant(double x) {
        return 1 / Math.cos(x);
    }

    public double sec(double x) {
        return 1 / Math.cos(x);
    }

    public boolean SameValue(double x, double y, double esp) {

        return Math.abs(x - y) <= esp;
    }

    public boolean SameValue(double x, double y) {
        return Math.abs(x - y) <= 0.0;
    }

    public double mean(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMean();
    }

    //Return total varians of values
    public double totalvariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getVariance();
    }

    public double maxvalue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMax();
    }

    public double popnvariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }

    public double popnstddev(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }

    public double minvalue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMin();
    }

    public int MinIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMin();
    }

    public int MaxIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMax();
    }

    public int min(int a, int b) {
        return a < b ? a : b;
    }

    public long min(long a, long b) {
        return a < b ? a : b;
    }

    public double min(double a, double b) {
        return a < b ? a : b;
    }

    //Return N-based logarithm.
    public double logn(double x, double n) {
        return FastMath.log(x, n);
    }

    public double log2(double x) {
        return FastMath.log(x, 2);
    }

    public double log10(double x) {
        return FastMath.log10(x);
    }

    public boolean IsZero(double x, double esp) {
        return Math.abs(x) <= esp;
    }

    //Check whether value is Not a Number
    public boolean IsNan(double x) {
        return Double.NaN == x;
    }

    //Check whether value is infinite
    public boolean IsInfinite(double x) {
        return Double.POSITIVE_INFINITY == x || Double.NEGATIVE_INFINITY == x;
    }

    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
