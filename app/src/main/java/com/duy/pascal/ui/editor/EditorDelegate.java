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

package com.duy.pascal.ui.editor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.duy.pascal.ui.BuildConfig;
import com.duy.pascal.ui.EditorControl;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.sample.activities.CodeSampleActivity;
import com.duy.pascal.ui.info.InfoActivity;
import com.duy.pascal.ui.setting.PascalPreferences;
import com.duy.pascal.ui.setting.SettingsActivity;
import com.duy.pascal.ui.utils.StoreUtil;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Handler for menu click
 * Created by Duy on 03-Mar-17.
 */

public class EditorDelegate {
    @NonNull
    private EditorActivity mActivity;
    @NonNull
    private EditorControl listener;
    private Menu menu;
    private PascalPreferences mPreference;
    private FirebaseAnalytics mAnalytics;

    public EditorDelegate(@NonNull EditorActivity activity, @NonNull EditorControl listener) {
        this.mActivity = activity;
        this.listener = listener;
        mPreference = new PascalPreferences(activity);
        mAnalytics = FirebaseAnalytics.getInstance(activity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        mActivity.getMenuInflater().inflate(R.menu.menu_tool, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (menuItem.isCheckable()) menuItem.setChecked(!menuItem.isChecked());
        switch (id) {
            case R.id.action_setting:
                mAnalytics.logEvent("action_setting", new Bundle());
                mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
                break;
            case R.id.action_find:
                mAnalytics.logEvent("action_find", new Bundle());

                mActivity.showDialogFind();
                break;
            case R.id.action_find_and_replace:
                mAnalytics.logEvent("action_find_and_replace", new Bundle());
                listener.findAndReplace();
                break;
            case R.id.action_doc:
                listener.showDocumentActivity();
                break;
            case R.id.action_new_file:
                listener.createNewSourceFile(null);
                break;
            case R.id.action_code_sample:
                mAnalytics.logEvent("action_code_sample", new Bundle());
                mActivity.startActivity(new Intent(mActivity, CodeSampleActivity.class));
                break;
            case R.id.action_rate:
                mAnalytics.logEvent("action_rate", new Bundle());
                StoreUtil.gotoPlayStore(mActivity, BuildConfig.APPLICATION_ID);
                break;
            case R.id.action_more_app:
                mAnalytics.logEvent("action_more_app", new Bundle());
                StoreUtil.moreApp(mActivity);
                break;
            case R.id.nav_run:
                listener.runProgram();
                break;
            case R.id.action_compile:
                listener.doCompile();
                break;
            case R.id.action_save:
                listener.saveFile();
                break;
            case R.id.action_save_as:
                listener.saveAs();
                break;
            case R.id.action_goto_line:
                mAnalytics.logEvent("action_goto_line", new Bundle());
                listener.goToLine();
                break;
            case R.id.action_format:
                mAnalytics.logEvent("action_format", new Bundle());
                listener.formatCode();
                break;
            case R.id.action_report_bug:
                listener.reportBug();
                break;
            case R.id.action_undo:
                listener.undo();
                break;
            case R.id.action_redo:
                listener.redo();
                break;
            case R.id.action_paste:
                listener.paste();
                break;
            case R.id.action_copy_all:
                listener.copyAll();
                break;
            case R.id.action_select_theme:
                mAnalytics.logEvent("action_select_theme", new Bundle());
                listener.selectThemeFont();
                break;
            case R.id.action_more_feature:
                mActivity.openDrawer(GravityCompat.END);
                break;
            case R.id.action_translate:
                mAnalytics.logEvent("action_translate", new Bundle());
                startActivityTranslate();
                break;
            case R.id.action_info:
                mAnalytics.logEvent("action_info", new Bundle());
                mActivity.startActivity(new Intent(mActivity, InfoActivity.class));
                break;
            case R.id.action_program_structure:
                mActivity.showProgramStructure();
                break;
            case R.id.action_debug:
                mAnalytics.logEvent("action_debug", new Bundle());
                mActivity.startDebug();
                break;
            case R.id.action_open_file:
                mActivity.openDrawer(GravityCompat.START);
                break;
            case R.id.action_insert_media_url:
                selectMediaUrl();
                break;
            case R.id.action_insert_color:
                mAnalytics.logEvent("action_insert_color", new Bundle());
                mActivity.insertColor();
                break;
        }
        return true;
    }


    private void selectMediaUrl() {
        Intent i = new Intent();
        i.setType("audio/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        Intent intent = Intent.createChooser(i, "Complete action using");
        mActivity.startActivityForResult(intent, EditorActivity.ACTION_PICK_MEDIA_URL);
    }

    public void startActivityTranslate() {
        String link = "http://osewnui.oneskyapp.com/collaboration/project?id=272800";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        mActivity.startActivity(intent);
    }

    @NonNull
    public EditorControl getListener() {
        return listener;
    }

    public void setListener(@NonNull EditorControl listener) {
        this.listener = listener;
    }

    boolean getChecked(int action_auto_save) {
        if (menu != null) {
            if (menu.findItem(action_auto_save).isChecked()) {
                return true;
            }
        }
        return false;
    }

}
