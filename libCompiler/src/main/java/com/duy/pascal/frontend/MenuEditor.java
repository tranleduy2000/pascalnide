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

package com.duy.pascal.frontend;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.pascal.frontend.editor.EditorActivity;
import com.duy.pascal.frontend.code_sample.activities.CodeSampleActivity;
import com.duy.pascal.frontend.info.InfoActivity;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.setting.SettingActivity;

/**
 * Handler for menu click
 * Created by Duy on 03-Mar-17.
 */

public class MenuEditor {
    @NonNull
    private EditorActivity activity;
    @Nullable
    private EditorControl listener;
    private Menu menu;
    private PascalPreferences pascalPreferences;

    public MenuEditor(@NonNull EditorActivity activity, @Nullable EditorControl listener) {
        this.activity = activity;
        this.listener = listener;
        pascalPreferences = new PascalPreferences(this.activity);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        activity.getMenuInflater().inflate(R.menu.menu_tool, menu);

        /*
         * set state for menu editor
         */
        menu.findItem(R.id.action_show_line).setChecked(pascalPreferences.isShowLines());
        menu.findItem(R.id.action_show_symbol).setChecked(pascalPreferences.isShowListSymbol());
        menu.findItem(R.id.action_show_popup).setChecked(pascalPreferences.isShowSuggestPopup());
        menu.findItem(R.id.action_edit_word_wrap).setChecked(pascalPreferences.isWrapText());
        menu.findItem(R.id.action_ime).setChecked(pascalPreferences.useImeKeyboard());

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (menuItem.isCheckable()) menuItem.setChecked(!menuItem.isChecked());
        if (id == R.id.action_setting) {
            activity.startActivity(new Intent(activity, SettingActivity.class));

        } else if (id == R.id.action_find) {
            activity.showDialogFind();

        } else if (id == R.id.action_find_and_replace) {
            if (listener != null) {
                listener.findAndReplace();
            }

//            case R.id.action_open_file:
//                listener.chooseFile(null);
//                break;
        } else if (id == R.id.action_doc) {
            if (listener != null) {
                listener.showDocumentActivity();
            }

        } else if (id == R.id.action_new_file) {
            if (listener != null) {
                listener.createNewSourceFile(null);
            }

        } else if (id == R.id.action_code_sample) {
            activity.startActivity(new Intent(activity, CodeSampleActivity.class));

        } else if (id == R.id.action_rate) {
            activity.goToPlayStore();

        } else if (id == R.id.action_more_app) {
            activity.moreApp();

        } else if (id == R.id.nav_run) {
            if (listener != null) {
                listener.runProgram();
            }

        } else if (id == R.id.action_compile) {
            if (listener != null) {
                listener.doCompile();
            }

        } else if (id == R.id.action_save) {
            if (listener != null) {
                listener.saveFile();
            }

        } else if (id == R.id.action_save_as) {
            if (listener != null) {
                listener.saveAs();
            }

        } else if (id == R.id.action_goto_line) {
            if (listener != null) {
                listener.goToLine();
            }

        } else if (id == R.id.action_format) {
            if (listener != null) {
                listener.formatCode();
            }

        } else if (id == R.id.action_report_bug) {
            if (listener != null) {
                listener.reportBug();
            }

        } else if (id == R.id.action_check_update) {
            if (listener != null) {
                listener.checkUpdate();
            }

//            case R.id.action_open_tool:
//                listener.openTool();
//                break;
        } else if (id == R.id.action_undo) {
            if (listener != null) {
                listener.undo();
            }

        } else if (id == R.id.action_redo) {
            if (listener != null) {
                listener.redo();
            }

        } else if (id == R.id.action_paste) {
            if (listener != null) {
                listener.paste();
            }

        } else if (id == R.id.action_copy_all) {
            if (listener != null) {
                listener.copyAll();
            }

        } else if (id == R.id.action_select_theme) {
            if (listener != null) {
                listener.selectThemeFont();
            }

        } else if (id == R.id.action_more_feature) {
            activity.openDrawer(GravityCompat.END);

        } else if (id == R.id.action_translate) {
            showPopupTranslate(activity);

        } else if (id == R.id.action_info) {
            activity.startActivity(new Intent(activity, InfoActivity.class));

        } else if (id == R.id.action_program_structure) {
            activity.showProgramStructure();

        } else if (id == R.id.action_debug) {
            activity.startDebug();

        } else if (id == R.id.action_show_line) {
            pascalPreferences.setShowLines(menuItem.isChecked());


        } else if (id == R.id.action_show_popup) {
            pascalPreferences.setShowSuggestPopup(menuItem.isChecked());

        } else if (id == R.id.action_show_symbol) {
            pascalPreferences.setShowSymbol(menuItem.isChecked());

        } else if (id == R.id.action_edit_word_wrap) {
            pascalPreferences.setWordWrap(menuItem.isChecked());

        } else if (id == R.id.action_got_to_blog) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://pascalnide.wordpress.com/"));
            activity.startActivity(intent);

        } else if (id == R.id.action_open_file) {
            activity.openDrawer(GravityCompat.START);

        } else if (id == R.id.action_insert_media_url) {
            Intent i = new Intent();
            i.setType("audio/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(i, "Complete action using"),
                    EditorActivity.ACTION_PICK_MEDIA_URL);

        } else if (id == R.id.action_create_shortcut) {
            try {
                Intent intent = new Intent(activity,
                        Class.forName("com.duy.pascal.pro.activities.CreateShortcutActivity"));
                activity.startActivityForResult(intent, EditorActivity.ACTION_CREATE_SHORTCUT);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.action_github) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/tranleduy2000/pascalnide"));
            activity.startActivity(intent);
        } else if (id == R.id.action_insert_color) {
            activity.insertColor();

        } else if (id == R.id.action_ime) {
            pascalPreferences.setImeMode(menuItem.isChecked());

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
            if (menu.findItem(action_auto_save).isChecked()) {
                return true;
            }
        }
        return false;
    }

}
