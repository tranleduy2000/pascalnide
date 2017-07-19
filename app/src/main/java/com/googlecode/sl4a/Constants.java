/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.sl4a;

import android.content.ComponentName;

public interface Constants {

    String ACTION_LAUNCH_FOREGROUND_SCRIPT =
            "com.googlecode.android_scripting.action.LAUNCH_FOREGROUND_SCRIPT";
    String ACTION_LAUNCH_BACKGROUND_SCRIPT =
            "com.googlecode.android_scripting.action.LAUNCH_BACKGROUND_SCRIPT";
    String ACTION_LAUNCH_INTERPRETER =
            "com.googlecode.android_scripting.action.LAUNCH_INTERPRETER";


    String EXTRA_RESULT = "SCRIPT_RESULT";
    String EXTRA_SCRIPT_PATH =
            "com.googlecode.android_scripting.extra.SCRIPT_PATH";
    String EXTRA_INTERPRETER_NAME =
            "com.googlecode.android_scripting.extra.INTERPRETER_NAME";

    String EXTRA_TASK_ID = "com.googlecode.android_scripting.extra.EXTRA_TASK_ID";

    // BluetoothDeviceManager
    String EXTRA_DEVICE_ADDRESS =
            "com.googlecode.android_scripting.extra.device_address";

    ComponentName SL4A_SERVICE_LAUNCHER_COMPONENT_NAME = new ComponentName(
            "com.googlecode.android_scripting",
            "com.googlecode.android_scripting.activity.ScriptingLayerServiceLauncher");
    ComponentName BLUETOOTH_DEVICE_LIST_COMPONENT_NAME = new ComponentName(
            "com.googlecode.android_scripting",
            "com.googlecode.android_scripting.activity.BluetoothDeviceList");
    ComponentName TRIGGER_SERVICE_COMPONENT_NAME = new ComponentName(
            "com.googlecode.android_scripting",
            "com.googlecode.android_scripting.activity.TriggerService");

    // Preference Keys

    String FORCE_BROWSER = "helpForceBrowser";
    String HIDE_NOTIFY = "hideServiceNotifications";
}