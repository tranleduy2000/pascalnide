package com.duy.pascal.compiler.editor;

public interface EditorListener {
    void init();

    void show();

    void hide();

    String getString();

    void updateTextFinger();

    boolean isTextChanged();
}
