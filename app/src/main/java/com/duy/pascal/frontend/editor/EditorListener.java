package com.duy.pascal.frontend.editor;

public interface EditorListener {
    void init();

    void show();

    void hide();

    String getString();

    void updateTextFinger();

    boolean isTextChanged();
}
