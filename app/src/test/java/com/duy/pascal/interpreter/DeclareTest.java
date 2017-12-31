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

import com.duy.pascal.ui.utils.DLog;

/**
 * Created by Duy on 29-May-17.
 */

public class DeclareTest extends BaseTestCase {


    @Override
    public String getDirTest() {
        return "test_declare";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
    }

    public void testDeclareArray() {
        run("test_const_array.pas");
    }

    public void testDeclareBasic() {
        run("test_const_basic.pas");

    }

    public void testIndexEnum() {
        run("test_index_enum.pas");
    }

    public void testInitArray() {
        run("test_init_array.pas");
    }

    public void testInitEnum() {
        run("test_init_enum.pas");
    }

    public void testInitSet() {
        run("test_init_set.pas");
    }

    public void testInitVariable() {
        run("test_init_variable.pas");
    }

    public void testSetType() {
        run("test_const_basic.pas");
    }

    public void testSetType2() {
        run("test_set_type2.pas");

    }

    public void testSetAssignValue() {
        run("test_set_assign_value.pas");

    }

}
