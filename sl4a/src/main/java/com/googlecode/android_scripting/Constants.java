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

package com.googlecode.android_scripting;

import android.content.ComponentName;

public interface Constants {

    String ACTION_LAUNCH_FOREGROUND_SCRIPT =
            "com.googlecode.android_scripting.action.LAUNCH_FOREGROUND_SCRIPT";
    String ACTION_LAUNCH_BACKGROUND_SCRIPT =
            "com.googlecode.android_scripting.action.LAUNCH_BACKGROUND_SCRIPT";
    String ACTION_LAUNCH_SCRIPT_FOR_RESULT =
            "com.googlecode.android_scripting.action.ACTION_LAUNCH_SCRIPT_FOR_RESULT";
    String ACTION_LAUNCH_INTERPRETER =
            "com.googlecode.android_scripting.action.LAUNCH_INTERPRETER";
    String ACTION_EDIT_SCRIPT =
            "com.googlecode.android_scripting.action.EDIT_SCRIPT";
    String ACTION_SAVE_SCRIPT =
            "com.googlecode.android_scripting.action.SAVE_SCRIPT";
    String ACTION_SAVE_AND_RUN_SCRIPT =
            "com.googlecode.android_scripting.action.SAVE_AND_RUN_SCRIPT";
    String ACTION_KILL_PROCESS =
            "com.googlecode.android_scripting.action.KILL_PROCESS";
    String ACTION_KILL_ALL = "com.googlecode.android_scripting.action.KILL_ALL";
    String ACTION_SHOW_RUNNING_SCRIPTS =
            "com.googlecode.android_scripting.action.SHOW_RUNNING_SCRIPTS";
    String ACTION_CANCEL_NOTIFICATION =
            "com.googlecode.android_scripting.action.CANCEL_NOTIFICAITON";
    String ACTION_ACTIVITY_RESULT =
            "com.googlecode.android_scripting.action.ACTIVITY_RESULT";
    String ACTION_LAUNCH_SERVER =
            "com.googlecode.android_scripting.action.LAUNCH_SERVER";

    String EXTRA_RESULT = "SCRIPT_RESULT";
    String EXTRA_SCRIPT_PATH =
            "com.googlecode.android_scripting.extra.SCRIPT_PATH";
    String EXTRA_SCRIPT_CONTENT =
            "com.googlecode.android_scripting.extra.SCRIPT_CONTENT";
    String EXTRA_INTERPRETER_NAME =
            "com.googlecode.android_scripting.extra.INTERPRETER_NAME";

    String EXTRA_USE_EXTERNAL_IP =
            "com.googlecode.android_scripting.extra.USE_PUBLIC_IP";
    String EXTRA_USE_SERVICE_PORT =
            "com.googlecode.android_scripting.extra.USE_SERVICE_PORT";
    String EXTRA_SCRIPT_TEXT =
            "com.googlecode.android_scripting.extra.SCRIPT_TEXT";
    String EXTRA_RPC_HELP_TEXT =
            "com.googlecode.android_scripting.extra.RPC_HELP_TEXT";
    String EXTRA_API_PROMPT_RPC_NAME =
            "com.googlecode.android_scripting.extra.API_PROMPT_RPC_NAME";
    String EXTRA_API_PROMPT_VALUES =
            "com.googlecode.android_scripting.extra.API_PROMPT_VALUES";
    String EXTRA_PROXY_PORT = "com.googlecode.android_scripting.extra.PROXY_PORT";
    String EXTRA_PROCESS_ID =
            "com.googlecode.android_scripting.extra.SCRIPT_PROCESS_ID";
    String EXTRA_IS_NEW_SCRIPT =
            "com.googlecode.android_scripting.extra.IS_NEW_SCRIPT";
    String EXTRA_TRIGGER_ID =
            "com.googlecode.android_scripting.extra.EXTRA_TRIGGER_ID";
    String EXTRA_LAUNCH_IN_BACKGROUND =
            "com.googlecode.android_scripting.extra.EXTRA_LAUNCH_IN_BACKGROUND";
    String EXTRA_TASK_ID = "com.googlecode.android_scripting.extra.EXTRA_TASK_ID";

    // BluetoothDeviceManager
    String EXTRA_DEVICE_ADDRESS =
            "com.googlecode.android_scripting.extra.device_address";

    ComponentName SL4A_SERVICE_COMPONENT_NAME = new ComponentName(
            "com.googlecode.android_scripting",
            "com.googlecode.android_scripting.activity.ScriptingLayerService");
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