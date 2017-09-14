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
 * Created by Duy on 29-May-17.
 */

public class ArrayTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_array";
    }

    public void testAccess() {
        run("test_index_access.pas");
    }

    public void testDynamic() {
        run("test_dynamic_array.pas");
    }

    public void testDynamic2() {
        run("test_dynamic_2.pas");
    }

    public void testMultiDimenArray() {
        run("test_multi_dimensional_array.pas");
    }

    public void testPacked() {
        run("test_packed_array.pas");
    }

    public void testParseArrayInFun() {
        run("test_parse_array_in_function.pas");
    }
}
