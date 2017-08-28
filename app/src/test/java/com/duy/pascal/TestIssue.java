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

package com.duy.pascal;

import com.duy.pascal.interpreter.BaseTestCase;

/**
 * Created by Duy on 04-Jun-17.
 */

public class TestIssue extends BaseTestCase {

    @Override
    public String getDirTest() {
        return "test_issue";
    }

    public void testConvertString() {
        run("test2.pas");
    }

    public void test4() {
        run("test4.pas");
    }

    public void test5() {
        run("test5.pas");
    }

    public void test6() {
        run("test6.pas");
    }

    public void test7() {
        run("test7.pas");
    }

    public void test8(){
        run("test8.pas");
    }
}
