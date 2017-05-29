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

package com.duy.pascal.test;

import com.duy.pascal.frontend.DLog;

import junit.framework.TestCase;

import java.io.File;

import static com.duy.pascal.Compiler.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class StatementTest extends TestCase {
    String dir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;

        dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        dir += File.separator + "test_pascal" + File.separator + "test_statement" + File.separator;
    }

    public void testCase() {
        try {
            runProgram(dir + "test_case.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testCaseElse() {
        try {
            runProgram(dir + "test_case_else.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testCaseNested() {
        try {
            runProgram(dir + "test_case_nested.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testIfElse() {
        try {
            runProgram(dir + "test_ifelse.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testIfElseIfElse() {
        try {
            runProgram(dir + "test_ifelse_ifelse.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testNestedIfElse() {
        try {
            runProgram(dir + "test_nested_ifelse.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
