package com.duy.pascal.frontend;

import android.view.View;

public interface EditorControl {


    boolean doCompile();

    void saveAs();

    void findAndReplace();

    void runProgram();

    boolean isAutoSave();

    void saveFile();

    void showDocumentActivity();

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
