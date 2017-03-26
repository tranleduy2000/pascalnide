package com.duy.pascal.frontend.view.screen.utils;

import android.content.Context;
import android.text.ClipboardManager;

@SuppressWarnings("deprecation")
public class ClipboardManagerCompatV1 implements ClipboardManagerCompat {
    private final ClipboardManager clip;

    public ClipboardManagerCompatV1(Context context) {
        clip = (ClipboardManager) context.getApplicationContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public CharSequence getText() {
        return clip.getText();
    }

    @Override
    public void setText(CharSequence text) {
        clip.setText(text);
    }

    @Override
    public boolean hasText() {
        return clip.hasText();
    }
}
