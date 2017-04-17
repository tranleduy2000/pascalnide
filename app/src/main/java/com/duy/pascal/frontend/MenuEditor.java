package com.duy.pascal.frontend;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.pascal.frontend.activities.EditorActivity;
import com.duy.pascal.frontend.info_application.InfoActivity;
import com.duy.pascal.frontend.sample.CodeSampleActivity;
import com.duy.pascal.frontend.setting.SettingActivity;

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
        activity.getMenuInflater().inflate(R.menu.menu_tool, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_setting:
                activity.startActivity(new Intent(activity, SettingActivity.class));
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
            case R.id.action_more_app:
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
//            case R.id.action_open_tool:
//                listener.openTool();
//                break;
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
            case R.id.action_more_feature:
                activity.openDrawer(GravityCompat.END);
                break;
            case R.id.action_translate:
                showPopupTranslate(activity);
                break;
            case R.id.action_info:
                activity.startActivity(new Intent(activity, InfoActivity.class));
            case R.id.action_program_structure:
                activity.showProgramStructure();
                break;
        }
        return true;
    }

    public void showPopupTranslate(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.help_me_translate).setView(R.layout.dialog_help_translate)
                .setIcon(R.drawable.ic_language_white_24dp);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editTitle = (EditText) alertDialog.findViewById(R.id.edit_title);
        final EditText editContent = (EditText) alertDialog.findViewById(R.id.edit_content);
        final Button btnSend = (Button) alertDialog.findViewById(R.id.btn_email);
        assert btnSend != null;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"tranleduy1233@gmail.com"});
                assert editTitle != null;
                assert editContent != null;
                String content = "Help translate: \n" + editTitle.getText().toString() + "\n"
                        + editContent.getText().toString();
                i.putExtra(Intent.EXTRA_TEXT, content);
                try {
                    activity.startActivity(Intent.createChooser(i, activity.getString(R.string.send_mail)));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(activity, R.string.no_mail_clients, Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }
        });

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

}
