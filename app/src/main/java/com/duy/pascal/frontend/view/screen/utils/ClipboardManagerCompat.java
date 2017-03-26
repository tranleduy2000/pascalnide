package com.duy.pascal.frontend.view.screen.utils;

public interface ClipboardManagerCompat {
    CharSequence getText();

    void setText(CharSequence text);

    boolean hasText();
}
