package com.duy.pascal.compiler.view.code_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.duy.pascal.compiler.utils.UndoRedoHelper;

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

    @Override
    public Parcelable onSaveInstanceState() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        undoRedoHelper.storePersistentState(editor, "history_undo_redo");
        editor.apply();
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        undoRedoHelper.restorePersistentState(PreferenceManager.getDefaultSharedPreferences(getContext()), "history_undo_redo");
        super.onRestoreInstanceState(state);
    }
}
