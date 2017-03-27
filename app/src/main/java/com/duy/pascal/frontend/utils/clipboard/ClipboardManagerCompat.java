package com.duy.pascal.frontend.utils.clipboard;

public interface ClipboardManagerCompat {
    CharSequence getText();

    void setText(CharSequence text);

    boolean hasText();
}
