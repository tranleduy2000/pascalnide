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

package com.duy.pascal.interperter.libraries;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime.ObjectBasedPointer;
import com.duy.pascal.interperter.ast.runtime.references.PascalPointer;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.exceptions.parsing.value.OrdinalExpressionExpectedException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.ScriptTerminatedException;
import com.duy.pascal.interperter.exceptions.runtime.WrongArgsException;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.exceptions.InvalidFloatingPointOperation;
import com.duy.pascal.interperter.libraries.exceptions.RangeCheckError;
import com.duy.pascal.ui.R;

import java.util.Random;

/**
 * System lib
 * <p>
 * - key event
 * - key read
 * <p>
 * Created by Duy on 07-Mar-17.
 */
@SuppressWarnings("unused")
public class SystemLibrary extends PascalLibrary {

    private Random random = new Random();


    public SystemLibrary() {

    }

    @Override
    @PascalMethod(description = "stop")

    public void onFinalize() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }


    @PascalMethod(description = "system lib")
    public int Byte(boolean b) {
        return b ? 1 : 0;
    }

    @PascalMethod(description = "Generate random number")
    public long Random(long range) {
        return (long) (random.nextDouble() * range);
    }

    @PascalMethod(description = "Generate random number")
    public double Random() {
        return random.nextDouble();
    }

    @PascalMethod(description = "Initialize random number generator")
    public void Randomize() {
        random = new Random(System.currentTimeMillis());
    }

    @PascalMethod(description = "Increase value of integer variable")
    public void Inc(PascalReference<Object> boxer) throws RuntimePascalException {
        Inc(boxer, 1);
    }

    @PascalMethod(description = "Increase value of integer variable")
    public void Inc(PascalReference<Object> boxer, Object increment) throws RuntimePascalException {
        if (boxer.get() instanceof Long) {
            long count;
            if (increment instanceof Integer) {
                count = Long.parseLong(increment.toString());
            } else if (increment instanceof Long) {
                count = (long) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((long) boxer.get()) + count);
        } else if (boxer.get() instanceof Integer) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((int) boxer.get()) + count);
        } else if (boxer.get() instanceof Character) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }

            Character character = (Character) boxer.get();
            Character newChar = (char) (character + count);
            boxer.set(newChar);
        } else if (boxer.get() instanceof Boolean) {
            if ((Boolean) boxer.get()) {
                throw new RangeCheckError(boxer);
            } else {
                boxer.set(true);
            }
        } else {
            //throw exception
            throw new WrongArgsException("inc");
//            boxer.set((long) boxer.indexOf() - 1);
        }
    }

    @PascalMethod(description = "Decrease value of variable")
    public void Dec(PascalReference<Object> boxer) throws RuntimePascalException {
        Dec(boxer, 1);
    }

    @PascalMethod(description = "Decrease value of variable")
    public void Dec(PascalReference<Object> boxer, Object increment) throws RuntimePascalException {
        if (boxer.get() instanceof Long) {
            long count;
            if (increment instanceof Integer) {
                count = Long.parseLong(increment.toString());
            } else if (increment instanceof Long) {
                count = (long) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Long) boxer.get()) - count);
        } else if (boxer.get() instanceof Integer) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }
            boxer.set(((Integer) boxer.get()) - count);
        } else if (boxer.get() instanceof Character) {
            int count;
            if (increment instanceof Long) {
                count = Integer.parseInt(increment.toString());
            } else if (increment instanceof Integer) {
                count = (int) increment;
            } else {
                throw new OrdinalExpressionExpectedException();
            }

            Character character = (Character) boxer.get();
            Character newChar = (char) (character - count);
            boxer.set(newChar);
        } else if (boxer.get() instanceof Boolean) {
            if (!(Boolean) boxer.get()) {
                throw new RangeCheckError(boxer);
            } else {
                boxer.set(false);
            }
        } else {
            //throw exception
            throw new WrongArgsException("dec");
//            boxer.set((long) boxer.indexOf() - 1);
        }
    }

    @PascalMethod(description = "Truncate a floating point value.")
    public long Trunc(double d) {
        return (long) (d - Frac(d));
    }

    @PascalMethod(description = "Return fractional part of floating point value.")
    public double Frac(double d) {
        return d - ((long) d);
    }

    @PascalMethod(description = "Calculate absolute value")
    public int Abs(int d) {
        return Math.abs(d);
    }

    @PascalMethod(description = "Calculate absolute value")
    public long Abs(long d) {
        return Math.abs(d);
    }

    @PascalMethod(description = "Calculate absolute value")
    public double Abs(double d) {
        return Math.abs(d);
    }

    @PascalMethod(description = "Round floating point value to nearest integer number.")
    public long Round(double d) {
        return Math.round(d);
    }

    @PascalMethod(description = "Calculate sine of angle")
    public double Sin(double d) {
        return Math.sin(d);
    }

    @PascalMethod(description = "Calculate cosine of angle")
    public double Cos(double d) {
        return Math.cos(d);
    }

    @PascalMethod(description = "Calculate the square of a value.")
    public int Sqr(int d) {
        return d * d;
    }

    @PascalMethod(description = "Calculate the square of a value.")
    public long Sqr(long d) {
        return d * d;
    }

    @PascalMethod(description = "Calculate the square of a value.")
    public double Sqr(double d) {
        return d * d;
    }

    @PascalMethod(description = "Calculate the square root of a value")
    public double Sqrt(double value) throws InvalidFloatingPointOperation {
        if (value < 0) {
            throw new RuntimePascalException(R.string.lower_than_zero, value);
        }
        return Math.sqrt(value);
    }

    @PascalMethod(description = "Return previous element for an ordinal type")
    public int Pred(int d) {
        return d - 1;
    }

    @PascalMethod(description = "system lib")
    public int Succ(int d) {
        return d + 1;
    }

    /**
     * logarithm function
     */
    @PascalMethod(description = "system lib")
    public double ln(double value) throws InvalidFloatingPointOperation {
        if (value < 0) {
            throw new RuntimePascalException(R.string.lower_than_zero, value);
        }
        return Math.log(value);
    }

    @PascalMethod(description = "Calculate inverse tangent")
    public double ArcTan(double a) {
        return Math.atan(a);
    }

    @PascalMethod(description = "Exponentiate")
    public double Exp(double a) {
        return Math.exp(a);
    }


    @PascalMethod(description = "Calculate integer part of floating point value.")
    public int Int(double x) {
        return (int) x;
    }

    @PascalMethod(description = "Is a value odd or even ?")
    public boolean Odd(long x) {
        return x % 2 == 0;
    }

    @PascalMethod(description = "Stop program execution")
    public void Halt(long value) throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }

    @PascalMethod(description = "")
    public void Halt() throws ScriptTerminatedException {
        throw new ScriptTerminatedException(null);
    }

    @PascalMethod(description = "Is a value odd or even ?")
    public boolean Odd(int i) {
        return i % 2 == 1;
    }

    @PascalMethod(description = "Convert ascii to character")
    public char Chr(int i) {
        return (char) i;
    }


    @PascalMethod(description = "Convert character to ascii code")
    public int Ord(char c) {
        return (int) c;
    }


    @PascalMethod(description = "Convert a numerical or enumeration value to a string")
    public void Str(int num, PascalReference<StringBuilder> s) {
        s.set(new StringBuilder(String.valueOf(num)));
    }

    @PascalMethod(description = "Convert a numerical or enumeration value to a string")
    public void Str(long num, PascalReference<StringBuilder> s) {
        s.set(new StringBuilder(String.valueOf(num)));
    }

    @PascalMethod(description = "Convert a numerical or enumeration value to a string")
    public void Str(double num, PascalReference<StringBuilder> s) {
        s.set(new StringBuilder(String.valueOf(num)));
    }


    @PascalMethod(description = " Calculate numerical/enumerated value of a string.")
    public void Val(StringBuilder zinput, PascalReference<Object> output, PascalReference<Integer> resultCode) throws RuntimePascalException {
        try {
            String input;
            input = zinput.toString().trim(); //remove white space in start and end postion
            if (output.get() instanceof Long) {
                Long l = Long.parseLong(input);
                output.set(l);
                resultCode.set(1);
            } else if (output.get() instanceof Double) {
                Double d = Double.parseDouble(input);
                output.set(d);
                resultCode.set(1);
            } else if (output.get() instanceof Integer) {
                Integer d = Integer.parseInt(input);
                output.set(d);
                resultCode.set(1);
            } else {
                throw new WrongArgsException("Can not call \"val(string, number, result)\"");
            }
        } catch (NumberFormatException e) {
            resultCode.set(-1);
        }
    }


    @PascalMethod(description = "Convert a char to uppercase.")
    public char UpCase(char s) {
        return Character.toUpperCase(s);
    }

    @PascalMethod(description = "Convert a string to all uppercase.")
    public StringBuilder UpCase(StringBuilder s) {
        return new StringBuilder(s.toString().toUpperCase());
    }

    @PascalMethod(description = "Append one string to another")
    public StringBuilder Concat(StringBuilder... args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StringBuilder arg : args) {
            stringBuilder.append(arg);
        }
        return stringBuilder;
    }

    @PascalMethod(description = "Search for substring in a string")
    public int Pos(StringBuilder substring, StringBuilder s) {
        return s.indexOf(substring.toString()) + 1;
    }

    @PascalMethod(description = "Set length of a string.")
    public void SetLength(PascalReference<StringBuilder> s, int length) {
        String filler = "!@#$%";
        StringBuilder old = s.get();
        if (length <= old.length()) {
            s.set(new StringBuilder(old.subSequence(0, length)));
        } else {
            int extra = length - old.length();
            int count = extra / filler.length();
            StringBuilder result = new StringBuilder(length);
            result.append(old);
            for (int i = 0; i < count; i++) {
                result.append(filler);
            }
            int remaining = extra - count * filler.length();
            result.append(filler.subSequence(0, remaining));
            s.set(result);
        }
    }

    @PascalMethod(description = "Insert one string in another.")
    public void Insert(StringBuilder s, PascalReference<StringBuilder> s1, int pos)
            {
        s1.set(new StringBuilder(s1.get()).insert(pos - 1, s));
    }

    @PascalMethod(description = "Copy part of a string")
    public StringBuilder Copy(StringBuilder s, int from, int count) {
        if (from - 1 + count > s.length()) {
            return new StringBuilder(s.substring(from - 1, s.length()));
        }
        return new StringBuilder(s.substring(from - 1, from - 1 + count));
    }


    @PascalMethod(description = "Delete part of a string")
    public void Delete(PascalReference<StringBuilder> s, int start, int count)
            throws RuntimePascalException {
        s.set(s.get().delete(start - 1, start + count - 1));
    }

    @PascalMethod(description = "Free dynamically allocated memory")
    public void Dispose(PascalPointer<?> pascalPointer) {
        pascalPointer.set(null);
    }

    @PascalMethod(description = "Allocate new memory on the heap")
    public void GetMem(PascalPointer pascalPointer, long size) {
    }

    @PascalMethod(description = "Allocate new memory on the heap")
    public PascalPointer GetMem(long size) {
        return new ObjectBasedPointer<>(new Object());
    }

    public StringBuilder CreateNull() {
        return null;
    }
}
