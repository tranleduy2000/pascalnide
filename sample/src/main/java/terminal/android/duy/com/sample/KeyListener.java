package terminal.android.duy.com.sample;

import android.view.KeyEvent;

import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_CTRL_LEFT;
import static android.view.KeyEvent.KEYCODE_CTRL_RIGHT;
import static android.view.KeyEvent.KEYCODE_V;
import static android.view.KeyEvent.KEYCODE_X;

/**
 * An ASCII key listener. Supports control characters and escape. Keeps track of
 * the current state of the alt, shift, and control keys.
 */
class KeyListener {

    public static final int ACTION_COPY = 10011;
    public static final int ACTION_CUT = 10012;
    public static final int ACTION_PASTE = 10013;
    public static final int ACTION_SELECT_ALL = 10014;
    private ModifierKey mControlKey = new ModifierKey();

    public void handleControlKey(boolean down) {
        if (down) {
            mControlKey.onPress();
        } else {
            mControlKey.onRelease();
        }
    }

    public int keyDown(int keyCode, KeyEvent event) {
        int result = -1;
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
        }
        return mControlKey.isActive() ? 0 : -1;
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
