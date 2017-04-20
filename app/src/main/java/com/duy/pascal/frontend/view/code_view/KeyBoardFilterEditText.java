/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.keyboard.KeyListener;
import com.duy.pascal.frontend.keyboard.KeySettings;
import com.duy.pascal.frontend.utils.UndoRedoHelper;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompat;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompatFactory;

/**
 * EditText with undo and redo support
 * <p>
 * Created by Duy on 15-Mar-17.
 */

public abstract class KeyBoardFilterEditText extends HighlightEditor {
    public static final int ID_SELECT_ALL = android.R.id.selectAll;
    public static final int ID_CUT = android.R.id.cut;
    public static final int ID_COPY = android.R.id.copy;
    public static final int ID_PASTE = android.R.id.paste;
    private static final boolean LOG_KEY_EVENTS = true;
    private static final String TAG = KeyBoardFilterEditText.class.getSimpleName();
    private static final int ID_UNDO = R.id.action_undo;
    private static final int ID_REDO = R.id.action_redo;
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
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onKey: " + event);
                return false;
            }
        });
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
    private boolean handleControlKey(int keyCode, KeyEvent event, boolean down) {
        if (keyCode == mSettings.getControlKeyCode()
                || event.isCtrlPressed()) {
            Log.w(TAG, "handler control key: ");
            mKeyListener.handleControlKey(down);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == ID_UNDO) {
            undo();
            return true;
        } else if (id == ID_REDO) {
            redo();
            return true;
        } else {
            return super.onTextContextMenuItem(id);
        }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (DLog.DEBUG) Log.w(TAG, "onKeyDown: " + keyCode + " " + event);
        if (handleControlKey(keyCode, event, true)) {
            return true;
        }
        if (event.isCtrlPressed() || mKeyListener.mControlKey.isActive()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    return onTextContextMenuItem(ID_SELECT_ALL);
                case KeyEvent.KEYCODE_X:
                    return onTextContextMenuItem(ID_CUT);
                case KeyEvent.KEYCODE_C:
                    return onTextContextMenuItem(ID_COPY);
                case KeyEvent.KEYCODE_V:
                    return onTextContextMenuItem(ID_PASTE);
                case KeyEvent.KEYCODE_R:
                    if (editorControl != null)
                        editorControl.runProgram();
                    return true;
                case KeyEvent.KEYCODE_B:
                    if (editorControl != null)
                        editorControl.doCompile();
                    return true;
                case KeyEvent.KEYCODE_G:
                    if (editorControl != null)
                        editorControl.goToLine();
                    return true;
                case KeyEvent.KEYCODE_L:
                    if (editorControl != null)
                        editorControl.formatCode();
                    return true;
                case KeyEvent.KEYCODE_Z:
                    if (canUndo()) {
                        return onTextContextMenuItem(ID_UNDO);
                    }
                case KeyEvent.KEYCODE_Y:
                    if (canRedo()) {
                        return onTextContextMenuItem(ID_REDO);
                    }
                case KeyEvent.KEYCODE_S:
                    if (editorControl != null)
                        editorControl.saveFile();
                    return true;
                case KeyEvent.KEYCODE_N:
                    if (editorControl != null)
                        editorControl.saveAs();
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_TAB:
                    String textToInsert = "\t";
                    int start, end;
                    start = Math.max(getSelectionStart(), 0);
                    end = Math.max(getSelectionEnd(), 0);
                    getText().replace(Math.min(start, end), Math.max(start, end),
                            textToInsert, 0, textToInsert.length());
                    return true;
                default:
                    return super.onKeyDown(keyCode, event);
            }
        }
    }

    @Override
    public boolean onKeyUp(int zKeyCode, KeyEvent event) {
        if (LOG_KEY_EVENTS) Log.w(TAG, "onKeyUp " + zKeyCode);

        //The new Key Code
        int keyCode = event.getKeyCode();
        if (handleControlKey(keyCode, event, false)) {
            return true;
        }
        if (event.isCtrlPressed() || mKeyListener.mControlKey.isActive()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                case KeyEvent.KEYCODE_X:
                case KeyEvent.KEYCODE_C:
                case KeyEvent.KEYCODE_V:
                case KeyEvent.KEYCODE_Z:
                case KeyEvent.KEYCODE_Y:
                case KeyEvent.KEYCODE_S:
                    return true;
                default:
                    return false;
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_TAB:
                    return true;
                default:
                    return false;
            }
        }
    }

    public void cut() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        selectionStart = Math.max(0, selectionStart);
        selectionEnd = Math.max(0, selectionEnd);
        mClipboardManager.setText(getText().subSequence(selectionStart, selectionEnd));
        getEditableText().delete(selectionStart, selectionEnd);
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
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        selectionStart = Math.max(0, selectionStart);
        selectionEnd = Math.max(0, selectionEnd);
        getText().replace(selectionStart, selectionEnd, delta);
    }

    public void copy() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        selectionStart = Math.max(0, selectionStart);
        selectionEnd = Math.max(0, selectionEnd);
        mClipboardManager.setText(getText().subSequence(selectionStart, selectionEnd));
    }

    public void setEditorControl(EditorControl editorControl) {
        this.editorControl = editorControl;
    }


}
