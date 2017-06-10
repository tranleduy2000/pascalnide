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

package com.duy.pascal.test_interpreter;

import com.duy.pascal.frontend.DLog;

import static com.duy.pascal.Interperter.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class ForStatementTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_statement\\for_statement";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
    }

    public void test1() {
        run("test1.pas");
    }

    public void test2() {
        run("test2.pas");
    }

    public void test3() {
        run("test3.pas");
    }

    public void testForInEnum() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\test_statement\\test_for_in_enum.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testForInSet() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\test_statement\\test_for_in_set.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testForInArray() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\test_statement\\test_for_in_array.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }


}
