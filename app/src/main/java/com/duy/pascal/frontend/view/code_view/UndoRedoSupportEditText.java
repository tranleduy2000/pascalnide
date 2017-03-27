package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.duy.pascal.frontend.utils.UndoRedoHelper;

/**
 * EditText with undo and redo support
 * <p>
 * Created by Duy on 15-Mar-17.
 */

public abstract class UndoRedoSupportEditText extends HighlightEditor {
    private UndoRedoHelper undoRedoHelper;

    public UndoRedoSupportEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UndoRedoSupportEditText(Context context) {
        super(context);
        init();
    }

    public UndoRedoSupportEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        super.init();
        undoRedoHelper = new UndoRedoHelper(this);
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

    @Override
    public Parcelable onSaveInstanceState() {

        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
