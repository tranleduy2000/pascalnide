/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use getContext() file except in compliance with the License.
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

package com.duy.pascal.frontend.code_editor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duy.pascal.backend.lib.PascalLibraryManager;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokenizer.AutoIndentCode;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.utils.LineUtils;
import com.duy.pascal.frontend.view.LockableScrollView;
import com.duy.pascal.frontend.view.code_view.CodeView;
import com.duy.pascal.frontend.view.code_view.HighlightEditor;

import java.io.File;

/**
 * Created by Duy on 15-Mar-17.
 */

public class EditorFragment extends Fragment implements EditorListener {
    private CodeView mCodeEditor;
    private LockableScrollView mScrollView;
    private ApplicationFileManager mFileManager;
    private Handler handler = new Handler();

    public static Fragment newInstance(String filePath) {
        EditorFragment editorFragment = new EditorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CompileManager.FILE_PATH, filePath);
        editorFragment.setArguments(bundle);
        return editorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFileManager = new ApplicationFileManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        mCodeEditor = (CodeView) view.findViewById(R.id.code_editor);
        mScrollView = (LockableScrollView) view.findViewById(R.id.vertical_scroll);

        ApplicationFileManager fileManager = new ApplicationFileManager(getContext());
        String code = fileManager.readFileAsString(getArguments().getString(CompileManager.FILE_PATH));
        mCodeEditor.setTextHighlighted(code);
        mCodeEditor.clearHistory();
        mCodeEditor.restoreHistory(getFilePath());

        try {
            mCodeEditor.setEditorControl((EditorControl) getActivity());
        } catch (Exception ignored) {
        }
        mCodeEditor.setVerticalScroll(mScrollView);
        mScrollView.setScrollListener(new LockableScrollView.ScrollListener() {
            @Override
            public void onScroll(int x, int y) {
                mCodeEditor.updateHighlightWithDelay(HighlightEditor.SHORT_DELAY);
            }
        });
        mCodeEditor.setSuggestData(PascalLibraryManager.getAllMethodDescription(SystemLib.class,
                IOLib.class, FileLib.class));
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        saveFile();
        if (mCodeEditor != null) {
            mCodeEditor.saveHistory(getFilePath());
        }
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeEditor.updateFromSettings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void saveAs() {

    }

    @Override
    public void doFindAndReplace(String from, String to, boolean regex, boolean matchCase) {
        mCodeEditor.replaceAll(from, to, regex, matchCase);
    }

    @Override
    public void doFind(String find, boolean regex, boolean wordOnly, boolean matchCase) {
        mCodeEditor.find(find, regex, wordOnly, matchCase);
    }

    @Override
    public void saveFile() {
        if (mCodeEditor == null) return;
        String filePath = getArguments().getString(CompileManager.FILE_PATH);
        boolean result;
        if (filePath != null) {
            try {
                String code = getCode();
                result = mFileManager.saveFile(filePath, code);
                if (result) {
                    Toast.makeText(getContext(), getString(R.string.saved) + " " + (new File(filePath).getName()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.can_not_save_file) + " " + (new File(filePath).getName()),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void goToLine(int line) {
        mCodeEditor.goToLine(line);
    }

    @Override
    public void formatCode() {
        String text = getCode();
        AutoIndentCode autoIndentCode = new AutoIndentCode();
        String result = autoIndentCode.format(text);
        mCodeEditor.setTextHighlighted(result);
        mCodeEditor.applyTabWidth(mCodeEditor.getText(), 0, mCodeEditor.getText().length());
    }

    @Override
    public void undo() {
        if (mCodeEditor.canUndo()) {
            mCodeEditor.undo();
        } else {
            Toast.makeText(getContext(), R.string.cant_undo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void redo() {
        if (mCodeEditor.canRedo()) {
            mCodeEditor.redo();
        } else {
            Toast.makeText(getContext(), R.string.cant_redo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void paste() {
        mCodeEditor.paste();
    }

    @Override
    public void copyAll() {
        mCodeEditor.copyAll();
    }

    @Override
    public String getCode() {
        return mCodeEditor.getCleanText();
    }

    @Override
    public void insert(CharSequence text) {
        mCodeEditor.insert(text);
    }

    public CodeView getEditor() {
        return mCodeEditor;
    }

    public void setLineError(@NonNull final LineInfo lineInfo) {
        mCodeEditor.setLineError(lineInfo);
        mCodeEditor.refresh();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, LineUtils.getYAtLine(mScrollView,
                        mCodeEditor.getLineCount(), lineInfo.line));
            }
        }, 100);
    }

    public void refreshCodeEditor() {
        mCodeEditor.updateFromSettings();
    }

    public String getFilePath() {
        String path = getArguments().getString(CompileManager.FILE_PATH);
        if (path == null) {
            return "";
        } else {
            return path;
        }
    }

    public void hideKeyboard() {
    }
}
