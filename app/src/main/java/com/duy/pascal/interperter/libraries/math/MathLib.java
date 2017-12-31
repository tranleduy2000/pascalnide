/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.libraries.math;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.WrongArgsException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

import java.util.Map;
import java.util.Random;

@SuppressWarnings("unused")
public class MathLib extends PascalLibrary {
    public static final String NAME = "math";
    private Random random = new Random();

    public MathLib() {

    }

    @PascalMethod(description = "Return inverse cosine")
    public double ArcCos(double x) {
        return Math.acos(x);
    }

    @PascalMethod(description = "Return inverse hyperbolic cosine")
    public double ArcCosh(double d) {
        return MoreMath.acosh(d);
    }

    @PascalMethod(description = "Return inverse hyperbolic cosine")
    public double Arcosh(double d) {
        return MoreMath.acosh(d);
    }

    @PascalMethod(description = "Return inverse sine")
    public double ArcSin(double d) {
        return Math.asin(d);
    }

    @PascalMethod(description = "Return inverse hyperbolic sine")
    public double ArcSinh(double d) {
        return MoreMath.asinh(d);
    }

    @PascalMethod(description = "Return inverse hyperbolic sine")
    public double Arsinh(double d) {
        return MoreMath.asinh(d);
    }

    @PascalMethod(description = "Return Arctangent of (y/x)")
    public double ArcTan2(double y, double x) {
        return Math.atan2(y, x);
    }

    @PascalMethod(description = "Return inverse hyperbolic tangent")
    public double ArcTanh(double d) {
        return MoreMath.atanh(d);
    }

    @PascalMethod(description = "Return inverse hyperbolic tangent")
    public double Artanh(double d) {
        return MoreMath.atanh(d);
    }

    @PascalMethod(description = "Return the lowest integer number greater than or equal to argument")
    public int Ceil(double d) {
        return (int) Math.ceil(d);
    }


    @PascalMethod(description = "Round to the nearest bigger int64 value")
    public long Ceil64(double d) {
        return (long) Math.ceil(d);
    }


    @PascalMethod(description = "")
    public int CompareValue(int a, int b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }


    @PascalMethod(description = "")
    public int CompareValue(long a, long b) {
        if (a < b) return -1;
        else if (a > b) return 1;
        else return 0;
    }


    @PascalMethod(description = "")
    public int CompareValue(double a, double b, double delta) {
        if (Math.abs(a - b) < delta) return 0;
        else if (a > b) return 1;
        else return -1;
    }


    @PascalMethod(description = "")
    public double Cosh(double d) {
        return Math.cosh(d);
    }

    //Convert cycle angle to radians angle

    @PascalMethod(description = "")
    public double CycleToRad(double cycle) {
        double degree = cycle * 360;
        return Math.toRadians(degree);
    }

    //Convert degree angle to grads angle

    @PascalMethod(description = "")
    public double DegToGrad(double deg) {
        return 1.1111111111111 * deg;
    }


    @PascalMethod(description = "")
    public double DegToRad(double deg) {
        return Math.toRadians(deg);
    }


    @PascalMethod(description = "")
    public void DivMod(long dividend, int divisor, PascalReference<Object> result,
                       PascalReference<Object> remainder) throws RuntimePascalException {
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

    @PascalMethod(description = "")
    public int EnsureRange(int value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }


    @PascalMethod(description = "")
    public long EnsureRange(long value, int max, int min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }


    @PascalMethod(description = "")
    public double EnsureRange(double value, double max, double min) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }


    @PascalMethod(description = "Return the largest integer smaller than or equal to argument")
    public int Floor(double d) {
        return (int) Math.floor(d);
    }


    @PascalMethod(description = "Round to the nearest smaller int64 value")
    public long Floor64(double d) {
        return (int) Math.floor(d);
    }


    @PascalMethod(description = "")
    public double RadToDeg(double d) {
        return Math.toDegrees(d);
    }


    @PascalMethod(description = "")
    public double RandomFrom(double... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }


    @PascalMethod(description = "")
    public int RandomFrom(int... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }


    @PascalMethod(description = "")
    public long RandomFrom(long... arr) {
        int size = arr.length;
        int i = random.nextInt(size);
        return arr[i];
    }


    @PascalMethod(description = "")
    public int RandomRange(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }


    @PascalMethod(description = "")
    public long RandomRange(long from, long to) {
        Random r = new Random();
        return from + ((long) (r.nextDouble() * (to - from)));
    }


    @PascalMethod(description = "")
    public int Max(int a, int b) {
        return a > b ? a : b;
    }


