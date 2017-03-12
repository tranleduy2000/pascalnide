package com.duy.pascal.compiler.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.interpreter.core.PascalCompiler;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.pascal.compiler.CodeManager;
import com.duy.pascal.compiler.CompileManager;
import com.duy.pascal.compiler.ExceptionManager;
import com.duy.pascal.compiler.MenuEditor;
import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.adapters.FileAdapter;
import com.duy.pascal.compiler.alogrithm.AutoIndentCode;
import com.duy.pascal.compiler.data.CodeSample;
import com.duy.pascal.compiler.data.FileManager;
import com.duy.pascal.compiler.data.Preferences;
import com.duy.pascal.compiler.utils.ClipboardManager;
import com.duy.pascal.compiler.view.HighlightEditor;
import com.duy.pascal.compiler.view.LockableScrollView;
import com.duy.pascal.compiler.view.SymbolListView;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class EditorActivity extends BaseEditorActivity
        implements SymbolListView.OnKeyListener, FileAdapter.FileListener, DrawerLayout.DrawerListener,
        MenuEditor.EditorControl {
    private static final String TAG = EditorActivity.class.getSimpleName();
    private static final int FILE_SELECT_CODE = 1012;
    private static final int REQ_COMPILE = 1011;

    private CompileManager mCompileManager;
    private FileManager fileManager;
    private Handler handler = new Handler();
    //    private RunDo mUndoRedoSupport;
    private MenuEditor menuEditor;

    private static float getDistanceBetweenTouches(MotionEvent ev) {
        float xx = ev.getX(1) - ev.getX(0);
        float yy = ev.getY(1) - ev.getY(0);
        return (float) Math.sqrt(xx * xx + yy * yy);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompileManager = new CompileManager(this);
        fileManager = new FileManager(this);
        mDrawerLayout.addDrawerListener(this);
        mKeyList.setListener(this);
        mFilesView.setListener(this);
        menuEditor = new MenuEditor(this, this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return menuEditor.onOptionsItemSelected(item);
            }
        });
        initContent();
        undoRedoSupport();
        loadFile();
    }

    /**
     * load lasted file
     */
    private void loadFile() {
        mFileName = mPreferences.getString(Preferences.LAST_FILE);
        if (mFileName.isEmpty()) {
            mFileName = "new_file.pas";
        }
        loadFile(mFileName);
    }

    private void undoRedoSupport() {
//        mUndoRedoSupport = RunDo.Factory.getInstance(getFragmentManager());
    }

    public void initContent() {
        mScrollView.setScrollListener(new LockableScrollView.ScrollListener() {
            @Override
            public void onScroll(int x, int y) {
                mHighlightEditor.onMove(x, y);
            }
        });
    }


    @Override
    public void onKeyClick(String text) {
        mHighlightEditor.insert(text);
    }

    @Override
    public void onKeyLongClick(String text) {
        mHighlightEditor.insert(text);
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
        builder.setView(R.layout.find_and_replace_dialog);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final CheckBox ckbRegex = (CheckBox) alertDialog.findViewById(R.id.ckb_regex);
        final CheckBox ckbMatch = (CheckBox) alertDialog.findViewById(R.id.ckb_match_key);
        final EditText editFind = (EditText) alertDialog.findViewById(R.id.txt_find);
        final EditText editReplace = (EditText) alertDialog.findViewById(R.id.edit_replace);
        assert editFind != null;
        editFind.setText(mPreferences.getString(Preferences.LAST_FIND));
        alertDialog.findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 01-Mar-17 replace
                assert ckbRegex != null;
                assert editReplace != null;
                assert ckbMatch != null;
                mHighlightEditor.replaceAll(
                        editFind.getText().toString(),
                        editReplace.getText().toString(),
                        ckbRegex.isChecked(),
                        ckbMatch.isChecked());
                mPreferences.put(Preferences.LAST_FIND, editFind.getText().toString());
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
        if (doCompile())
            mCompileManager.execute(fileManager.getCurrentPath() + mFileName);
    }

    @Override
    public boolean autoSave() {
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
        editFind.setText(mPreferences.getString(Preferences.LAST_FIND));
        alertDialog.findViewById(R.id.btn_replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 01-Mar-17 replace
                mHighlightEditor.find(editFind.getText().toString(),
                        ckbRegex.isChecked(),
                        ckbWordOnly.isChecked(),
                        ckbMatch.isChecked());
                mPreferences.put(Preferences.LAST_FIND, editFind.getText().toString());
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
                        fileManager.saveInMode(fileManager.createNewFileInMode(fileName),
                                mHighlightEditor.getCleanText());
                        mFilesView.reload();
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
        boolean result = fileManager.saveInMode(mFileName, getCode());
        if (result) Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Can not save file!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDocumentActivity() {
        Intent intent = new Intent(this, DocumentActivity.class);
        startActivity(intent);
    }

    private void showLineError(ParsingException e) {
        LineInfo lineInfo = e.line;
        int row = lineInfo.line;
        int col = (lineInfo.column);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        String raw = getCode();

        //calc line
        int currentLine = 0, index = 0;
        for (char c : raw.toCharArray()) {
            if (c == '\n') currentLine++;
            if (currentLine == lineInfo.line) {
                break;
            }
            index++;
        }
        //space or end line
//        while (index < raw.length() &&
//                raw.charAt(index) == ' ' ||
//                raw.charAt(index) == '\n') index++;
        //index out of bound, decrease it
//        if (index >= raw.length()) index--;
        //set index error in exit text
//        if (index + lineInfo.column < raw.length()) {
//            mHighlightEditor.setSelection(index + lineInfo.column);
//        } else {
//            mHighlightEditor.setSelection(index);
//        }

        mHighlightEditor.setLineError(lineInfo.line + 1);
        Log.d(TAG, "showLineError: " + (row + 1) + " - " + col);
    }

    public String getCode() {
        String code = mHighlightEditor.getText().toString();
        return CodeManager.normalCode(code);
    }

    public void setCode(String code) {
        Log.d(TAG, "setCode: ");
        code = CodeManager.localCode(code);
        mHighlightEditor.setTextHighlighted(code);
    }

    /**
     * compile code, if is error, show dialog error
     */
    @Override
    public boolean doCompile() {
        fileManager.saveInMode(mFileName, getCode());
        try {
            new PascalCompiler(null).loadPascal(fileManager.getCurrentPath() + mFileName,
                    new FileReader(fileManager.getCurrentPath() + mFileName),
                    new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showErrorDialog(e);
            return false;
        } catch (ParsingException e) {
            e.printStackTrace();
            showErrorDialog(e);
            showLineError(e);
            return false;
        } catch (Exception e) {
            showErrorDialog(e);
        }
        Toast.makeText(this, R.string.compile_ok, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void showErrorDialog(Exception e) {
        ExceptionManager exceptionManager = new ExceptionManager(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(exceptionManager.getMessage(e))
                .setCancelable(false)
                .setTitle(R.string.compile_error)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create().show();
    }

    /**
     * load file and set text to editor
     *
     * @param fileName - fileName of file, do not include path
     */
    private void loadFile(final String fileName) {
        final String txt = fileManager.loadInMode(fileName);
        this.mFileName = fileName;
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(fileName);
            }
        });
        setCode(txt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHighlightEditor.updateFromSettings(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
            mFileName = intent.getStringExtra(CompileManager.FILE_PATH);
            loadFile(mFileName);
        }
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
        fileManager.saveInMode(mFileName, getCode());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(Preferences.LAST_FILE, mFileName).apply();
    }

    @Override
    public void onFileClick(File file) {
        //save current file
        fileManager.saveInMode(mFileName, getCode());
        //open new file
        loadFile(file.getName());
        //close drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onFileLongClick(File file) {
        showFileInfo(file);
    }

    @Override
    public boolean doRemoveFile(final File file) {
//        showDialogDeleteFile(file);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_file_msg) + file.getName());
        builder.setTitle(R.string.delete_file);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean success = fileManager.deleteFile(file);
                if (success)
                    Toast.makeText(EditorActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditorActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                mFilesView.reload();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
        return false;
    }

    /**
     * show dialog with file info
     * fileName, path, size, extension ...
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
        HighlightEditor codeView = (HighlightEditor) dialog.findViewById(R.id.code_view);
        assert codeView != null;
        codeView.setTextHighlighted(fileManager.readFileAsString(file));
        codeView.setFlingToScroll(false);
    }

    /**
     * creat new source file
     *
     * @param view
     */
    public void createNewSourceFile(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_file);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText editText = (EditText) alertDialog.findViewById(R.id.edit_file_name);
        Button btnOK = (Button) alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert btnOK != null;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }

                RadioButton checkBoxPas = (RadioButton) alertDialog.findViewById(R.id.rad_pas);
                RadioButton checkBoxInp = (RadioButton) alertDialog.findViewById(R.id.rad_inp);

                if (checkBoxInp.isChecked()) fileName += ".inp";
                else if (checkBoxPas.isChecked()) fileName += ".pas";

                //create new file
                fileName = fileManager.createNewFileInMode(fileName);
                //load to view
                loadFile(fileName);
                if (checkBoxPas.isChecked()) {
                    mHighlightEditor.setTextHighlighted(CodeSample.MAIN);
                    mHighlightEditor.setSelection(CodeSample.DEFAULT_POSITION);
                }
//                        mUndoRedoSupport.clearAllQueues();
                mFilesView.reload();
                mDrawerLayout.closeDrawers();
                alertDialog.cancel();
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
                        String lineNumber = edittext.getText().toString();
                        if (lineNumber.length() > 5) {
                            mHighlightEditor.goToLine(Integer.parseInt(lineNumber));
                        }
                        mHighlightEditor.goToLine(1);
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
        String text = mHighlightEditor.getText().toString();
        String result = AutoIndentCode.format(text);
        mHighlightEditor.setTextHighlighted(result);
    }

    @Override
    public void checkUpdate() {

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
                content += "\n ====================== \n" + mHighlightEditor.getCleanText();
                i.putExtra(Intent.EXTRA_TEXT, content);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(EditorActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
//        mUndoRedoSupport.undo();
    }

    @Override
    public void redo() {
//        mUndoRedoSupport.redo();
    }

    /**
     * open file from storage
     *
     * @param view
     */
    public void chooseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all files
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, R.string.install_file_manager, Toast.LENGTH_SHORT).show();
        }
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
                    path = fileManager.getPath(this, uri);
                    if (path != null) {
                        File file = new File(path);
//                        if (file.getName().endsWith(".txt") || file.getName().endsWith(".pas")
//                                || file.getName().endsWith(".inp") || file.getName().endsWith(".out")) {

                        //clone it to application path

                        if (!fileManager.createNewFileInMode(file.getName()).isEmpty()) {
                            fileManager.saveInMode(file.getName(),
                                    fileManager.readFileAsString(file.getPath()));
                            mFilesView.reload();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                            sharedPreferences.edit().putString(Preferences.LAST_FILE, file.getName()).apply();
                            loadFile(mFileName);
                        } else {
                            Toast.makeText(this, R.string.can_not_new_file, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, R.string.can_not_open_file, Toast.LENGTH_SHORT).show();
                    }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        hideKeyboard(mHighlightEditor);
//        fileManager.saveInMode(mFileName, getCode());
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void paste() {
        String text = ClipboardManager.getClipboard(this);
        mHighlightEditor.insert(text);
    }

    @Override
    public void copyAll() {
        String text = mHighlightEditor.getCleanText();
        ClipboardManager.setClipboard(this, text);
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

//    @Override
//    public EditText getEditTextForRunDo() {
//        return mHighlightEditor;
////        return null;
//    }
}