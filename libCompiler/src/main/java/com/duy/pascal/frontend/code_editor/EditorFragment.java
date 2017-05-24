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

package com.duy.pascal.frontend.code_editor;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.define.UnrecognizedTypeException;
import com.duy.pascal.backend.exceptions.missing.MissingTokenException;
import com.duy.pascal.backend.lib.PascalLibraryManager;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokenizer.IndentCode;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.code_editor.editor_view.EditorView;
import com.duy.pascal.frontend.code_editor.editor_view.LineUtils;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.view.LockableScrollView;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Duy on 15-Mar-17.
 * Editor fragment
 */
public class EditorFragment extends Fragment implements EditorListener {
    private static final String TAG = "EditorFragment";
    private EditorView mCodeEditor;
    @Nullable
    private LockableScrollView mScrollView;
    private ApplicationFileManager mFileManager;
    private Handler handler = new Handler();


    public static EditorFragment newInstance(String filePath) {
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
        mCodeEditor = (EditorView) view.findViewById(R.id.code_editor);
        mScrollView = (LockableScrollView) view.findViewById(R.id.vertical_scroll);

        ApplicationFileManager fileManager = new ApplicationFileManager(getContext());
        StringBuilder code = fileManager.fileToString(getArguments().getString(CompileManager.FILE_PATH));
        mCodeEditor.setTextHighlighted(code);
        mCodeEditor.clearHistory();
        mCodeEditor.restoreHistory(getFilePath());

        try {
            mCodeEditor.setEditorControl((EditorControl) getActivity());
        } catch (Exception ignored) {
        }

        if (mScrollView != null) {
            mCodeEditor.setVerticalScroll(mScrollView);
            mScrollView.setScrollListener(new LockableScrollView.ScrollListener() {
                @Override
                public void onScroll(int x, int y) {
                    mCodeEditor.updateTextSyntax();
                }
            });
        }
        mCodeEditor.setSuggestData(PascalLibraryManager.getAllMethodDescription(SystemLib.class,
                IOLib.class, FileLib.class));
        return view;
    }


    @Override
    public void onStop() {
        saveFile();
        if (mCodeEditor != null && getFilePath() != null) {
            mCodeEditor.saveHistory(getFilePath());
        } else {
            Log.d(TAG, "onDestroy: " + " null editor");
        }

        super.onStop();
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

    public void autoFix(ParsingException e) {
        if (e instanceof UnrecognizedTypeException) {
            mCodeEditor.getAutoFixError().autoFixType((UnrecognizedTypeException) e);
        } else if (e instanceof NoSuchFunctionOrVariableException) {
            mCodeEditor.getAutoFixError().autoFixDefine((NoSuchFunctionOrVariableException) e);
        } else if (e instanceof UnConvertibleTypeException) {
            mCodeEditor.getAutoFixError().autoFixConvertType((UnConvertibleTypeException) e);
        } else if (e instanceof MissingTokenException) {

        }
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
                    //do some thing
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
        try {
            IndentCode autoIndentCode;
            autoIndentCode = new IndentCode(new StringReader(text));
            StringBuilder result = autoIndentCode.getResult();
            mCodeEditor.setTextHighlighted(result);
            mCodeEditor.applyTabWidth(mCodeEditor.getText(), 0, mCodeEditor.getText().length());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @NonNull
    @Override
    public String getCode() {
        return mCodeEditor.getCleanText();
    }

    @Override
    public void insert(CharSequence text) {
        mCodeEditor.insert(text);
    }

    @Nullable
    public EditorView getEditor() {
        return mCodeEditor;
    }

    public void setLineError(@NonNull final LineInfo lineInfo) {
        mCodeEditor.setLineError(lineInfo);
        mCodeEditor.refresh();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScrollView != null) {
                    mScrollView.smoothScrollTo(0, LineUtils.getYAtLine(mScrollView,
                            mCodeEditor.getLineCount(), lineInfo.line));
                }
            }
        }, 100);
    }

    public void refreshCodeEditor() {
        mCodeEditor.updateFromSettings();
        mCodeEditor.refresh();
    }

    public String getFilePath() {
        String path = getArguments().getString(CompileManager.FILE_PATH);
        if (path == null) {
            return "";
        } else {
            return path;
        }
    }

}
