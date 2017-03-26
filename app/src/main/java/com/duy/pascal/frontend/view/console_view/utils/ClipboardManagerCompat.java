package com.duy.pascal.frontend.view.console_view.utils;

public interface ClipboardManagerCompat {
	CharSequence getText();

	boolean hasText();

    void setText(CharSequence text);
}
