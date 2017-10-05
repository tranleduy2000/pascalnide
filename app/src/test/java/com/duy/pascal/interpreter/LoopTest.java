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

import com.duy.pascal.ui.DLog;

import junit.framework.TestCase;

import java.io.File;

import static com.duy.pascal.Interpreter.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class LoopTest extends TestCase {
    String dir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;

        dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        dir += File.separator + "test_pascal" + File.separator + "test_loop" + File.separator;
    }

    public void testBreak() {
        try {
            runProgram(dir + "test_break.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testContinue() {
        try {
            runProgram(dir + "test_continue.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testFor() {
        try {
            runProgram(dir + "test_for.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testNested() {
        try {
            runProgram(dir + "test_nested.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testRepeat() {
        try {
            runProgram(dir + "test_repeat.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testWhile() {
        try {
            runProgram(dir + "test_while.pas");
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
