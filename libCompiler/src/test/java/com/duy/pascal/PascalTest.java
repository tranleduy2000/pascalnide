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

import com.duy.pascal.frontend.DLog;

import junit.framework.TestCase;

import java.io.File;

/**
 * Created by Duy on 29-May-17.
 */

public abstract class PascalTest extends TestCase {
    String dir;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;

        dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        dir += File.separator + "test_pascal" + File.separator + "test_statement" + File.separator;
    }
}
