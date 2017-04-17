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
    private Random random = new Random();

    @SuppressWarnings("unused")
    public double arcsin(double d) {
        return Math.asin(d);
    }

    @SuppressWarnings("unused")
    public double arcsinh(double d) {
        return MoreMath.asinh(d);
    }

    @SuppressWarnings("unused")
    public double arccos(double x) {
        return Math.acos(x);
    }

    @SuppressWarnings("unused")
    public double arccosh(double d) {
        return MoreMath.acosh(d);
    }

    @SuppressWarnings("unused")
    public double arctan(double d) {
        return MoreMath.atanh(d);
    }

    @SuppressWarnings("unused")
    public double arctan2(double d) {
        return MoreMath.atanh(d);
    }

    @SuppressWarnings("unused")
    public double arctanh(double d) {
        return MoreMath.atanh(d);
    }

    @SuppressWarnings("unused")
    public int celi(double d) {
        return (int) Math.ceil(d);
    }

    @SuppressWarnings("unused")
    public long celi64(double d) {
        return (long) Math.ceil(d);
    }

    @SuppressWarnings("unused")
    public int CompareValue(int a, int b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }

    @SuppressWarnings("unused")
    public int CompareValue(long a, long b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }

    @SuppressWarnings("unused")
    public int CompareValue(double a, double b, double delta) {
        if (Math.abs(a - b) < delta) return 0;
        else if (a > b) return 1;
        else return -1;
    }

    @SuppressWarnings("unused")
    public double cosh(double d) {
        return Math.cosh(d);
    }

    //Convert cycle angle to radians angle
    @SuppressWarnings("unused")
    public double cycletorad(double cycle) {
        double degree = cycle * 360;
        return Math.toRadians(degree);
    }

    //Convert degree angle to grads angle
    @SuppressWarnings("unused")
    public double degtograd(double deg) {
        return 1.1111111111111 * deg;
    }

    @SuppressWarnings("unused")
    public double degtorad(double deg) {
        return Math.toRadians(deg);
    }

    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public int EnsureRange(int value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    @SuppressWarnings("unused")
    public long EnsureRange(long value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    @SuppressWarnings("unused")
    public double EnsureRange(double value, double max, double min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    @SuppressWarnings("unused")
    public int floor(double d) {
        return (int) Math.floor(d);
    }

    @SuppressWarnings("unused")
    public long floor64(double d) {
        return (int) Math.floor(d);
    }

    @SuppressWarnings("unused")
    public double radtodeg(double d) {
        return Math.toDegrees(d);
    }

    @SuppressWarnings("unused")
    public double RandomFrom(double... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    @SuppressWarnings("unused")
    public int RandomFrom(int... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    @SuppressWarnings("unused")
    public long RandomFrom(long... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }

    @SuppressWarnings("unused")
    public int RandomRange(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }

    @SuppressWarnings("unused")
    public long RandomRange(long from, long to) {
        Random r = new Random();
        return from + ((long) (r.nextDouble() * (to - from)));
    }

    @SuppressWarnings("unused")
    public int max(int a, int b) {
        return a > b ? a : b;
    }

    @SuppressWarnings("unused")
    public long max(long a, long b) {
        return Math.max(a, b);
    }

    @SuppressWarnings("unused")
    public double max(double a, double b) {
        return Math.max(a, b);
    }

    @SuppressWarnings("unused")
    public double power(double base, Number exponent) {
        return Math.pow(base, exponent.doubleValue());
    }

    @SuppressWarnings("unused")
    public boolean InRange(int value, int max, int min) {
        return value <= min && value <= max;
    }

    @SuppressWarnings("unused")
    public boolean InRange(double value, double max, double min) {
        return value <= min && value <= max;
    }

    @SuppressWarnings("unused")
    public boolean InRange(long value, long max, long min) {
        return value <= min && value <= max;
    }

    @SuppressWarnings("unused")
    public double tanh(double d) {
        return Math.tanh(d);
    }

    @SuppressWarnings("unused")
    public double sumofsquares(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += power(v, 2);
        }
        return res;
    }

    @SuppressWarnings("unused")
    public double sum(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += v;
        }
        return res;
    }

    @SuppressWarnings("unused")
    public int sumInt(int... arr) {
        int res = 0;
        for (int v : arr) {
            res += v;
        }
        return res;
    }

    @SuppressWarnings("unused")
    public long sumInt(long... arr) {
        int res = 0;
        for (long v : arr) {
            res += v;
        }
        return res;
    }

    @SuppressWarnings("unused")
    public double stddev(double... arr) {
        Statistics statistics = new Statistics(arr);
        return statistics.getStdDev();
    }

    @SuppressWarnings("unused")
    public int sign(double val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    @SuppressWarnings("unused")
    public int sign(long val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    @SuppressWarnings("unused")
    public int sign(int val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }

    @SuppressWarnings("unused")
    public double secant(double x) {
        return 1 / Math.cos(x);
    }

    @SuppressWarnings("unused")
    public double sec(double x) {
        return 1 / Math.cos(x);
    }

    @SuppressWarnings("unused")
    public boolean SameValue(double x, double y, double esp) {

        return Math.abs(x - y) <= esp;
    }

    @SuppressWarnings("unused")
    public boolean SameValue(double x, double y) {
        return Math.abs(x - y) <= 0.0;
    }

    @SuppressWarnings("unused")
    public double mean(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMean();
    }

    //Return total varians of values
    @SuppressWarnings("unused")
    public double totalvariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getVariance();
    }

    @SuppressWarnings("unused")
    public double maxvalue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMax();
    }

    @SuppressWarnings("unused")
    public double popnvariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }

    @SuppressWarnings("unused")
    public double popnstddev(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }

    @SuppressWarnings("unused")
    public double minvalue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMin();
    }

    @SuppressWarnings("unused")
    public int MinIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMin();
    }

    @SuppressWarnings("unused")
    public int MaxIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMax();
    }

    @SuppressWarnings("unused")
    public int min(int a, int b) {
        return a < b ? a : b;
    }

    @SuppressWarnings("unused")
    public long min(long a, long b) {
        return a < b ? a : b;
    }

    @SuppressWarnings("unused")
    public double min(double a, double b) {
        return a < b ? a : b;
    }

    //Return N-based logarithm.
    @SuppressWarnings("unused")
    public double logn(double x, double n) {
        return FastMath.log(x, n);
    }

    @SuppressWarnings("unused")
    public double log2(double x) {
        return FastMath.log(x, 2);
    }

    @SuppressWarnings("unused")
    public double log10(double x) {
        return FastMath.log10(x);
    }

    @SuppressWarnings("unused")
    public boolean IsZero(double x, double esp) {
        return Math.abs(x) <= esp;
    }

    //Check whether value is Not a Number
    @SuppressWarnings("unused")
    public boolean IsNan(double x) {
        return Double.NaN == x;
    }

    //Check whether value is infinite
    @SuppressWarnings("unused")
    public boolean IsInfinite(double x) {
        return Double.POSITIVE_INFINITY == x || Double.NEGATIVE_INFINITY == x;
    }

    @SuppressWarnings("unused")
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
