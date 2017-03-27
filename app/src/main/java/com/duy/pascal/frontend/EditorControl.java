package com.duy.pascal.frontend;

import android.view.View;

public interface EditorControl {

    int ACTION_COPY = 1;
    int ACTION_CUT = 2;
    int ACTION_PASTE = 3;
    int ACTION_SELECT_ALL = 4;
    int ACTION_RUN = 5;
    int ACTION_COMPILE = 6;
    int ACTION_SAVE = 7;
    int ACTION_SAVE_AS = 8;
    int ACTION_GOTO_LINE = 9;
    int ACTION_FORMAT_CODE = 10;
    int ACTION_UNDO = 11;
    int ACTION_REDO = 12;
    int ACTION_FIND_AND_REPLACE = 13;
    int ACTION_OPEN = 14;
    int ACTION_FIND = 15;

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
