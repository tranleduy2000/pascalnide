package com.duy.pascal.frontend.keyboard;

import android.view.KeyEvent;

import static android.view.KeyEvent.KEYCODE_B;
import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_CTRL_LEFT;
import static android.view.KeyEvent.KEYCODE_CTRL_RIGHT;
import static android.view.KeyEvent.KEYCODE_F;
import static android.view.KeyEvent.KEYCODE_G;
import static android.view.KeyEvent.KEYCODE_H;
import static android.view.KeyEvent.KEYCODE_L;
import static android.view.KeyEvent.KEYCODE_O;
import static android.view.KeyEvent.KEYCODE_R;
import static android.view.KeyEvent.KEYCODE_S;
import static android.view.KeyEvent.KEYCODE_V;
import static android.view.KeyEvent.KEYCODE_X;
import static android.view.KeyEvent.KEYCODE_Y;
import static android.view.KeyEvent.KEYCODE_Z;
import static com.duy.pascal.frontend.EditorControl.*;
import static com.duy.pascal.frontend.EditorControl.ACTION_CUT;
import static com.duy.pascal.frontend.EditorControl.ACTION_PASTE;
import static com.duy.pascal.frontend.EditorControl.ACTION_SELECT_ALL;

/**
 * An ASCII key listener. Supports control characters and escape. Keeps track of
 * the current state of the alt, shift, and control keys.
 */
public class KeyListener {


    private ModifierKey mControlKey = new ModifierKey();

    public void handleControlKey(boolean down) {
        if (down) {
            mControlKey.onPress();
        } else {
            mControlKey.onRelease();
        }
    }

    /**
     * CTRL + C copy z
     * CTRL + V paste z
     * CTRL + B: compile z
     * CTRL + R run z
     * CTRL + X cut z
     * CTRL + Z undo z
     * CTRL + Y redo z
     * CTRL + Q quit
     * CTRL + S save z
     * CTRL + O open z
     * CTRL + F find z
     * CTRL + H find and replace z
     * CTRL + L format code z
     * CTRL + G: goto line z
     */
    public int keyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case -99:
            case KEYCODE_CTRL_LEFT:
            case KEYCODE_CTRL_RIGHT:
                mControlKey.onPress();
                return -10;
            case KeyEvent.KEYCODE_A:
                return ACTION_SELECT_ALL;
            case KEYCODE_C:
                return ACTION_COPY;
            case KEYCODE_X:
                return ACTION_CUT;
            case KEYCODE_V:
                return ACTION_PASTE;
            case KEYCODE_B:
                return ACTION_COMPILE;
            case KEYCODE_R:
                return ACTION_RUN;
            case KEYCODE_Z:
                return ACTION_UNDO;
            case KEYCODE_Y:
                return ACTION_REDO;
            case KEYCODE_S:
                return ACTION_SAVE;
            case KEYCODE_O:
                return ACTION_OPEN;
            case KEYCODE_H:
                return ACTION_FIND_AND_REPLACE;
            case KEYCODE_G:
                return ACTION_GOTO_LINE;
            case KEYCODE_L:
                return ACTION_FORMAT_CODE;
            case KEYCODE_F:
                return ACTION_FIND;

            default:
                return mControlKey.isActive() ? 0 : -1;
        }
//        editText.getEditableText().insert(editText.getSelectionStart(), Character.toString((char) result));
    }

    /**
     * Handle a keyUp event.
     *
     * @param keyCode the keyCode of the keyUp event
     */
    public void keyUp(int keyCode) {
        switch (keyCode) {
            case -99:
            case KEYCODE_CTRL_LEFT:
            case KEYCODE_CTRL_RIGHT:
                mControlKey.onRelease();
                break;
            default:
                // Ignore other keyUps
                break;
        }
    }

}
