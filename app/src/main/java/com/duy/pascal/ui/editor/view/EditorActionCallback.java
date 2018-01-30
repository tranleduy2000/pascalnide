/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.ui.editor.view;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.duy.pascal.ui.EditorControl;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.utils.DLog;

/**
 * Created by Duy on 12-Dec-17.
 */
class EditorActionCallback implements ActionMode.Callback {

    private static final String TAG = "EditorActionCallback";
    private EditorView editorView;

    EditorActionCallback(EditorView editText) {
        this.editorView = editText;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.menu_editor_action, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_duplicate:
                duplicateSelection();
                break;
            case R.id.action_run:
                EditorControl editorControl = editorView.getEditorControl();
                if (editorControl != null) editorControl.runProgram();
                break;
        }
        return false;
    }

    private void duplicateSelection() {
        DLog.d(TAG, "duplicate() called");

        int selectionStart = editorView.getSelectionStart();
        int selectionEnd = editorView.getSelectionEnd();
        if (selectionEnd < 0) return;
        CharSequence textToInsert = editorView.getText().subSequence(selectionStart, selectionEnd);
        editorView.getText().insert(selectionEnd, textToInsert);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
