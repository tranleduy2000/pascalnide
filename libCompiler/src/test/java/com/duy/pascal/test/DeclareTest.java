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

import static com.duy.pascal.Compiler.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class DeclareTest extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
    }

    public void testDeclareArray() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_const_array.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testDeclareBasic() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_const_basic.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testIndexEnum() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_index_enum.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testInitArray() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_init_array.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testInitEnum() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_init_enum.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testInitSet() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_init_set.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testInitVariable() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_init_variable.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testSetType() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_const_basic.pas");
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testSetType2() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_set_type2.pas"
            );
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

    public void testSetAssignValue() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_set_assign_value.pas"
            );
            assertTrue("result ", true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("result ", false);
        }
    }

}
