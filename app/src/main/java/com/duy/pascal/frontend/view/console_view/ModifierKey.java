package com.duy.pascal.frontend.view.console_view;

/**
 * Created by Duy on 26-Mar-17.
 */
public class ModifierKey {

    private static final int UNPRESSED = 0;
    private static final int PRESSED = 1;
    private static final int RELEASED = 2;
    private static final int USED = 3;
    private static final int LOCKED = 4;
    private int mState;

    /**
     * Construct a modifier key. UNPRESSED by default.
     */
    public ModifierKey() {
        mState = UNPRESSED;
    }

    public void onPress() {
        switch (mState) {
            case PRESSED:
                // This is a repeat before use
                break;
            case RELEASED:
                mState = LOCKED;
                break;
            case USED:
                // This is a repeat after use
                break;
            case LOCKED:
                mState = UNPRESSED;
                break;
            default:
                mState = PRESSED;
                break;
        }
    }

    public void onRelease() {
        switch (mState) {
            case USED:
                mState = UNPRESSED;
                break;
            case PRESSED:
                mState = RELEASED;
                break;
            default:
                // Leave state alone
                break;
        }
    }

    public void adjustAfterKeypress() {
        switch (mState) {
            case PRESSED:
                mState = USED;
                break;
            case RELEASED:
                mState = UNPRESSED;
                break;
            default:
                // Leave state alone
                break;
        }
    }

    public boolean isActive() {
        return mState != UNPRESSED;
    }

    public int getUIMode() {
        switch (mState) {
            default:
            case UNPRESSED:
                return TextRenderer.MODE_OFF;
            case PRESSED:
            case RELEASED:
            case USED:
                return TextRenderer.MODE_ON;
            case LOCKED:
                return TextRenderer.MODE_LOCKED;
        }
    }
}
