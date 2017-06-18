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
 * Created by Duy on 16-Jun-17.
 */

public class ClassTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_class";
    }

    public void testParse() {
        assertTrue(parse("test_parse.pas"));
    }

    public void testParse2() {
        assertTrue(parse("test_parse2.pas"));
    }

    public void test1() {
        run("test_parse2.pas");
    }

    public void test3() {
        run("test3.pas");
    }

    public void test4() {
        run("test4.pas");
    }

    public void test5() {
        run("test5.pas");
    }

    public void testCreate() {
        run("test_create.pas");
    }

    public void testModifier() {
        run("test_modifier.pas");
    }

    public void testField() {
        run("test_field.pas");
    }

    public void testStaticField() {
        run("test_static_field.pas");
    }

    public void testClassInClass() {
        run("test_class_in_class.pas");
    }
}
