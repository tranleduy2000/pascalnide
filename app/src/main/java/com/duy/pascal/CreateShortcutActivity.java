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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.duy.pascal.frontend.activities.ActivitySplashScreen;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.FileActionListener;
import com.duy.pascal.frontend.file.FragmentFileManager;

import java.io.File;

public class CreateShortcutActivity extends AppCompatActivity
        implements FileActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shortcut);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        FragmentFileManager fragmentFileManager = new FragmentFileManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragmentFileManager);
        fragmentTransaction.commit();

        Toast.makeText(this, R.string.create_shortcut_msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileClick(File file) {
        // create shortcut if requested
        Intent.ShortcutIconResource icon =
                Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);

        Intent intent = new Intent();

        Intent launchIntent = new Intent(this, ActivitySplashScreen.class);
        launchIntent.putExtra(CompileManager.FILE_PATH, file.getPath());
        launchIntent.setAction("run_from_shortcut");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, file.getName());
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        setResult(RESULT_OK, intent);
        Toast.makeText(this, R.string.shortcut_created, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFileLongClick(File file) {

    }

    @Override
    public boolean doRemoveFile(File file) {
        return false;
    }
}
