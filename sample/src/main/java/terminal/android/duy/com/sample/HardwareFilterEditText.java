package terminal.android.duy.com.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.io.IOException;

/**
 * Created by Duy on 26-Mar-17.
 */

public class HardwareFilterEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = HardwareFilterEditText.class.getSimpleName();
    private final boolean LOG_KEY_EVENTS = true;
    private TermSettings mSettings;
    private SharedPreferences mPrefs;
    private TermKeyFileListener mKeyListener;
    private boolean mUseCookedIme;
    private String mImeBuffer = "";

    public HardwareFilterEditText(Context context) {
        super(context);
        init();
    }

    public HardwareFilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HardwareFilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSettings = new TermSettings(mPrefs);
        mKeyListener = new TermKeyFileListener();
        updatePrefs(mSettings);

    }

    public void updatePrefs(TermSettings settings) {
        mSettings = settings;
        setUseCookedIME(mSettings.useCookedIME());
    }

    public void setUseCookedIME(boolean useCookedIME) {
        mUseCookedIme = useCookedIME;
    }

    private void setImeBuffer(String buffer) {
        if (!buffer.equals(mImeBuffer)) {
            invalidate();
        }
        mImeBuffer = buffer;
    }

    public boolean getKeypadApplicationMode() {
        return true;
    }

    private KeyEvent handleKeyCodeMapper(int zAction, int zKeyCode) {
        //Check with HardKey Mappings..!
        KeyEvent newEvent = new KeyEvent(zAction, zKeyCode);

        if (TermService.isHardKeyEnabled()) {
            int hardmap = TermService.isSpecialKeyCode(zKeyCode);

            //Valid.. ?
            if (hardmap != -1) {
                //Its a special key code..
                if (hardmap == hardkeymappings.HARDKEY_CTRL_LEFT || hardmap == hardkeymappings.HARDKEY_CTRL_RIGHT) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_CTRL_LEFT);

                } else if (hardmap == hardkeymappings.HARDKEY_ALT_LEFT || hardmap == hardkeymappings.HARDKEY_ALT_RIGHT) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_ALT_LEFT);

                } else if (hardmap == hardkeymappings.HARDKEY_ESCAPE) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_ESCAPE);

                } else if (hardmap == hardkeymappings.HARDKEY_FUNCTION) {
                    //Just Update the Function Key Settings
                    mKeyListener.handleFunctionKey(false);
                    return null;

                } else if (hardmap == hardkeymappings.HARDKEY_TAB) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_TAB);

                } else if (hardmap == hardkeymappings.HARDKEY_LSHIFT || hardmap == hardkeymappings.HARDKEY_RSHIFT) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_SHIFT_LEFT);

                } else if (hardmap == hardkeymappings.HARDKEY_SPACE) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_SPACE);

                } else if (hardmap == hardkeymappings.HARDKEY_ENTER) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_ENTER);

                } else if (hardmap == hardkeymappings.HARDKEY_DELETE) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_FORWARD_DEL);

                } else if (hardmap == hardkeymappings.HARDKEY_BACKSPACE) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_DEL);

                } else if (hardmap == hardkeymappings.HARDKEY_UP) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_DPAD_UP);

                } else if (hardmap == hardkeymappings.HARDKEY_DOWN) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_DPAD_DOWN);

                } else if (hardmap == hardkeymappings.HARDKEY_LEFT) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_DPAD_LEFT);

                } else if (hardmap == hardkeymappings.HARDKEY_RIGHT) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_DPAD_RIGHT);

                } else if (hardmap == hardkeymappings.HARDKEY_PGUP) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_PAGE_UP);

                } else if (hardmap == hardkeymappings.HARDKEY_PGDOWN) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_PAGE_DOWN);

                } else if (hardmap == hardkeymappings.HARDKEY_HOME) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_MOVE_HOME);

                } else if (hardmap == hardkeymappings.HARDKEY_END) {
                    newEvent = new KeyEvent(zAction, TermKeyFileListener.KEYCODE_MOVE_END);
                }
            }
        }

        return newEvent;
    }

    private boolean handleControlKey(int keyCode, boolean down) {
        if (keyCode == mSettings.getControlKeyCode()) {
            Log.w(TAG, "handler control key: ");
            mKeyListener.handleControlKey(down);
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int zKeyCode, KeyEvent event) {
        if (TermDebug.LOG_IME) {
            Log.w(TAG, "onKeyDown: " + zKeyCode + " " + event);
        }

        //Check with HardKey Mappings..!
        KeyEvent newEvent = handleKeyCodeMapper(event.getAction(), zKeyCode);
        if (newEvent == null) {
            //Function press..
            return true;
        }

        //The new Key Code
        int keyCode = newEvent.getKeyCode();

        if (handleControlKey(keyCode, true)) {
            return true;
        } else if (handleFnKey(keyCode, true)) {
            //Send the escape key
            try {
                mKeyListener.keyDown(TermKeyFileListener.KEYCODE_ESCAPE, newEvent, HardwareFilterEditText.this, getKeypadApplicationMode());
            } catch (IOException iOException) {
            }
            return true;
        } else if (isSystemKey(keyCode, newEvent) && keyCode != 122 && keyCode != 123 && keyCode != 92 && keyCode != 93) {
            // Don't intercept the system keys And the HOME /END / PGUP / PGDOWN KEYS
            return super.onKeyDown(keyCode, newEvent);
        }

        try {
            mKeyListener.keyDown(keyCode, newEvent, HardwareFilterEditText.this, getKeypadApplicationMode());
        } catch (IOException e) {
            // Ignore I/O exceptions
        }

        return true;
    }

    @Override
    public boolean onKeyUp(int zKeyCode, KeyEvent event) {
        if (LOG_KEY_EVENTS) {
            Log.w(TAG, "onKeyUp " + zKeyCode);
        }
        //Check with HardKey Mappings..!
        KeyEvent newEvent = handleKeyCodeMapper(event.getAction(), zKeyCode);
        if (newEvent == null) {
            //Function press..
            return true;
        }
        //The new Key Code
        int keyCode = newEvent.getKeyCode();
        if (handleControlKey(keyCode, false)) {
            return true;
        } else if (handleFnKey(keyCode, false)) {
            mKeyListener.keyUp(TermKeyFileListener.KEYCODE_TAB);
            return true;
        } else if (isSystemKey(keyCode, newEvent)) {
            // Don't intercept the system keys
            return super.onKeyUp(keyCode, newEvent);
        }
        mKeyListener.keyUp(keyCode);
        return true;
    }

    private boolean handleFnKey(int keyCode, boolean down) {
        if (keyCode == mSettings.getFnKeyCode()) {
            if (LOG_KEY_EVENTS) {
                Log.w(TAG, "handleFnKey " + keyCode);
            }
            return true;
        }
        return false;
    }

    private boolean handleFunctionKey(boolean down) {
        if (LOG_KEY_EVENTS) {
            Log.w(TAG, "handleFunctionKey ");
        }
        mKeyListener.handleFunctionKey(down);
        return true;
    }

    private boolean isSystemKey(int keyCode, KeyEvent event) {
        return event.isSystem();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new BaseInputConnection(this, false) {
            @Override
            public boolean sendKeyEvent(KeyEvent event) {
                return super.sendKeyEvent(event);
            }

           /* private void sendText(CharSequence text) {
                int n = text.length();
                try {
                    for (int i = 0; i < n; i++) {
                        char c = text.charAt(i);
                        mapAndSend(c);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "error writing ", e);
                }
            }

            private void mapAndSend(int c) throws IOException {
                Log.d(TAG, "mapAndSend: ");
                int result = mKeyListener.mapControlChar(c);
                if (result < TermKeyFileListener.KEYCODE_OFFSET) {
                    getEditableText().insert(getSelectionStart(), Character.toString((char) result));
                } else {
                    mKeyListener.handleKeyCode(result - TermKeyFileListener.KEYCODE_OFFSET, HardwareFilterEditText.this, getKeypadApplicationMode());
                }
            }


            public boolean performEditorAction(int actionCode) {
                if (TermDebug.LOG_IME) {
                    Log.w(TAG, "performEditorAction(" + actionCode + ")");
                }
                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    // The "return" key has been pressed on the IME.
                    sendText("\n");
                }
                return true;
            }*/

        };
    }

}
