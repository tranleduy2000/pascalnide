/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.interpreter;

/**
 * Created by Duy on 28-Aug-17.
 */

public class SystemFunctionTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return System.getProperty("user.dir") + "\\app\\src\\main\\assets\\code_sample\\system";
    }

    public void testbyte() {
        run("byte.pas");
    }

    public void testCharToAscii() {
        run("CharToAscii.pas");
    }

    public void testConcatString() {
        run("ConcatString.pas");
    }

    public void testCopy() {
        run("Copy.pas");
    }

    public void testCos() {
        run("Cos.pas");
    }

    public void testDec() {
        run("Dec.pas");
    }

    public void testDelete() {
        run("Delete.pas");
    }

    public void testearse() {
        run("earse.pas");
    }

    public void testEoln() {
        run("Eoln.pas");
    }

    public void testExit() {
        run("Exit.pas");
    }

    public void testFrac() {
        run("Frac.pas");
    }

    public void testFunction() {
        run("Function.pas");
    }

    public void testIncDec() {
        run("IncDec.pas");
    }

    public void testInsert() {
        run("Insert.pas");
    }

    public void testKeypressed() {
        run("Keypressed.pas");
    }

    public void testLength() {
        run("Length.pas");
    }

    public void testodd() {
        run("odd.pas");
    }

    public void testPos() {
        run("Pos.pas");
    }

    public void testRandom() {
        run("Random.pas");
    }

    public void testrandom_2() {
        run("random_2.pas");
    }

//    public void testReadKey() {
//        run("ReadKey.pas");
//    }

    public void testRound() {
        run("Round.pas");
    }

    public void testSetLength() {
        run("SetLength.pas");
    }

    public void testSin() {
        run("Sin.pas");
    }

    public void testSizeOf() {
        run("SizeOf.pas");
    }

    public void testSqr() {
        run("Sqr.pas");
    }

    public void testSqrt() {
        run("Sqrt.pas");
    }

    public void testStr() {
        run("Str.pas");
    }

    public void testStringToInt() {
        run("StringToInt.pas");
    }

    public void testTrunc() {
        run("Trunc.pas");
    }

    public void testUpCase() {
        run("UpCase.pas");
    }
}