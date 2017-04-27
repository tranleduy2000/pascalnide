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

package com.duy.pascal.frontend.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.clipboard.ClipboardManager;

import java.util.ArrayList;

/**
 * Adapter for list sample code
 * <p>
 * Created by Duy on 08-Apr-17.
 */
public class CodeSampleAdapter extends RecyclerView.Adapter<CodeHolder> {
    private ArrayList<CodeSampleEntry> codeSampleEntries = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private OnCodeClickListener listener;

    public CodeSampleAdapter(Context context) {
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
        holder.codeView.setTextHighlighted(codeSampleEntry.getContent());
        holder.codeView.setCanEdit(false);

        holder.txtTitle.setText(codeSampleEntry.getName());

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onPlay(codeSampleEntry.getContent());
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEdit(codeSampleEntry.getContent());
            }
        });
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, codeSampleEntry.getContent());
            }
        });
    }

    public void setListener(OnCodeClickListener listener) {
        this.listener = listener;
    }

    public void addCodes(ArrayList<CodeSampleEntry> listCodeCategories) {
        this.codeSampleEntries.addAll(listCodeCategories);
    }

    @Override
    public int getItemCount() {
        return codeSampleEntries.size();
    }

    public interface OnCodeClickListener {
        void onPlay(String code);
        //            void onCopy(String code);
        void onEdit(String code);
    }

}
