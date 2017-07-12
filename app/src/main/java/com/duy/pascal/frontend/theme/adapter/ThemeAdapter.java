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
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.editor.view.EditorView;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.theme.fragment.ThemeFragment;
import com.duy.pascal.frontend.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Object> mThemes = new ArrayList<>();
    private LayoutInflater inflater;
    private PascalPreferences mPascalPreferences;
    private Activity context;

    @Nullable
    private ThemeFragment.OnThemeSelectListener onThemeSelectListener;

    public ThemeAdapter(Activity context) {
        Collections.addAll(mThemes, context.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    public void clear() {
        mThemes.clear();
        notifyDataSetChanged();
    }

    public void reload() {
        Collections.addAll(mThemes, context.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (Utils.PATCHED) {
            return 1;
        } else {
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.list_item_theme, parent, false);
            return new CodeThemeHolder(view);
        } else {
            View view = inflater.inflate(R.layout.list_item_get_pro_theme, parent, false);
            return new ProHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int pos) {
        //free version
        final int position;
        if (Utils.PATCHED) {
            position = pos;
        } else {
            position = pos - 1;
        }
        if (holder instanceof CodeThemeHolder) {
            CodeThemeHolder holder1 = (CodeThemeHolder) holder;
            holder1.editorView.setLineError(new LineInfo(3, 0, ""));
            if ((mThemes.get(position) instanceof String)) {
                holder1.editorView.setColorTheme((String) mThemes.get(position));
            } else {
                holder1.editorView.setColorTheme((int) mThemes.get(position));
            }
            holder1.editorView.setTextHighlighted(CodeSample.DEMO_THEME);
            holder1.txtTitle.setText(String.valueOf(mThemes.get(position)));
            holder1.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPascalPreferences.put(context.getString(R.string.key_code_theme),
                            String.valueOf(mThemes.get(position)));
                    Toast.makeText(context,
                            context.getString(R.string.select) + " " + mThemes.get(position),
                            Toast.LENGTH_SHORT).show();
                    if (onThemeSelectListener != null) {
                        onThemeSelectListener.onThemeSelect(String.valueOf(mThemes.get(position)));
                    }
                }
            });
        } else if (holder instanceof ProHolder) {
            ((ProHolder) holder).root.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (Utils.PATCHED) {
            return mThemes.size();
        } else {
            return mThemes.size() + 1;
        }
    }

    @Nullable
    public ThemeFragment.OnThemeSelectListener getOnThemeSelectListener() {
        return onThemeSelectListener;
    }

    public void setOnThemeSelectListener(@Nullable ThemeFragment.OnThemeSelectListener onThemeSelectListener) {
        this.onThemeSelectListener = onThemeSelectListener;
    }

    class CodeThemeHolder extends RecyclerView.ViewHolder {
        EditorView editorView;
        TextView txtTitle;
        Button btnSelect;

        public CodeThemeHolder(View itemView) {
            super(itemView);
            editorView = (EditorView) itemView.findViewById(R.id.editor_view);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            btnSelect = (Button) itemView.findViewById(R.id.btn_select);
        }
    }

    class ProHolder extends RecyclerView.ViewHolder {
        View root;

        public ProHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.container);
        }
    }
}
