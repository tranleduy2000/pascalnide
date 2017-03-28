package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Selection;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.keyboard.KeyListener;
import com.duy.pascal.frontend.keyboard.KeySettings;
import com.duy.pascal.frontend.utils.UndoRedoHelper;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompat;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompatFactory;

import static com.duy.pascal.frontend.EditorControl.ACTION_COMPILE;
import static com.duy.pascal.frontend.EditorControl.ACTION_COPY;
import static com.duy.pascal.frontend.EditorControl.ACTION_CUT;
import static com.duy.pascal.frontend.EditorControl.ACTION_FORMAT_CODE;
import static com.duy.pascal.frontend.EditorControl.ACTION_GOTO_LINE;
import static com.duy.pascal.frontend.EditorControl.ACTION_PASTE;
import static com.duy.pascal.frontend.EditorControl.ACTION_REDO;
import static com.duy.pascal.frontend.EditorControl.ACTION_RUN;
import static com.duy.pascal.frontend.EditorControl.ACTION_SAVE;
import static com.duy.pascal.frontend.EditorControl.ACTION_SAVE_AS;
import static com.duy.pascal.frontend.EditorControl.ACTION_SELECT_ALL;
import static com.duy.pascal.frontend.EditorControl.ACTION_UNDO;

/**
 * EditText with undo and redo support
 * <p>
 * Created by Duy on 15-Mar-17.
 */

public abstract class KeyBoardFilterEditText extends HighlightEditor {
    private static final boolean LOG_KEY_EVENTS = true;
    private UndoRedoHelper undoRedoHelper;
    private KeySettings mSettings;
    private SharedPreferences mPrefs;
    private KeyListener mKeyListener;
    private ClipboardManagerCompat clipboardManager;
    private EditorControl editorControl;

    public KeyBoardFilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KeyBoardFilterEditText(Context context) {
        super(context);
        init();
    }

    public KeyBoardFilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        super.init();
        undoRedoHelper = new UndoRedoHelper(this);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSettings = new KeySettings(mPrefs);
        mKeyListener = new KeyListener();
        clipboardManager = ClipboardManagerCompatFactory.getManager(getContext());
    }

    /**
     * undo text
     */
    public void undo() {
        undoRedoHelper.undo();
    }

    /**
     * redo text
     */
    public void redo() {
        undoRedoHelper.redo();
    }

    /**
     * @return <code>true</code> if stack not empty
     */
    public boolean canUndo() {
        return undoRedoHelper.getCanUndo();
    }

    /**
     * @return <code>true</code> if stack not empty
     */
    public boolean canRedo() {
        return undoRedoHelper.getCanRedo();
    }

    /**
     * clear history
     */
    public void clearStackHistory() {
        undoRedoHelper.clearHistory();
    }

    public void saveHistory(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        undoRedoHelper.storePersistentState(editor, key);
        editor.apply();
    }

    public void restoreHistory(String key) {
        undoRedoHelper.restorePersistentState(
                PreferenceManager.getDefaultSharedPreferences(getContext()), key);

    }


    /**
     * @param keyCode - key code event
     * @param down    - is down
     * @return - <code>true</code> if is ctrl key
     */
    private boolean handleControlKey(int keyCode, boolean down) {
        if (keyCode == mSettings.getControlKeyCode()) {
            Log.w(TAG, "handler control key: ");
            mKeyListener.handleControlKey(down);
            return true;
        }
        return false;
    }

    /**
     * CTRL + C copy
     * CTRL + V paste
     * CTRL + B: compile
     * CTRL + R run
     * CTRL + X cut
     * CTRL + Z undo
     * CTRL + Y redo
     * CTRL + Q quit
     * CTRL + S save
     * CTRL + O open
     * CTRL + F find
     * CTRL + H find and replace
     * CTRL + L format code
     * CTRL + G: goto line
     */
    @Override
    public boolean onKeyDown(int zKeyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.w(TAG, "onKeyDown: " + zKeyCode + " " + event);

        //The new Key Code
        int keyCode = event.getKeyCode();

        if (handleControlKey(keyCode, true)) {
            return true;
        } else if (event.isSystem()) {
            // Don't intercept the system keys And the HOME /END / PGUP / PGDOWN KEYS
            return super.onKeyDown(keyCode, event);
        }

        int state = mKeyListener.keyDown(keyCode, event);
        if (state != -1) {
            Log.d(TAG, "onKeyDown: " + state);
            switch (state) {
                case ACTION_SELECT_ALL:
                    Selection.selectAll(getText());
                    break;
                case ACTION_COPY:
                    copy();
                    break;
                case ACTION_PASTE:
                    paste();
                    break;
                case ACTION_CUT:
                    cut();
                    break;
                case ACTION_RUN:
                    assert editorControl != null;
                    editorControl.runProgram();
                    break;
                case ACTION_COMPILE:
                    assert editorControl != null;
                    editorControl.doCompile();
                    break;
                case ACTION_GOTO_LINE:
                    assert editorControl != null;
                    editorControl.goToLine();
                    break;
                case ACTION_FORMAT_CODE:
                    assert editorControl != null;
                    editorControl.formatCode();
                    break;
                case ACTION_UNDO:
                    if (canUndo()) undo();
                    break;
                case ACTION_REDO:
                    if (canRedo()) redo();
                    break;
                case ACTION_SAVE:
                    assert editorControl != null;
                    editorControl.saveFile();
                    break;
                case ACTION_SAVE_AS:
                    assert editorControl != null;
                    editorControl.saveAs();
                default:
                    return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(zKeyCode, event);
    }

    public void cut() {
        clipboardManager.setText(getText()
                .subSequence(getSelectionStart(), getSelectionEnd()));
        getEditableText().delete(getSelectionStart(), getSelectionEnd());
    }

    public void paste() {
        insert(clipboardManager.getText());
    }

    public void copy() {
        clipboardManager.setText(getText()
                .subSequence(getSelectionStart(), getSelectionEnd()));
    }

    /**
     * insert text
     *
     * @param delta text for insert
     */
    public void insert(CharSequence delta) {
        getText().insert(getSelectionStart(), delta, getSelectionStart(), getSelectionEnd());
    }

    @Override
    public boolean onKeyUp(int zKeyCode, KeyEvent event) {
        if (LOG_KEY_EVENTS) Log.w(TAG, "onKeyUp " + zKeyCode);

        //The new Key Code
        int keyCode = event.getKeyCode();
        if (handleControlKey(keyCode, false)) {
            return true;
        } else if (event.isSystem()) {
            // Don't intercept the system keys
            return super.onKeyUp(keyCode, event);
        }
        if (mKeyListener.keyUp(keyCode) == -1)
            return super.onKeyUp(zKeyCode, event);
        else
            return true;
    }


//    @Override
//    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//        return new BaseInputConnection(this, false) {
//            @Override
//            public boolean performEditorAction(int actionCode) {
//                if (actionCode == EditorInfo.IME_ACTION_UNSPECIFIED
//                        || actionCode == EditorInfo.IME_MASK_ACTION) {
//                    insert("\n");
//                    return true;
//                }
//                return false;
//            }
//
//        };
//    }

    public EditorControl getEditorControl() {
        return editorControl;
    }

    public void setEditorControl(EditorControl editorControl) {
        this.editorControl = editorControl;
    }
}
