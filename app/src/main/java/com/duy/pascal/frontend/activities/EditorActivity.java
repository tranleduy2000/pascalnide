package com.duy.pascal.frontend.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.backend.core.PascalCompiler;
import com.duy.pascal.backend.exceptions.MainProgramNotFoundException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.MenuEditor;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.alogrithm.AutoIndentCode;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.dialog.DialogCreateNewFile;
import com.duy.pascal.frontend.dialog.DialogFragmentErrorMsg;
import com.duy.pascal.frontend.sample.DocumentActivity;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.utils.ClipboardManager;
import com.duy.pascal.frontend.utils.LineUtils;
import com.duy.pascal.frontend.view.LockableScrollView;
import com.duy.pascal.frontend.view.code_view.CodeView;
import com.duy.pascal.frontend.view.code_view.HighlightEditor;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class EditorActivity extends FileEditorActivity implements
        DrawerLayout.DrawerListener {

    private static final int FILE_SELECT_CODE = 1012;

    private CompileManager mCompileManager;
    private MenuEditor menuEditor;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompileManager = new CompileManager(this);
        mDrawerLayout.addDrawerListener(this);
        mKeyList.setListener(this);
        menuEditor = new MenuEditor(this, this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return menuEditor.onOptionsItemSelected(item);
            }
        });
        initContent();
    }

    public void initContent() {
        mCodeView.setVerticalScroll(mScrollView);
        mScrollView.setScrollListener(new LockableScrollView.ScrollListener() {
            @Override
            public void onScroll(int x, int y) {
                mCodeView.updateHighlightWithDelay(HighlightEditor.SHORT_DELAY);
            }
        });
       /* mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCodeView.hasFocus()) {
                    mCodeView.clearFocus();
                }
                return false;
            }
        });*/
        findViewById(R.id.img_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClick(v, "\t");
            }
        });

    }

    @Override
    public void onKeyClick(View view, String text) {
        Log.d(TAG, "onKeyClick: " + text);
        mCodeView.insert(text);
    }

    @Override
    public void onKeyLongClick(String text) {
        mCodeView.insert(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return menuEditor.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuEditor.onOptionsItemSelected(item);
    }

    /**
     * create dialog find and replace
     */
    @Override
    public void findAndReplace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setView(R.layout.dialog_find_and_replace);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final CheckBox ckbRegex = (CheckBox) alertDialog.findViewById(R.id.ckb_regex);
        final CheckBox ckbMatch = (CheckBox) alertDialog.findViewById(R.id.ckb_match_key);
        final EditText editFind = (EditText) alertDialog.findViewById(R.id.txt_find);
        final EditText editReplace = (EditText) alertDialog.findViewById(R.id.edit_replace);
        assert editFind != null;
        editFind.setText(mPascalPreferences.getString(PascalPreferences.LAST_FIND));
        alertDialog.findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 01-Mar-17 replace
                assert ckbRegex != null;
                assert editReplace != null;
                assert ckbMatch != null;
                mCodeView.replaceAll(
                        editFind.getText().toString(),
                        editReplace.getText().toString(),
                        ckbRegex.isChecked(),
                        ckbMatch.isChecked());
                mCodeView.refresh();
                mPascalPreferences.put(PascalPreferences.LAST_FIND, editFind.getText().toString());
                alertDialog.dismiss();
            }
        });
        alertDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void runProgram() {
        if (doCompile()) mCompileManager.execute(mFilePath);
    }

    @Override
    public boolean isAutoSave() {
        return menuEditor.getChecked(R.id.action_auto_save);
    }

    /**
     * replace dialog find
     */
    public void showDialogFind() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
        builder.setView(R.layout.find_dialog);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final CheckBox ckbRegex = (CheckBox) alertDialog.findViewById(R.id.ckb_regex);
        final CheckBox ckbMatch = (CheckBox) alertDialog.findViewById(R.id.ckb_match_key);
        final CheckBox ckbWordOnly = (CheckBox) alertDialog.findViewById(R.id.ckb_word_only);
        final EditText editFind = (EditText) alertDialog.findViewById(R.id.txt_find);
        assert editFind != null;
        editFind.setText(mPascalPreferences.getString(PascalPreferences.LAST_FIND));
        alertDialog.findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCodeView.find(editFind.getText().toString(),
                        ckbRegex.isChecked(),
                        ckbWordOnly.isChecked(),
                        ckbMatch.isChecked());
                mPascalPreferences.put(PascalPreferences.LAST_FIND, editFind.getText().toString());
                alertDialog.dismiss();
            }
        });
        alertDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void saveAs() {
        final AppCompatEditText edittext = new AppCompatEditText(this);
        edittext.setHint(R.string.enter_new_file_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_as)
                .setView(edittext)
                .setIcon(R.drawable.ic_create_new_folder_white_24dp)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fileName = edittext.getText().toString();
                        dialog.cancel();
                        mFileManager.saveFile(mFileManager.createNewFileInMode(fileName),
                                mCodeView.getCleanText());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    public void saveFile() {
        boolean result = mFileManager.saveFile(mFilePath, getCode());
        if (result) Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, R.string.can_not_save_file, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDocumentActivity() {
        Intent intent = new Intent(this, DocumentActivity.class);
        startActivity(intent);
    }

    private void showLineError(final ParsingException e) {
        if (e != null) {
            if (e.line != null) {
                LineInfo lineInfo = e.line;
                mCodeView.setLineError(lineInfo.line);
                mCodeView.refresh();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.smoothScrollTo(0, LineUtils.getYAtLine(mScrollView,
                                mCodeView.getLineCount(), e.line.line));
                    }
                }, 100);
            }
        }
    }

    public String getCode() {
        return mCodeView.getCleanText();
    }

    /**
     * set text code to {@link CodeView}
     *
     * @param code
     */
    public void setCode(String code) {
//        code = CodeManager.localCode(code);
        mCodeView.setText(code);
        mCodeView.clearHistory();
        mCodeView.refresh();
    }

    /**
     * compile code, if is error, show dialog error
     */
    @Override
    public boolean doCompile() {
        mFileManager.saveFile(mFilePath, getCode());
        try {
            PascalProgram pascalProgram = new PascalCompiler(null)
                    .loadPascal(mFilePath,
                            new FileReader(mFilePath),
                            new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>(), null);

            if (pascalProgram.main == null) {
                showErrorDialog(new MainProgramNotFoundException());
                return false;
            }
        } catch (FileNotFoundException e) {
            showErrorDialog(e);
            return false;
        } catch (ParsingException e) {
            showErrorDialog(e);
            showLineError(e);
            return false;
        } catch (Exception e) {
            showErrorDialog(e);
            return false;
        }
        Toast.makeText(this, R.string.compile_ok, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void showErrorDialog(Exception e) {
        ExceptionManager exceptionManager = new ExceptionManager(this);
        DialogFragmentErrorMsg dialogFragmentErrorMsg = DialogFragmentErrorMsg
                .newInstance(exceptionManager.getMessage(e), "");
        dialogFragmentErrorMsg.show(getSupportFragmentManager(), DialogFragmentErrorMsg.TAG);

    }

    /**
     * load file and set text to editor
     *
     * @param filePath - filePath of file, do not include path
     */
    protected void loadFile(final String filePath) {
        try {
            File file = new File(filePath);
            String txt = mFileManager.readFileAsString(file);
            setCode(txt);
            mFilePath = filePath;
            mPascalPreferences.put(PascalPreferences.FILE_PATH, filePath);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeView.updateFromSettings();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        super.onSharedPreferenceChanged(sharedPreferences, s);
    }

    /**
     * save current project
     */
    @Override
    protected void onPause() {
        super.onPause();
        mFileManager.saveFile(mFilePath, getCode());
        mPascalPreferences.put(PascalPreferences.FILE_PATH, mFilePath);
    }

    @Override
    public void onFileClick(File file) {
        //save current file
        addNewFile(file, true);

        //close drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onFileLongClick(File file) {
        showFileInfo(file);
    }


    /**
     * show dialog with file info
     * filePath, path, size, extension ...
     *
     * @param file - file to show info
     */
    private void showFileInfo(File file) {
        String extension = "";
        int ind = file.getPath().lastIndexOf('.');
        if (ind > 0) {
            extension = file.getPath().substring(ind + 1);// this is the extension
        }
        String info = "";
        info += getString(R.string.path) + " " + file.getPath() + "\n" +
                getString(R.string.extension) + " " + extension + "\n" +
                getString(R.string.readable) + " " + file.canRead() + "\n" +
                getString(R.string.writeable) + " " + file.canWrite();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(file.getName());
        builder.setView(R.layout.dialog_view_file);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView txtInfo = (TextView) dialog.findViewById(R.id.txt_info);
        assert txtInfo != null;
        txtInfo.setText(info);
        CodeView codeView = (CodeView) dialog.findViewById(R.id.code_view);
        assert codeView != null;
        codeView.setTextHighlighted(mFileManager.readFileAsString(file));
        codeView.setFlingToScroll(false);
    }

    /**
     * creat new source file
     *
     * @param view
     */
    @Override
    public void createNewSourceFile(View view) {

        DialogCreateNewFile dialogCreateNewFile = DialogCreateNewFile.getInstance();
        dialogCreateNewFile.show(getSupportFragmentManager(), DialogCreateNewFile.TAG);
        dialogCreateNewFile.setListener(new DialogCreateNewFile.OnCreateNewFileListener() {
            @Override
            public void onFileCreated(File file) {
                saveFile();
                //add to view
                addNewFile(file, true);
                mCodeView.setText(CodeSample.MAIN);
                mCodeView.refresh();
                //select before update
                mCodeView.selectAll();
                mDrawerLayout.closeDrawers();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void goToLine() {
        final AppCompatEditText edittext = new AppCompatEditText(this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.goto_line)
                .setView(edittext)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String line = edittext.getText().toString();
                        if (line.length() > 5) {
//                            mCodeView.goToLine(1);
                        } else if (!line.isEmpty()) {
                            // TODO: 03-Apr-17
                            mCodeView.goToLine(Integer.parseInt(line));
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    public void formatCode() {
        String text = getCode();
        String result = AutoIndentCode.format(text);
        setCode(result);
    }

    @Override
    public void checkUpdate() {
        rateApp();
    }

    @Override
    public void reportBug() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.report_bug).setView(R.layout.report_bug_dialog).setIcon(R.drawable.ic_bug_report_white_24dp);
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
                i.putExtra(Intent.EXTRA_SUBJECT, "Report bug: " + editTitle.getText().toString());
                assert editContent != null;
                String content = "Cause: \n" + editContent.getText().toString();
                content += "\n ====================== \n" + mCodeView.getCleanText();
                i.putExtra(Intent.EXTRA_TEXT, content);
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.send_mail)));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(EditorActivity.this, R.string.no_mail_clients, Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void openTool() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void undo() {
        if (mCodeView.canUndo()) mCodeView.undo();
        else Toast.makeText(this, R.string.cant_undo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redo() {
        if (mCodeView.canRedo()) mCodeView.redo();
        else Toast.makeText(this, R.string.cant_redo, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                // Get the path
                String path;
                try {
                    path = mFileManager.getPath(this, uri);
                    mFileManager.setWorkingFilePath(path);
                    loadFile(path);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void openFileView(View view) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        hideKeyboard(mCodeView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void paste() {
        String text = ClipboardManager.getClipboard(this);
        mCodeView.insert(text);
    }

    @Override
    public void copyAll() {
        String text = mCodeView.getCleanText();
        ClipboardManager.setClipboard(this, text);
    }

    @Override
    public void selectTheme() {
        startActivity(new Intent(this, SelectThemeActivity.class));
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)
                || mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawers();
            return;
        }

        /**
         * check can undo
         */
        if (mPascalPreferences.getBoolean(getString(R.string.key_back_undo))) {
            if (mCodeView.canUndo()) {
                undo();
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit)
                .setMessage(R.string.exit_mgs)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditorActivity.super.onBackPressed();
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void openDrawer(int gravity) {
        mDrawerLayout.openDrawer(gravity);
    }

}