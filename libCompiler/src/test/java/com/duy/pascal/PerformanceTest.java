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
 * Created by Duy on 10-Jun-17.
 */

public class PerformanceTest extends BaseTestCase {

    @Override
    public String getDirTest() {
        return "test_performance";
    }

    public void testFor() {
        run("test_for.pas");

    }

    public void testFor2() {
        run("test_for2.pas");

    }

    public void testFor3() {
        run("test_for3.pas");

    }

    public void testFor4() {
        run("test_for3.pas");

    }

    public void testFor5() {
        run("test_for3.pas");

    }

    public void testFor6() {
        run("test_for6.pas");

    }
}
