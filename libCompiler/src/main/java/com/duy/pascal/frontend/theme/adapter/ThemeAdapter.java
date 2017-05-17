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

package com.duy.pascal.frontend.theme.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.BasePascalApplication;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.editor_view.EditorView;

import java.util.Collections;
import java.util.LinkedList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
    private LinkedList<Object> mThemes = new LinkedList<>();
    private LayoutInflater inflater;
    private PascalPreferences mPascalPreferences;
    private Activity context;

    public ThemeAdapter(Activity context) {
        Collections.addAll(mThemes, context.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (((BasePascalApplication) context.getApplication()).isProVersion()) {
            return 1;
        }
        if (position == 0) {
            return 0; //get pro version
        } else {
            return 1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.list_item_theme, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_get_pro_theme, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int pos) {

        //free version
        final int position;

        if (((BasePascalApplication) context.getApplication()).isProVersion()) {
            position = pos;
        } else {
            position = pos - 1;
        }

        if ((mThemes.get(position) instanceof String)) {
            holder.editorView.setColorTheme((String) mThemes.get(position));
        } else {
            holder.editorView.setColorTheme((int) mThemes.get(position));
        }
        holder.editorView.setTextHighlighted(CodeSample.DEMO_THEME);
        holder.txtTitle.setText(String.valueOf(mThemes.get(position)));
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPascalPreferences.put(context.getString(R.string.key_code_theme),
                        String.valueOf(mThemes.get(position)));
                Toast.makeText(context,
                        context.getString(R.string.select) + " " + mThemes.get(position),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (((BasePascalApplication) context.getApplication()).isProVersion()) {
            return mThemes.size();
        } else {
            return mThemes.size() + 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EditorView editorView;
        TextView txtTitle;
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            editorView = (EditorView) itemView.findViewById(R.id.editor_view);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            btnSelect = (Button) itemView.findViewById(R.id.btn_select);
        }
    }
}
