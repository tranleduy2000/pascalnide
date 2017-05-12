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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManager;

import java.util.ArrayList;

/**
 * Adapter for list sample code
 * <p>
 * Created by Duy on 08-Apr-17.
 */
class CodeSampleAdapter extends RecyclerView.Adapter<CodeHolder> {
    private ArrayList<CodeSampleEntry> codeSampleEntries = new ArrayList<>();
    private ArrayList<CodeSampleEntry> originalData = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private OnCodeClickListener listener;

    CodeSampleAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.code_view_item, parent, false);
        return new CodeHolder(view);
    }

    @Override
    public void onBindViewHolder(final CodeHolder holder, int position) {
        final CodeSampleEntry codeSampleEntry = codeSampleEntries.get(position);
        //set code
        final String content = codeSampleEntry.getContent();

        holder.editorView.setTextHighlighted(content);
        holder.editorView.applyTabWidth();

        holder.editorView.setCanEdit(false);
        if (codeSampleEntry.getQuery() != null && !codeSampleEntry.getQuery().isEmpty()) {
            holder.editorView.find(codeSampleEntry.getQuery(), false, false, false);
        }

        holder.txtTitle.setText(codeSampleEntry.getName());

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlay(content);
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEdit(content);
            }
        });
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, content);
            }
        });
    }

    public void setListener(OnCodeClickListener listener) {
        this.listener = listener;
    }

    void addCodes(ArrayList<CodeSampleEntry> listCodeCategories) {
        this.originalData.addAll(listCodeCategories);
        this.codeSampleEntries.addAll(listCodeCategories);
    }

    @Override
    public int getItemCount() {
        return codeSampleEntries.size();
    }

    public void query(String query) {
        int size = codeSampleEntries.size();
        codeSampleEntries.clear();
        notifyItemRangeRemoved(0, size);

        int count = 0;
        for (CodeSampleEntry codeSampleEntry : originalData) {
            if (codeSampleEntry.getName().contains(query) ||
                    codeSampleEntry.getContent().contains(query)) {
                CodeSampleEntry clone = codeSampleEntry.clone();
                clone.setQuery(query);
                codeSampleEntries.add(clone);
                notifyItemInserted(codeSampleEntries.size() - 1);
                count++;
            }
        }
        if (count == 0) {
            Toast.makeText(context, "No matching", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, count + " file", Toast.LENGTH_SHORT).show();
        }
    }


    public interface OnCodeClickListener {
        void onPlay(String code);

        //            void onCopy(String code);
        void onEdit(String code);
    }
}
