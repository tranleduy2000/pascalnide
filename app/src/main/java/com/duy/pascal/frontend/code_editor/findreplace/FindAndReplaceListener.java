package com.duy.pascal.frontend.code_editor.findreplace;

public interface FindAndReplaceListener {
    void onFindAndReplace(String from, String to, boolean regex, boolean matchCase);

    void onFind(String find, boolean regex, boolean wordOnly, boolean matchCase);
}
