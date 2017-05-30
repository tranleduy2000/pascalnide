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

/**
 * Created by Duy on 29-May-17.
 */

public class VariableScopeTest extends BaseTestCase {

    public void testLocal() {
        run("test_local.pas");
    }

    public void testLocal1() {
        run("test_local1.pas");
    }

    public void testGobal() {
        run("test_gobal.pas");
    }

    public void testGobal1() {
        run("test_gobal1.pas");
    }

    @Override
    public String getDirTest() {
        return "test_scope";
    }


}
