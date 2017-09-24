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

package com.duy.pascal.frontend.editor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.duy.pascal.frontend.BuildConfig;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_sample.activities.CodeSampleActivity;
import com.duy.pascal.frontend.info.InfoActivity;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.setting.SettingsActivity;
import com.duy.pascal.frontend.utils.DonateUtils;
import com.duy.pascal.frontend.utils.StoreUtil;

/**
 * Handler for menu click
 * Created by Duy on 03-Mar-17.
 */

public class MenuEditor {
    @NonNull
    private EditorActivity mActivity;
    @NonNull
    private EditorControl listener;
    private Menu menu;
    private PascalPreferences mPreference;

    public MenuEditor(@NonNull EditorActivity activity, @NonNull EditorControl listener) {
        this.mActivity = activity;
        this.listener = listener;
        mPreference = new PascalPreferences(this.mActivity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        mActivity.getMenuInflater().inflate(R.menu.menu_tool, menu);

        menu.findItem(R.id.action_show_line).setChecked(mPreference.isShowLines());
        menu.findItem(R.id.action_show_symbol).setChecked(mPreference.isShowListSymbol());
        menu.findItem(R.id.action_show_popup).setChecked(mPreference.isShowSuggestPopup());
        menu.findItem(R.id.action_edit_word_wrap).setChecked(mPreference.isWrapText());
        menu.findItem(R.id.action_ime).setChecked(mPreference.useImeKeyboard());

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (menuItem.isCheckable()) menuItem.setChecked(!menuItem.isChecked());
        switch (id) {
            case R.id.action_setting:
                mActivity.startActivity(new Intent(mActivity, SettingsActivity.class));
                break;
            case R.id.action_find:
                mActivity.showDialogFind();

                break;
            case R.id.action_find_and_replace:
                listener.findAndReplace();
                break;
            case R.id.action_doc:
                listener.showDocumentActivity();
                break;
            case R.id.action_new_file:
                listener.createNewSourceFile(null);
                break;
            case R.id.action_code_sample:
                mActivity.startActivity(new Intent(mActivity, CodeSampleActivity.class));
                break;
            case R.id.action_rate:
                StoreUtil.gotoPlayStore(mActivity, BuildConfig.APPLICATION_ID);
                break;
            case R.id.action_more_app:
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
                listener.goToLine();
                break;
            case R.id.action_format:
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
                listener.selectThemeFont();
                break;
            case R.id.action_more_feature:
                mActivity.openDrawer(GravityCompat.END);
                break;
            case R.id.action_translate:
                startActivityTranslate();
                break;
            case R.id.action_info:
                mActivity.startActivity(new Intent(mActivity, InfoActivity.class));
                break;
            case R.id.action_program_structure:
                mActivity.showProgramStructure();
                break;
            case R.id.action_debug:
                mActivity.startDebug();
                break;
            case R.id.action_show_line:
                mPreference.setShowLines(menuItem.isChecked());
                break;
            case R.id.action_show_popup:
                mPreference.setShowSuggestPopup(menuItem.isChecked());
                break;
            case R.id.action_show_symbol:
                mPreference.setShowSymbol(menuItem.isChecked());
                break;
            case R.id.action_edit_word_wrap:
                mPreference.setWordWrap(menuItem.isChecked());
                break;
            case R.id.action_open_file:
                mActivity.openDrawer(GravityCompat.START);
                break;
            case R.id.action_insert_media_url:
                selectMediaUrl();
                break;
            case R.id.action_insert_color:
                mActivity.insertColor();
                break;
            case R.id.action_ime:
                mPreference.setImeMode(menuItem.isChecked());
                break;
            case R.id.action_donate:
                DonateUtils.showDialogDonate(mActivity);
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

    @Nullable
    public EditorControl getListener() {
        return listener;
    }

    public void setListener(@Nullable EditorControl listener) {
        this.listener = listener;
    }

    public boolean getChecked(int action_auto_save) {
        if (menu != null) {
            if (menu.findItem(action_auto_save).isChecked()) {
                return true;
            }
        }
        return false;
    }

}
