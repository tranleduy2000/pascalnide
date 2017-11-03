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

package com.duy.pascal.ui.code.sample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.sample.model.CodeSampleEntry;
import com.duy.pascal.ui.editor.view.EditorView;
import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.utils.clipboard.ClipboardManagerCompat;
import com.duy.pascal.ui.utils.clipboard.ClipboardManagerCompatFactory;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

/**
 * Adapter for list sample code
 * <p>
 * Created by Duy on 08-Apr-17.
 */
public class CodeSampleAdapter extends RecyclerView.Adapter<CodeSampleAdapter.CodeHolder>
        implements FastScrollRecyclerView.SectionedAdapter {
    private static final String TAG = "CodeSampleAdapter";

    private ClipboardManagerCompat mClipboard;
    private ArrayList<CodeSampleEntry> mItems = new ArrayList<>();
    private ArrayList<CodeSampleEntry> originalData = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private OnCodeClickListener listener;

    public CodeSampleAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        mClipboard = ClipboardManagerCompatFactory.newInstance(context);
    }

    @Override
    public CodeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_code, parent, false);
        return new CodeHolder(view);
    }

    @Override
    public void onBindViewHolder(final CodeHolder holder, int position) {
        final CodeSampleEntry codeSampleEntry = mItems.get(position);
        holder.bind(codeSampleEntry, listener, mClipboard);
    }

    public void setListener(OnCodeClickListener listener) {
        this.listener = listener;
    }

    public void addCodes(ArrayList<CodeSampleEntry> listCodeCategories) {
//        DLog.d(TAG, "addCodes() called with: listCodeCategories = [" + listCodeCategories + "]");

        this.originalData.addAll(listCodeCategories);
        this.mItems.addAll(listCodeCategories);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void query(String query) {
        int size = mItems.size();
        mItems.clear();
        notifyItemRangeRemoved(0, size);

        int count = 0;
        for (CodeSampleEntry codeSampleEntry : originalData) {
            if (codeSampleEntry.getName().contains(query) ||
                    codeSampleEntry.getContent().contains(query)) {
                CodeSampleEntry clone = codeSampleEntry.clone();
                clone.setQuery(query);
                mItems.add(clone);
                notifyItemInserted(mItems.size() - 1);
                count++;
            }
        }
        if (count == 0) {
            Toast.makeText(context, "No matching", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, count + " file", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return mItems.get(position).getName().charAt(0) + "";
    }


    public interface OnCodeClickListener {
        void onClickRun(String code);

        //            void onCopy(String code);
        void onClickEdit(String code);
    }


    static class CodeHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        View btnPlay;
        View btnEdit;
        View btnCopy;
        EditorView editorView;


        CodeHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txt_name);
            btnPlay = view.findViewById(R.id.img_play);
            btnEdit = view.findViewById(R.id.img_edit);
            btnCopy = view.findViewById(R.id.img_copy);
            editorView = view.findViewById(R.id.editor_view);
        }

        void bind(CodeSampleEntry codeSampleEntry,
                  final OnCodeClickListener listener,
                  final ClipboardManagerCompat clipboardManagerCompat) {
            //set code
            final String content = codeSampleEntry.getContent();

            editorView.setMaxLines(50);
            editorView.setEllipsize(TextUtils.TruncateAt.END);
            editorView.setCanEdit(false);
            editorView.disableTextChangedListener();
            editorView.setText(content);
            editorView.refresh();
            if (codeSampleEntry.getQuery() != null && !codeSampleEntry.getQuery().isEmpty()) {
                editorView.find(codeSampleEntry.getQuery(), false, false, false);
            }

            txtTitle.setText(codeSampleEntry.getName());

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickRun(content);
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickEdit(content);
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
}
