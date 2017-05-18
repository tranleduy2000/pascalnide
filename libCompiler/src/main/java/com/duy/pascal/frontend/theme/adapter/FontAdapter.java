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

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.theme.FontFragment;
import com.duy.pascal.frontend.theme.util.FontManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<String> listPathFont = new ArrayList<>();
    private PascalPreferences pascalPreferences;
    @Nullable
    private FontFragment.OnFontSelectListener onFontSelectListener;

    public FontAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        pascalPreferences = new PascalPreferences(context);
        try {
            String[] fonts = context.getAssets().list("fonts");
            listPathFont = Arrays.asList(fonts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_font_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtSample.setTextSize(pascalPreferences.getTextSize());
        holder.txtSample.setTypeface(FontManager.getFontFromAsset(context,
                listPathFont.get(position)));

        String name = listPathFont.get(position);
        name = name.replace("_", " ").replace("-", " ").toLowerCase();
        if (name.contains(".")) {
            holder.txtName.setText(name.substring(0, name.indexOf(".")));
        } else {
            holder.txtName.setText(name);
        }

        final String finalName = name;
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pascalPreferences.setFont(listPathFont.get(position));
                Toast.makeText(context,
                        context.getString(R.string.select) + " " + finalName,
                        Toast.LENGTH_SHORT).show();

                if (onFontSelectListener != null) {
                    onFontSelectListener.onFontSelect(finalName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPathFont.size();
    }

    @Nullable
    public FontFragment.OnFontSelectListener getOnFontSelectListener() {
        return onFontSelectListener;
    }

    public void setOnFontSelectListener(@Nullable FontFragment.OnFontSelectListener onFontSelectListener) {
        this.onFontSelectListener = onFontSelectListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSample;
        TextView txtName;
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSample = (TextView) itemView.findViewById(R.id.txt_sample);
            txtName = (TextView) itemView.findViewById(R.id.txt_name);
            btnSelect = (Button) itemView.findViewById(R.id.btn_select);

        }
    }
}
