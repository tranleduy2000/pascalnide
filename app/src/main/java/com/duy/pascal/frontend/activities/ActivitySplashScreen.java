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

package com.duy.pascal.frontend.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.editor.EditorActivity;
import com.duy.pascal.frontend.file.FileManager;
import com.duy.pascal.frontend.runnable.ExecuteActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;


public class ActivitySplashScreen extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 11;
    private static final String TAG = "ActivitySplashScreen";
    private static final int REQUEST_CHECK_LICENSE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Here, this is the current activity
        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
        } else {
            startMainActivity();
        }
    }

    private boolean isDonateInstalled(String name) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startMainActivity();
                } else {
                    Toast.makeText(this, R.string.permission_denied_storage, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * If receive data from other app (it could be file, text from clipboard),
     * You will be handle data and send to {@link EditorActivity}
     */
    private void startMainActivity() {
        Intent data = getIntent();
        String action = data.getAction();

        if (DLog.DEBUG) Log.d(TAG, "startMainActivity: action = " + action);

        String type = data.getType();
        final Intent intentEdit = new Intent(ActivitySplashScreen.this, EditorActivity.class);
        if (action != null && Intent.ACTION_SEND.equals(action) && type != null) {
            FirebaseAnalytics.getInstance(this).logEvent("open_from_clipboard", new Bundle());
            if (type.equals("text/plain")) {
                handleActionSend(data, intentEdit);
            }
        } else if (action != null && Intent.ACTION_VIEW.equals(action) && type != null) {
            FirebaseAnalytics.getInstance(this).logEvent("open_from_another", new Bundle());
            handleActionView(data, intentEdit);
        } else if (action != null && action.equalsIgnoreCase("run_from_shortcut")) {
            FirebaseAnalytics.getInstance(this).logEvent("run_from_shortcut", new Bundle());
            handleRunProgram(data);
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intentEdit.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
                startActivity(intentEdit);
                finish();
            }
        }, 3000);
    }

    private void handleRunProgram(Intent data) {
        Intent runIntent = new Intent(this, ExecuteActivity.class);
        runIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        runIntent.putExtra(CompileManager.FILE_PATH,
                data.getStringExtra(CompileManager.FILE_PATH));
        overridePendingTransition(0, 0);
        startActivity(runIntent);
        finish();
    }

    private void handleActionView(@NonNull Intent from,
                                  @NonNull Intent to) {
        Log.d(TAG, "handleActionView() called with: from = [" + from + "], to = [" + to + "]");
        if (from.getData().toString().endsWith(".pas") || from.getData().toString().endsWith(".txt")) {
            Uri uriPath = from.getData();
            Log.d(TAG, "handleActionView: " + uriPath.getPath());
            try {
                String path = FileManager.getPathFromUri(this, uriPath);
                to.putExtra(CompileManager.FILE_PATH, path);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else if (from.getType().equals("text/x-pascal")) {
            Uri uri = from.getData();
            try {
                //clone file
                InputStream inputStream = getContentResolver().openInputStream(uri);
                FileManager fileManager = new FileManager(this);
                String filePath = fileManager.createRandomFile(this);
                fileManager.copy(inputStream, new FileOutputStream(filePath));

                to.putExtra(CompileManager.FILE_PATH, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleActionSend(Intent from, Intent to) {
        String text = from.getStringExtra(Intent.EXTRA_TEXT);
        FileManager fileManager = new FileManager(this);
        String filePath = fileManager.createRandomFile(this);
        fileManager.saveFile(filePath, text);
        to.putExtra(CompileManager.FILE_PATH, filePath);
    }
}