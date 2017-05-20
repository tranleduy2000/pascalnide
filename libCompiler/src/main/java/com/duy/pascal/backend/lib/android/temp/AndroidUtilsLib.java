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

package com.duy.pascal.backend.lib.android.temp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StatFs;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.pascal.BasePascalApplication;
import com.duy.pascal.backend.lib.AndroidLibraryUtils;
import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.android.activity.PascalActivityTask;
import com.duy.pascal.backend.lib.android.activity.PascalActivityTaskExecutor;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.FileUtils;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.RpcDefault;
import com.googlecode.sl4a.rpc.RpcDeprecated;
import com.googlecode.sl4a.rpc.RpcOptional;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Some general purpose Android routines.<br>
 * <h2>Intents</h2> Intents are returned as a map, in the following form:<br>
 * <ul>
 * <li><b>action</b> - action.
 * <li><b>data</b> - url
 * <li><b>operator</b> - mime operator
 * <li><b>packagename</b> - name of package. If used, requires classname to be useful (optional)
 * <li><b>classname</b> - name of class. If used, requires packagename to be useful (optional)
 * <li><b>categories</b> - list of categories
 * <li><b>extras</b> - map of extras
 * <li><b>flags</b> - integer flags.
 * </ul>
 * <br>
 * An intent can be built using the {@see #makeIntent} call, but can also be constructed exterally.
 */
public class AndroidUtilsLib implements PascalLibrary {
    public static final String NAME = "aUtils".toLowerCase();
    private Context mContext;
    private Handler mHandler;
    private PascalActivityTaskExecutor mTaskQueue;

    public AndroidUtilsLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        if (mContext != null) {
            BasePascalApplication application = ((BasePascalApplication) mContext);
            mTaskQueue = application.getTaskExecutor();
            mHandler = new Handler(mContext.getMainLooper());
        }
    }

    public Intent startActivityForResult(final Intent intent) {
        PascalActivityTask<Intent> task = new PascalActivityTask<Intent>() {
            @Override
            public void onCreate() {
                super.onCreate();
                try {
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    intent.putExtra("EXCEPTION", e.getMessage());
                    setResult(intent);
                }
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                setResult(data);
            }
        };
        mTaskQueue.execute(task);
        try {
            return task.getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            task.finish();
        }
    }

    public void startActivity(final Intent intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            Log.e("Failed to launch intent.", e);
        }
    }

    private Intent buildIntent(String action, String uri, String type, JSONObject extras,
                               String packagename, String classname, JSONArray categories) throws JSONException {
        Intent intent = new Intent(action);
        intent.setDataAndType(uri != null ? Uri.parse(uri) : null, type);
        if (packagename != null && classname != null) {
            intent.setComponent(new ComponentName(packagename, classname));
        }
        if (extras != null) {
            AndroidLibraryUtils.putExtrasFromJsonObject(extras, intent);
        }
        if (categories != null) {
            for (int i = 0; i < categories.length(); i++) {
                intent.addCategory(categories.getString(i));
            }
        }
        return intent;
    }

    // TODO(damonkohler): It's unnecessary to add the complication of choosing between startActivity
    // and startActivityForResult. It's probably better to just always use the ForResult version.
    // However, this makes the call always blocking. We'd need to add an extra boolean parameter to
    // indicate if we should wait for a result.
    @PascalMethod(description = "Starts an activity and returns the result.", returns = "A Map representation of the result Intent.")
    public Intent startActivityForResult(
            @PascalParameter(name = "action") String action,
            @PascalParameter(name = "uri") @RpcOptional String uri,
            @PascalParameter(name = "operator", description = "MIME operator/subtype of the URI") @RpcOptional String type,
            @PascalParameter(name = "extras", description = "a Map of extras to add to the Intent") @RpcOptional JSONObject extras,
            @PascalParameter(name = "packagename", description = "name of package. If used, requires classname to be useful") @RpcOptional String packagename,
            @PascalParameter(name = "classname", description = "name of class. If used, requires packagename to be useful") @RpcOptional String classname)
            throws JSONException {
        final Intent intent = buildIntent(action, uri, type, extras, packagename, classname, null);
        return startActivityForResult(intent);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Starts an activity and returns the result.", returns = "A Map representation of the result Intent.")
    public Intent startActivityForResultIntent(
            @PascalParameter(name = "intent", description = "Intent in the format as returned from makeIntent") Intent intent) {
        return startActivityForResult(intent);
    }

    private void doStartActivity(final Intent intent, Boolean wait) throws Exception {
        if (wait == null || !wait) {
            startActivity(intent);
        } else {
            PascalActivityTask<Intent> task = new PascalActivityTask<Intent>() {
                private boolean mSecondResume = false;

                @Override
                public void onCreate() {
                    super.onCreate();
                    startActivity(intent);
                }

                @Override
                public void onResume() {
                    super.onResume();
                    if (mSecondResume) {
                        finish();
                    }
                    mSecondResume = true;
                }

                @Override
                public void onDestroy() {
                    super.onDestroy();
                    setResult(null);
                }

            };
            mTaskQueue.execute(task);

            try {
                task.getResult();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * packagename and classname, if provided, are used in a 'setComponent' call.
     */
    @PascalMethod(description = "Starts an activity.")
    public void startActivity(
            @PascalParameter(name = "action") String action,
            @PascalParameter(name = "uri") @RpcOptional String uri,
            @PascalParameter(name = "operator", description = "MIME operator/subtype of the URI") @RpcOptional String type,
            @PascalParameter(name = "extras", description = "a Map of extras to add to the Intent") @RpcOptional JSONObject extras,
            @PascalParameter(name = "wait", description = "block until the user exits the started activity") @RpcOptional Boolean wait,
            @PascalParameter(name = "packagename", description = "name of package. If used, requires classname to be useful") @RpcOptional String packagename,
            @PascalParameter(name = "classname", description = "name of class. If used, requires packagename to be useful") @RpcOptional String classname)
            throws Exception {
        final Intent intent = buildIntent(action, uri, type, extras, packagename, classname, null);
        doStartActivity(intent, wait);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Send a broadcast.")
    public void sendBroadcast(
            @PascalParameter(name = "action") String action,
            @PascalParameter(name = "uri") @RpcOptional String uri,
            @PascalParameter(name = "operator", description = "MIME operator/subtype of the URI") @RpcOptional String type,
            @PascalParameter(name = "extras", description = "a Map of extras to add to the Intent") @RpcOptional JSONObject extras,
            @PascalParameter(name = "packagename", description = "name of package. If used, requires classname to be useful") @RpcOptional String packagename,
            @PascalParameter(name = "classname", description = "name of class. If used, requires packagename to be useful") @RpcOptional String classname)
            throws JSONException {
        final Intent intent = buildIntent(action, uri, type, extras, packagename, classname, null);
        try {
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("Failed to broadcast intent.", e);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Create an Intent.", returns = "An object representing an Intent")
    public Intent makeIntent(
            @PascalParameter(name = "action") String action,
            @PascalParameter(name = "uri") @RpcOptional String uri,
            @PascalParameter(name = "operator", description = "MIME operator/subtype of the URI") @RpcOptional String type,
            @PascalParameter(name = "extras", description = "a Map of extras to add to the Intent") @RpcOptional JSONObject extras,
            @PascalParameter(name = "categories", description = "a List of categories to add to the Intent") @RpcOptional JSONArray categories,
            @PascalParameter(name = "packagename", description = "name of package. If used, requires classname to be useful") @RpcOptional String packagename,
            @PascalParameter(name = "classname", description = "name of class. If used, requires packagename to be useful") @RpcOptional String classname,
            @PascalParameter(name = "flags", description = "Intent flags") @RpcOptional Integer flags)
            throws JSONException {
        Intent intent = buildIntent(action, uri, type, extras, packagename, classname, categories);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (flags != null) {
            intent.setFlags(flags);
        }
        return intent;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Start Activity using Intent")
    public void startActivityIntent(
            @PascalParameter(name = "intent", description = "Intent in the format as returned from makeIntent") Intent intent,
            @PascalParameter(name = "wait", description = "block until the user exits the started activity") @RpcOptional Boolean wait)
            throws Exception {
        doStartActivity(intent, wait);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Send Broadcast Intent")
    public void sendBroadcastIntent(
            @PascalParameter(name = "intent", description = "Intent in the format as returned from makeIntent") Intent intent)
            throws Exception {
        mContext.sendBroadcast(intent);
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Displays a short-duration Toast notification.")
    public void makeToast(@PascalParameter(name = "message") final String message) {
        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getInputFromAlertDialog(final String title, final String message,
                                           final boolean password) {
        final PascalActivityTask<String> task = new PascalActivityTask<String>() {
            @Override
            public void onCreate() {
                super.onCreate();
                final EditText input = new EditText(getActivity());
                if (password) {
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    input.setTransformationMethod(new PasswordTransformationMethod());
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(title);
                alert.setMessage(message);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        setResult(input.getText().toString());
                        finish();
                    }
                });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        setResult(null);
                        finish();
                    }
                });
                alert.show();
            }
        };
        mTaskQueue.execute(task);

        try {
            return task.getResult();
        } catch (Exception e) {
            Log.e("Failed to display dialog.", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Queries the user for a text input.")
    @RpcDeprecated(value = "dialogGetInput", release = "r3")
    public String getInput(
            @PascalParameter(name = "title", description = "title of the input box") @RpcDefault("SL4A Input") final String title,
            @PascalParameter(name = "message", description = "message to display above the input box") @RpcDefault("Please enter value:") final String message) {
        return getInputFromAlertDialog(title, message, false);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Queries the user for a password.")
    @RpcDeprecated(value = "dialogGetPassword", release = "r3")
    public String getPassword(
            @PascalParameter(name = "title", description = "title of the input box") @RpcDefault("SL4A Password Input") final String title,
            @PascalParameter(name = "message", description = "message to display above the input box") @RpcDefault("Please enter password:") final String message) {
        return getInputFromAlertDialog(title, message, true);
    }


    @SuppressWarnings("unused")
    @PascalMethod(description = "Launches an activity that sends an e-mail message to a given recipient.")
    public void sendEmail(
            @PascalParameter(name = "to", description = "A comma separated list of recipients.") final String to,
            @PascalParameter(name = "subject") final String subject,
            @PascalParameter(name = "body") final String body,
            @PascalParameter(name = "attachmentUri") @RpcOptional final String attachmentUri) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, to.split(","));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (attachmentUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(attachmentUri));
        }
        startActivity(intent);
    }

    @PascalMethod(description = "Returns package version code.")
    public int getPackageVersionCode(@PascalParameter(name = "packageName") final String packageName) {
        int result = -1;
        PackageInfo pInfo;
        try {
            pInfo =
                    mContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            pInfo = null;
        }
        if (pInfo != null) {
            result = pInfo.versionCode;
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns package version name.")
    public String getPackageVersion(@PascalParameter(name = "packageName") final String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo =
                    mContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            return null;
        }
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return null;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Checks if version of SL4A is greater than or equal to the specified version.")
    public boolean requiredVersion(@PascalParameter(name = "requiredVersion") final Integer version) {
        boolean result = false;
        int packageVersion = getPackageVersionCode("com.googlecode.android_scripting");
        if (version > -1) {
            result = (packageVersion >= version);
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Writes message to logcat.")
    public void log(@PascalParameter(name = "message") String message) {
        android.util.Log.v("SCRIPT", message);
    }

    /**
     * Map returned:
     * <p>
     * <pre>
     *   TZ = Timezone
     *     id = Timezone ID
     *     display = Timezone display name
     *     offset = Offset from UTC (in ms)
     *   SDK = SDK Version
     *   download = default download path
     *   appcache = Location of application cache
     *   sdcard = Space on sdcard
     *     availblocks = Available blocks
     *     blockcount = Total Blocks
     *     blocksize = size of block.
     * </pre>
     */
    @SuppressWarnings("unused")
    @PascalMethod(description = "A map of various useful environment details")
    public Map<String, Object> environment() {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> zone = new HashMap<>();
        Map<String, Object> space = new HashMap<>();
        TimeZone tz = TimeZone.getDefault();
        zone.put("id", tz.getID());
        zone.put("display", tz.getDisplayName());
        zone.put("offset", tz.getOffset((new Date()).getTime()));
        result.put("TZ", zone);
        result.put("SDK", android.os.Build.VERSION.SDK);
        result.put("download", FileUtils.getExternalDownload().getAbsolutePath());
        result.put("appcache", mContext.getCacheDir().getAbsolutePath());
        try {
            StatFs fs = new StatFs("/sdcard");
            space.put("availblocks", fs.getAvailableBlocks());
            space.put("blocksize", fs.getBlockSize());
            space.put("blockcount", fs.getBlockCount());
        } catch (Exception e) {
            space.put("exception", e.toString());
        }
        result.put("sdcard", space);
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Get list of constants (static final fields) for a class")
    public Bundle getConstants(
            @PascalParameter(name = "classname", description = "Class to get constants from") String classname)
            throws Exception {
        Bundle result = new Bundle();
        int flags = Modifier.FINAL | Modifier.PUBLIC | Modifier.STATIC;
        Class<?> clazz = Class.forName(classname);
        for (Field field : clazz.getFields()) {
            if ((field.getModifiers() & flags) == flags) {
                Class<?> type = field.getType();
                String name = field.getName();
                if (type == int.class) {
                    result.putInt(name, field.getInt(null));
                } else if (type == long.class) {
                    result.putLong(name, field.getLong(null));
                } else if (type == double.class) {
                    result.putDouble(name, field.getDouble(null));
                } else if (type == char.class) {
                    result.putChar(name, field.getChar(null));
                } else if (type instanceof Object) {
                    result.putString(name, field.get(null).toString());
                }
            }
        }
        return result;
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")

    public void shutdown() {

    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }

    /**
     * An instance of this interface is passed to the facade. From this object, the resource IDs can
     * be obtained.
     */
    public interface Resources {
        int getLogo48();
    }
}
