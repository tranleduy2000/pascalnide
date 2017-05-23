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

package com.duy.pascal.frontend.code_sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManagerCompat;
import com.duy.pascal.frontend.code_editor.editor_view.EditorView;

//import butterknife.BindView;

/**
 * Created by Duy on 20-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
class CodeHolder extends RecyclerView.ViewHolder {
//    @BindView(R.id.txt_title)
    TextView txtTitle;
//    @BindView(R.id.img_play)
    View btnPlay;
//    @BindView(R.id.img_edit)
    View btnEdit;
//    @BindView(R.id.img_copy)
    View btnCopy;
//    @BindView(R.id.editor_view)
    EditorView editorView;


    public CodeHolder(View view) {
        super(view);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        btnPlay = view.findViewById(R.id.img_play);
        btnEdit = view.findViewById(R.id.img_edit);
        btnCopy = view.findViewById(R.id.img_copy);
        editorView = (EditorView) view.findViewById(R.id.editor_view);
    }

    public void bind(CodeSampleEntry codeSampleEntry,
                     final CodeSampleAdapter.OnCodeClickListener listener,
                     final ClipboardManagerCompat clipboardManagerCompat) {
        //set code
        final String content = codeSampleEntry.getContent();

        editorView.disableTextChangedListener();
        editorView.setTextHighlighted(content);
        editorView.applyTabWidth();

        editorView.setCanEdit(false);
        if (codeSampleEntry.getQuery() != null && !codeSampleEntry.getQuery().isEmpty()) {
            editorView.find(codeSampleEntry.getQuery(), false, false, false);
        }

        txtTitle.setText(codeSampleEntry.getName());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlay(content);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEdit(content);
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboardManagerCompat.setText(content);
            }
        });
    }
}