    @PascalMethod(description = "")
    public long Max(long a, long b) {
        return Math.max(a, b);
    }


    @PascalMethod(description = "")
    public double Max(double a, double b) {
        return Math.max(a, b);
    }


    @PascalMethod(description = "")
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }


    @PascalMethod(description = "")
    public boolean InRange(int value, int max, int min) {
        return value <= min && value <= max;
    }


    @PascalMethod(description = "")
    public boolean InRange(double value, double max, double min) {
        return value <= min && value <= max;
    }


    @PascalMethod(description = "")
    public boolean InRange(long value, long max, long min) {
        return value <= min && value <= max;
    }


    @PascalMethod(description = "")
    public double Tanh(double d) {
        return Math.tanh(d);
    }


    @PascalMethod(description = "")
    public double SumOfSquares(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += power(v, 2);
        }
        return res;
    }


    @PascalMethod(description = "")
    public double Sum(double... arr) {
        double res = 0;
        for (double v : arr) {
            res += v;
        }
        return res;
    }


    @PascalMethod(description = "")
    public int SumInt(int... arr) {
        int res = 0;
        for (int v : arr) {
            res += v;
        }
        return res;
    }


    @PascalMethod(description = "")
    public long SumInt(long... arr) {
        int res = 0;
        for (long v : arr) {
            res += v;
        }
        return res;
    }


    @PascalMethod(description = "")
    public double Stddev(double... arr) {
        Statistics statistics = new Statistics(arr);
        return statistics.getStdDev();
    }


    @PascalMethod(description = "")
    public int Sign(double val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }


    @PascalMethod(description = "")
    public int Sign(long val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }


    @PascalMethod(description = "")
    public int Sign(int val) {
        if (val > 0) return 1;
        if (val < 0) return -1;
        return 0;
    }


    @PascalMethod(description = "")
    public double Secant(double x) {
        return 1 / Math.cos(x);
    }


    @PascalMethod(description = "")
    public double Sec(double x) {
        return 1 / Math.cos(x);
    }


    @PascalMethod(description = "")
    public boolean SameValue(double x, double y, double esp) {

        return Math.abs(x - y) <= esp;
    }


    @PascalMethod(description = "")
    public boolean SameValue(double x, double y) {
        return Math.abs(x - y) <= 0.0;
    }


    @PascalMethod(description = "")
    public double Mean(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMean();
    }

    //Return total varians of values

    @PascalMethod(description = "")
    public double TotalVariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getVariance();
    }


    @PascalMethod(description = "")
    public double MaxValue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMax();
    }


    @PascalMethod(description = "")
    public double PopNVariance(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }


    @PascalMethod(description = "")
    public double Popstddev(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getPopulationVariance();
    }


    @PascalMethod(description = "")
    public double MinValue(double... arr) {
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(arr);
        return descriptiveStatistics1.getMin();
    }


    @PascalMethod(description = "")
    public int MinIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMin();
    }


    @PascalMethod(description = "")
    public int MaxIntValue(int... arr) {
        double[] copy = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        DescriptiveStatistics descriptiveStatistics1 = new DescriptiveStatistics(copy);
        return (int) descriptiveStatistics1.getMax();
    }


    @PascalMethod(description = "")
    public int Min(int a, int b) {
        return a < b ? a : b;
    }


    @PascalMethod(description = "")
    public long Min(long a, long b) {
        return a < b ? a : b;
    }


    @PascalMethod(description = "")
    public double Min(double a, double b) {
        return a < b ? a : b;
    }

    //Return N-based logarithm.

    @PascalMethod(description = "")
    public double Logn(double x, double n) {
        return FastMath.log(x, n);
    }


    @PascalMethod(description = "")
    public double Log2(double x) {
        return FastMath.log(x, 2);
    }


    @PascalMethod(description = "")
    public double Log10(double x) {
        return FastMath.log10(x);
    }


    @PascalMethod(description = "")
    public boolean IsZero(double x, double esp) {
        return Math.abs(x) <= esp;
    }

    //Check whether value is Not a Number

    @PascalMethod(description = "")
    public boolean IsNan(double x) {
        return Double.NaN == x;
    }

    //Check whether value is infinite

    @PascalMethod(description = "")
    public boolean IsInfinite(double x) {
        return Double.POSITIVE_INFINITY == x || Double.NEGATIVE_INFINITY == x;
    }


    @PascalMethod(description = "")
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {
        parentContext.declareTypedef("float", BasicType.Double);
        parentContext.declareTypedef("pfloat", new PointerType(BasicType.Double));
        parentContext.declareTypedef("pinteger", new PointerType(BasicType.Integer));
    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }
}
