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

package com.duy.pascal.ui.themefont.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.purchase.Premium;
import com.duy.pascal.ui.setting.PascalPreferences;
import com.duy.pascal.ui.themefont.fonts.FontManager;
import com.duy.pascal.ui.themefont.fonts.OnFontSelectListener;
import com.duy.pascal.ui.themefont.model.FontEntry;

import java.util.ArrayList;


/**
 * Created by Duy on 14-Jul-17.
 */

public class FontAdapter2 extends RecyclerView.Adapter<FontAdapter2.ViewHolder> {
    private final Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FontEntry> mListFonts = new ArrayList<>();
    @Nullable
    private OnFontSelectListener mListener;
    private PascalPreferences mPascalPreferences;

    public FontAdapter2(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mListFonts = FontManager.getAll(mContext);
        this.mPascalPreferences = new PascalPreferences(context);
    }

    public void setOnFontSelectListener(@Nullable OnFontSelectListener onFontSelectListener) {
        this.mListener = onFontSelectListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.list_item_font_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FontEntry fontEntry = mListFonts.get(position);

        holder.txtSample.setTextSize(mPascalPreferences.getEditorTextSize() * 2);
        holder.txtSample.setTypeface(FontManager.getFont(fontEntry, mContext));

        String name = fontEntry.name;
        name = name.replace("_", " ").replace("-", " ").toLowerCase();
        if (name.contains(".")) {
            holder.txtName.setText(name.substring(0, name.indexOf(".")));
        } else {
            holder.txtName.setText(name);
        }

        if (fontEntry.isPremium && !Premium.isPremiumUser(mContext)) {
            holder.btnSelect.setText(R.string.premium_version);
        } else {
            holder.btnSelect.setText(R.string.select);
        }

        final int pos = position;
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (Premium.canUseAdvancedFeature(mContext)) {
                        mListener.onFontSelected(mListFonts.get(pos));
                    } else {
                        if (mListFonts.get(pos).isPremium()) {
                            mListener.onUpgradeClick();
                        } else {
                            mListener.onFontSelected(mListFonts.get(pos));
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFonts.size();
    }


    public interface OnFontClickListener {
        void onFontClick(String name);

        void onUpgradeClick();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSample;
        TextView txtName;
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            txtSample = itemView.findViewById(R.id.txt_sample);
            txtName = itemView.findViewById(R.id.txt_name);
            btnSelect = itemView.findViewById(R.id.btn_select);
        }
    }

}
