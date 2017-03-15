package com.duy.pascal.compiler;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duy.pascal.compiler.activities.CodeSampleActivity;
import com.duy.pascal.compiler.activities.EditorActivity;
import com.duy.pascal.compiler.activities.SettingsActivity;

/**
 * Handler for menu click
 * Created by Duy on 03-Mar-17.
 */

public class MenuEditor {
    private EditorActivity activity;
    private EditorControl listener;
    private Menu menu;

    public MenuEditor(EditorActivity activity, EditorControl listener) {
        this.activity = activity;
        this.listener = listener;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        activity.getMenuInflater().inflate(R.menu.menu_build, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                activity.startActivity(new Intent(activity, SettingsActivity.class));
                break;
            case R.id.action_find:
                activity.showDialogFind();
                break;
            case R.id.action_find_and_replace:
                listener.findAndReplace();
                break;
//            case R.id.action_open_file:
//                listener.chooseFile(null);
//                break;
            case R.id.action_doc:
                listener.showDocumentActivity();
                break;
            case R.id.action_new_file:
                listener.createNewSourceFile(null);
                break;
            case R.id.action_code_sample:
                activity.startActivity(new Intent(activity, CodeSampleActivity.class));
                break;
            case R.id.action_rate:
                activity.rateApp();
                break;
            case R.id.action_more:
                activity.moreApp(null);
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
            case R.id.action_check_update:
                listener.checkUpdate();
                break;
            case R.id.action_open_tool:
                listener.openTool();
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
                listener.selectTheme();
                break;
        }
        return true;
    }

    public EditorControl getListener() {
        return listener;
    }

    public void setListener(EditorControl listener) {
        this.listener = listener;
    }

    public boolean getChecked(int action_auto_save) {
        if (menu != null) {
            return menu.findItem(action_auto_save).isChecked();
        }
        return false;
    }

    public interface EditorControl {
        boolean doCompile();

        void saveAs();

        void findAndReplace();

        void runProgram();

        boolean isAutoSave();

        void saveFile();

        void showDocumentActivity();

        void chooseFile(View o);

        void createNewSourceFile(View view);

        void goToLine();

        void formatCode();

        void checkUpdate();

        void reportBug();

        void openTool();

        void undo();

        void redo();

        void paste();

        void copyAll();

        void selectTheme();
    }
}
