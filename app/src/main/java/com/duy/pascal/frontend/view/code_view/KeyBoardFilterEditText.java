package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.keyboard.KeyListener;
import com.duy.pascal.frontend.keyboard.KeySettings;
import com.duy.pascal.frontend.utils.UndoRedoHelper;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompat;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompatFactory;

import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_COMPILE;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_COPY;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_CUT;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_FORMAT_CODE;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_GOTO_LINE;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_PASTE;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_REDO;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_RUN;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_SAVE;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_SAVE_AS;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_SELECT_ALL;
import static com.duy.pascal.frontend.keyboard.KeyListener.ACTION_UNDO;

/**
 * EditText with undo and redo support
 * <p>
 * Created by Duy on 15-Mar-17.
 */

public abstract class KeyBoardFilterEditText extends HighlightEditor {
    private static final boolean LOG_KEY_EVENTS = true;
    private static final String TAG = KeyBoardFilterEditText.class.getSimpleName();
    private UndoRedoHelper mUndoRedoHelper;
    private KeySettings mSettings;
    private KeyListener mKeyListener;
    private ClipboardManagerCompat mClipboardManager;
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

    private void init() {
        Log.i(TAG, "init: ");
        mUndoRedoHelper = new UndoRedoHelper(this);
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSettings = new KeySettings(mPrefs, getContext());
        mKeyListener = new KeyListener();
        mClipboardManager = ClipboardManagerCompatFactory.getManager(getContext());
    }

    /**
     * undo text
     */
    public void undo() {
        mUndoRedoHelper.undo();
    }

    /**
     * redo text
     */
    public void redo() {
        mUndoRedoHelper.redo();
    }

    /**
     * @return <code>true</code> if stack not empty
     */
    public boolean canUndo() {
        return mUndoRedoHelper.getCanUndo();
    }

    /**
     * @return <code>true</code> if stack not empty
     */
    public boolean canRedo() {
        return mUndoRedoHelper.getCanRedo();
    }

    /**
     * clear history
     */
    public void clearHistory() {
        mUndoRedoHelper.clearHistory();
    }

    public void saveHistory(String key) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        mUndoRedoHelper.storePersistentState(editor, key);
        editor.apply();
    }

    public void restoreHistory(String key) {
        mUndoRedoHelper.restorePersistentState(
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
        }

        int state = mKeyListener.keyDown(keyCode, event);
        if (state != -1) {
            Log.d(TAG, "onKeyDown: " + state);
            switch (state) {
                case ACTION_SELECT_ALL:
                    selectAll();
                    return true;
                case ACTION_COPY:
                    copy();
                    return true;
                case ACTION_PASTE:
                    paste();
                    return true;
                case ACTION_CUT:
                    cut();
                    return true;
                case ACTION_RUN:
                    if (editorControl != null)
                        editorControl.runProgram();
                    return true;
                case ACTION_COMPILE:
                    if (editorControl != null)
                        editorControl.doCompile();
                    return true;
                case ACTION_GOTO_LINE:
                    if (editorControl != null)
                        editorControl.goToLine();
                    return true;
                case ACTION_FORMAT_CODE:
                    if (editorControl != null)
                        editorControl.formatCode();
                    return true;
                case ACTION_UNDO:
                    if (canUndo()) undo();
                    return true;
                case ACTION_REDO:
                    if (canRedo()) redo();
                    return true;
                case ACTION_SAVE:
                    if (editorControl != null)
                        editorControl.saveFile();
                    return true;
                case ACTION_SAVE_AS:
                    if (editorControl != null)
                        editorControl.saveAs();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(zKeyCode, event);
    }


    @Override
    public boolean onKeyUp(int zKeyCode, KeyEvent event) {
        if (LOG_KEY_EVENTS) Log.w(TAG, "onKeyUp " + zKeyCode);

        //The new Key Code
        int keyCode = event.getKeyCode();
        if (handleControlKey(keyCode, false)) {
            return true;
        }
        if (mKeyListener.keyUp(keyCode) == -1)
            return super.onKeyUp(zKeyCode, event);
        else
            return true;
    }

    public void cut() {
        mClipboardManager.setText(getText()
                .subSequence(getSelectionStart(), getSelectionEnd()));
        getEditableText().delete(getSelectionStart(), getSelectionEnd());
    }

    public void paste() {
        insert(mClipboardManager.getText().toString());
    }

    /**
     * insert text
     *
     * @param delta text for insert
     */
    public void insert(CharSequence delta) {
//        setSelection(getSelectionStart());
//        getText().insert(getSelectionStart(), delta);
        getText().replace(getSelectionStart(), getSelectionEnd(), delta);
    }

    public void copy() {
        mClipboardManager.setText(getText().subSequence(getSelectionStart(), getSelectionEnd()));
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new BaseInputConnection(this, false);
    }

    public EditorControl getEditorControl() {
        return editorControl;
    }

    public void setEditorControl(EditorControl editorControl) {
        this.editorControl = editorControl;
    }
}
