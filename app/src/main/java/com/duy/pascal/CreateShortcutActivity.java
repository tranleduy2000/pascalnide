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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.file.FileActionCallback;
import com.duy.pascal.frontend.file.FileClipboard;
import com.duy.pascal.frontend.file.FileManager;
import com.duy.pascal.frontend.file.fragment.FragmentFileManager;

import java.io.File;

public class CreateShortcutActivity extends AppCompatActivity
        implements FileActionCallback {

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
    public boolean onSelectFile(@NonNull File file) {
        FileManager fileManager = new FileManager(this);
        Intent intent = fileManager.createShortcutIntent(this, file);
        setResult(RESULT_OK, intent);

        Toast.makeText(this, R.string.shortcut_created, Toast.LENGTH_SHORT).show();
        finish();
        return true;
    }

    @Override
    public boolean onFileLongClick(@NonNull File file) {
        return true;
    }

    @Override
    public boolean doRemoveFile(@NonNull File file) {
        return false;
    }


    @NonNull
    @Override
    public FileClipboard getFileClipboard() {
        if (mFileClipboard == null) {
            mFileClipboard = new FileClipboard();
        }
        return mFileClipboard;
    }
    private FileClipboard mFileClipboard;

}
