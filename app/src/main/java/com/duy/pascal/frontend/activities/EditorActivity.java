/*
 *  Copyright 2017 Tran Le Duy
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
import com.duy.pascal.backend.lib.PascalLibraryManager;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokenizer.AutoIndentCode;
import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.MenuEditor;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.dialog.DialogCreateNewFile;
import com.duy.pascal.frontend.dialog.DialogFragmentErrorMsg;
import com.duy.pascal.frontend.program_structure.DialogProgramStructure;
import com.duy.pascal.frontend.program_structure.viewholder.StructureItem;
import com.duy.pascal.frontend.program_structure.viewholder.StructureType;
import com.duy.pascal.frontend.sample.DocumentActivity;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.utils.LineUtils;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManager;
import com.duy.pascal.frontend.view.LockableScrollView;
import com.duy.pascal.frontend.view.code_view.CodeView;
import com.duy.pascal.frontend.view.code_view.HighlightEditor;
import com.duy.pascal.frontend.view.code_view.SuggestItem;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.ast.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;

public class EditorActivity extends BaseEditorActivity implements
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuEditor.onOptionsItemSelected(item);
    }

    public void initContent() {
        mCodeEditor.setEditorControl(this);
        mCodeEditor.setVerticalScroll(mScrollView);
        mScrollView.setScrollListener(new LockableScrollView.ScrollListener() {
            @Override
            public void onScroll(int x, int y) {
                mCodeEditor.updateHighlightWithDelay(HighlightEditor.SHORT_DELAY);
            }
        });
        mCodeEditor.setSuggestData(PascalLibraryManager.getAllMethod(SystemLib.class,
                IOLib.class, FileLib.class));
    }

    @OnClick(R.id.img_tab)
    void insertTab(View v) {
        onKeyClick(v, "\t");
    }

    @Override
    public void onKeyClick(View view, String text) {
        mCodeEditor.insert(text);
    }

    @Override
    public void onKeyLongClick(String text) {
        mCodeEditor.insert(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return menuEditor.onCreateOptionsMenu(menu);
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
                mCodeEditor.replaceAll(
                        editFind.getText().toString(),
                        editReplace.getText().toString(),
                        ckbRegex.isChecked(),
                        ckbMatch.isChecked());
                mCodeEditor.refresh();
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
                mCodeEditor.find(editFind.getText().toString(),
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
                mCodeEditor.setLineError(lineInfo);
                mCodeEditor.refresh();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.smoothScrollTo(0, LineUtils.getYAtLine(mScrollView,
                                mCodeEditor.getLineCount(), e.line.line));
                    }
                }, 100);
            }
        }
    }

    public String getCode() {
        return mCodeEditor.getCleanText();
    }

    /**
     * set text code to {@link CodeView}
     *
     * @param code
     */
    public void setCode(String code) {
        mCodeEditor.setText(code);
        mCodeEditor.clearHistory();
        mCodeEditor.refresh();
    }

    /**
     * compile code, if is error, show dialog error
     * invalidate keyword
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
            ExpressionContextMixin program = pascalProgram.getProgram();
            ArrayList<SuggestItem> data = new ArrayList<>();
            data.addAll(program.getListNameConstants());
            data.addAll(program.getListNameFunctions());
            data.addAll(program.getListNameTypes());
            ArrayList<VariableDeclaration> variables = program.getVariables();
            ArrayList<SuggestItem> listVariables = new ArrayList<>();
            for (VariableDeclaration variableDeclaration : variables) {
                listVariables.add(new SuggestItem(StructureType.TYPE_VARIABLE, variableDeclaration.name()));
            }
            data.addAll(listVariables);
            mCodeEditor.setSuggestData(data);
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
        Dlog.e(e);
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
        if (mPascalPreferences.isShowListSymbol()) {
            mKeyList.setListener(this);
            mContainerSymbol.setVisibility(View.VISIBLE);
        } else {
            mContainerSymbol.setVisibility(View.GONE);
        }
        mCodeEditor.updateFromSettings();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.key_show_suggest_popup))
                || s.equals(getString(R.string.key_show_line_number))
                || s.equals(getString(R.string.show_suggest_popup))
                || s.equals(getString(R.string.key_pref_word_wrap))) {
            mCodeEditor.updateFromSettings();
        } else if (s.equals(getString(R.string.key_show_symbol))) {
            mContainerSymbol.setVisibility(mPascalPreferences.isShowListSymbol() ? View.VISIBLE : View.GONE);
        } else {
            super.onSharedPreferenceChanged(sharedPreferences, s);
        }
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
        addNewTab(file, SELECT, SAVE_LAST_FILE);

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
                addNewTab(file, SELECT, SAVE_LAST_FILE);
                mCodeEditor.setText(CodeSample.MAIN);
                mCodeEditor.refresh();
                //select before update
                mCodeEditor.selectAll();
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
        edittext.setMaxEms(5);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.goto_line)
                .setView(edittext)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String line = edittext.getText().toString();
                        if (!line.isEmpty()) {
                            // TODO: 03-Apr-17
                            mCodeEditor.goToLine(Integer.parseInt(line));
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
        AutoIndentCode autoIndentCode = new AutoIndentCode();
        String result = autoIndentCode.format(text);
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
                content += "\n ====================== \n" + mCodeEditor.getCleanText();
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
        if (mCodeEditor.canUndo()) mCodeEditor.undo();
        else Toast.makeText(this, R.string.cant_undo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void redo() {
        if (mCodeEditor.canRedo()) mCodeEditor.redo();
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
        hideKeyboard(mCodeEditor);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void paste() {
        String text = ClipboardManager.getClipboard(this);
        mCodeEditor.paste();
    }

    @Override
    public void copyAll() {
        String text = mCodeEditor.getCleanText();
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
            if (mCodeEditor.canUndo()) {
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

    public void showProgramStructure() {
        saveFile();

        try {
            PascalProgram pascalProgram = new PascalCompiler(null)
                    .loadPascal(mFilePath,
                            new FileReader(mFilePath),
                            new ArrayList<ScriptSource>(), new ArrayList<ScriptSource>(), null);

            if (pascalProgram.main == null) {
                showErrorDialog(new MainProgramNotFoundException());
            }
            ExpressionContextMixin program = pascalProgram.getProgram();

            StructureItem node = getNode(program, pascalProgram.getProgramName(), StructureType.TYPE_PROGRAM, 0);

            DialogProgramStructure dialog = DialogProgramStructure.newInstance(node);
            dialog.show(getSupportFragmentManager(), DialogProgramStructure.TAG);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private StructureItem getNode(ExpressionContextMixin context, String nameOfNode, int type, int depth) {
        StructureItem node = new StructureItem(type, nameOfNode);
        String tab = "";
        for (int i = 0; i < depth; i++) tab += "\t";
        Map<String, ConstantDefinition> constants = context.getConstants();
        ArrayList<SuggestItem> listNameConstants = context.getListNameConstants();
        for (SuggestItem name : listNameConstants) {
            node.addNode(new StructureItem(StructureType.TYPE_CONST,
                    name + " = " + constants.get(name.getName().toLowerCase()).getValue()));
        }

        ArrayList<String> libraries = context.getLibrarieNames();
        for (String name : libraries) {
            Log.d(TAG, tab + "showProgramStructure: library " + name);
            node.addNode(new StructureItem(StructureType.TYPE_LIBRARY, name));
        }

        List<VariableDeclaration> variables = context.getVariables();
        for (VariableDeclaration variableDeclaration : variables) {
            Log.d(TAG, tab + "showProgramStructure: var " + variableDeclaration.getName() + " = "
                    + variableDeclaration.getInitialValue() + " " + variableDeclaration.getType());
            node.addNode(new StructureItem(StructureType.TYPE_VARIABLE,
                    variableDeclaration.getName() + ": " + variableDeclaration.getType()));
        }

        ListMultimap<String, AbstractFunction> callableFunctions = context.getCallableFunctions();
        ArrayList<SuggestItem> listNameFunctions = context.getListNameFunctions();
        for (SuggestItem name : listNameFunctions) {
            List<AbstractFunction> abstractFunctions = callableFunctions.get(name.getName().toLowerCase());
            for (AbstractFunction function : abstractFunctions) {
                if (function instanceof FunctionDeclaration) {
                    FunctionDeclaration functionInPascal = (FunctionDeclaration) function;
                    StructureItem child = getNode(
                            functionInPascal.declarations,
                            ((FunctionDeclaration) function).name,
                            functionInPascal.isProcedure() ? StructureType.TYPE_PROCEDURE : StructureType.TYPE_FUNCTION,
                            depth + 1);
                    node.addNode(child);
                }
            }
        }
        return node;
    }

    public void startDebug() {
        if (doCompile()) mCompileManager.debug(mFilePath);
    }
}