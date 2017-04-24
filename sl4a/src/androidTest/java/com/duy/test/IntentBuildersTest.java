/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.test;

import android.content.Intent;

import com.googlecode.android_scripting.Constants;
import com.googlecode.android_scripting.IntentBuilders;

import junit.framework.TestCase;

import java.io.File;

public class IntentBuildersTest extends TestCase {

    private static final String mScriptPath = "/foo/bar/baz";
    private final File mScript;

    public IntentBuildersTest() {
        mScript = new File(mScriptPath);
    }

    public void testBuildStartInBackgroundIntent() {
        Intent intent = IntentBuilders.buildStartInBackgroundIntent(mScript);
        assertEquals(mScriptPath, intent.getStringExtra(Constants.EXTRA_SCRIPT_PATH));
    }

    public void testBuildStartInTerminalIntent() {
        Intent intent = IntentBuilders.buildStartInTerminalIntent(mScript);
        assertEquals(mScriptPath, intent.getStringExtra(Constants.EXTRA_SCRIPT_PATH));
    }
}
