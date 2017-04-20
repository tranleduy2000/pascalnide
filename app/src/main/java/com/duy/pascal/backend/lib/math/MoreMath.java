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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.duy.pascal.backend.lib.math;

class MoreMath {
    private static final double LOG2E = 1.4426950408889634D;
    static final double[] GAMMA = new double[]{57.15623566586292D, -59.59796035547549D, 14.136097974741746D, -0.4919138160976202D, 3.399464998481189E-5D, 4.652362892704858E-5D, -9.837447530487956E-5D, 1.580887032249125E-4D, -2.1026444172410488E-4D, 2.1743961811521265E-4D, -1.643181065367639E-4D, 8.441822398385275E-5D, -2.6190838401581408E-5D, 3.6899182659531625E-6D};
    static final double[] FACT = new double[]{1.0D, 40320.0D, 2.0922789888E13D, 6.204484017332394E23D, 2.631308369336935E35D, 8.159152832478977E47D, 1.2413915592536073E61D, 7.109985878048635E74D, 1.2688693218588417E89D, 6.1234458376886085E103D, 7.156945704626381E118D, 1.8548264225739844E134D, 9.916779348709496E149D, 1.0299016745145628E166D, 1.974506857221074E182D, 6.689502913449127E198D, 3.856204823625804E215D, 3.659042881952549E232D, 5.5502938327393044E249D, 1.3113358856834524E267D, 4.7147236359920616E284D, 2.5260757449731984E302D};

    MoreMath() {
    }

    public static final double asinh(double x) {
        return x < 0.0D?-asinh(-x):Math.log(x + x + 1.0D / (Math.sqrt(x * x + 1.0D) + x));
    }

    public static final double acosh(double x) {
        return Math.log(x + x - 1.0D / (Math.sqrt(x * x - 1.0D) + x));
    }

    public static final double atanh(double x) {
        return x < 0.0D?-atanh(-x):0.5D * Math.log(1.0D + (x + x) / (1.0D - x));
    }

    public static final double trunc(double x) {
        return x >= 0.0D?Math.floor(x):Math.ceil(x);
    }

    public static final double gcd(double x, double y) {
        if(!Double.isNaN(x) && !Double.isNaN(y) && !Double.isInfinite(x) && !Double.isInfinite(y)) {
            x = Math.abs(x);

            double save;
            for(y = Math.abs(y); x < y * 1.0E15D; x = save) {
                save = y;
                y = x % y;
            }

            return x;
        } else {
            return 0.0D / 0.0;
        }
    }

    public static final double lgamma(double x) {
        double tmp = x + 5.2421875D;
        double sum = 0.9999999999999971D;

        for(int i = 0; i < GAMMA.length; ++i) {
            sum += GAMMA[i] / ++x;
        }

        return 0.9189385332046728D + Math.log(sum) + (tmp - 4.7421875D) * Math.log(tmp) - tmp;
    }

    public static final double factorial(double x) {
        if(x < 0.0D) {
            return 0.0D / 0.0;
        } else {
            if(x <= 170.0D && Math.floor(x) == x) {
                int n = (int)x;
                double extra = x;
                switch(n & 7) {
                case 0:
                    return FACT[n >> 3];
                case 7:
                    extra = x * --x;
                case 6:
                    extra *= --x;
                case 5:
                    extra *= --x;
                case 4:
                    extra *= --x;
                case 3:
                    extra *= --x;
                case 2:
                    extra *= --x;
                case 1:
                    return FACT[n >> 3] * extra;
                }
            }

            return Math.exp(lgamma(x));
        }
    }

    public static final double combinations(double n, double k) {
        if(n >= 0.0D && k >= 0.0D) {
            if(n < k) {
                return 0.0D;
            } else if(Math.floor(n) == n && Math.floor(k) == k) {
                k = Math.min(k, n - k);
                if(n <= 170.0D && 12.0D < k && k <= 170.0D) {
                    return factorial(n) / factorial(k) / factorial(n - k);
                } else {
                    double r = 1.0D;
                    double diff = n - k;

                    for(double i = k; i > 0.5D && r < 1.0D / 0.0; --i) {
                        r *= (diff + i) / i;
                    }

                    return r;
                }
            } else {
                return Math.exp(lgamma(n) - lgamma(k) - lgamma(n - k));
            }
        } else {
            return 0.0D / 0.0;
        }
    }

    public static final double permutations(double n, double k) {
        if(n >= 0.0D && k >= 0.0D) {
            if(n < k) {
                return 0.0D;
            } else if(Math.floor(n) == n && Math.floor(k) == k) {
                if(n <= 170.0D && 10.0D < k && k <= 170.0D) {
                    return factorial(n) / factorial(n - k);
                } else {
                    double r = 1.0D;
                    double limit = n - k + 0.5D;

                    for(double i = n; i > limit && r < 1.0D / 0.0; --i) {
                        r *= i;
                    }

                    return r;
                }
            } else {
                return Math.exp(lgamma(n) - lgamma(n - k));
            }
        } else {
            return 0.0D / 0.0;
        }
    }

    public static final double log2(double x) {
        return Math.log(x) * 1.4426950408889634D;
    }

    private static final boolean isPiMultiple(double x) {
        double d = x / 3.141592653589793D;
        return d == Math.floor(d);
    }

    public static final double sin(double x) {
        return isPiMultiple(x)?0.0D:Math.sin(x);
    }

    public static final double cos(double x) {
        return isPiMultiple(x - 1.5707963267948966D)?0.0D:Math.cos(x);
    }

    public static final double tan(double x) {
        return isPiMultiple(x)?0.0D:Math.tan(x);
    }

    public static final int intLog10(double x) {
        return (int)Math.floor(Math.log10(x));
    }

    public static final double intExp10(int exp) {
        return Double.parseDouble("1E" + exp);
    }
}
