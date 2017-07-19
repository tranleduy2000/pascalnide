package com.duy.pascal.java;/*
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


import com.duy.pascal.interpreter.BaseTestCase;

import java.io.File;

/**
 * Created by Duy on 17-Jun-17.
 */

public class JavaCollectionsTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_libraries" + File.separator + "javacollections";
    }


    public void testArrayList() {
        run("ArrayList.pas");
    }

    public void testHashMap() {
        run("HashMap.pas");
    }

    public void testHashSet() {
        run("HashSet.pas");

    }

    public void testLinkedHashMap() {
        run("LinkedHashMap.pas");

    }

    public void testLinkedHashSet() {
        run("LinkedHashSet.pas");

    }

    public void testLinkedList() {
        run("LinkedList.pas");

    }

    public void testStack() {
        run("Stack.pas");

    }

    public void testTreeSet() {
        run("TreeSet.pas");

    }

    public void testVector() {
        run("Vector.pas");

    }
}
