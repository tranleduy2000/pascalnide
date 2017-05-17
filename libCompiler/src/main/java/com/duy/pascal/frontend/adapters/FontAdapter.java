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

package com.duy.pascal.frontend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.theme.FontManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private PascalPreferences mPascalPreferences;
    private Context context;
    private List<String> listPathFont;

    public FontAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
        try {
            String[] fontses = context.getAssets().list("fonts");
            listPathFont = Arrays.asList(fontses);
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
        holder.txtSample.setTypeface(FontManager.getFontFromAsset(context,
                listPathFont.get(position)));
    }

    @Override
    public int getItemCount() {
        return listPathFont.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSample;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSample = (TextView) itemView.findViewById(R.id.txt_title);
        }
    }
}
