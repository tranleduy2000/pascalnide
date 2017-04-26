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

package com.duy.pascal.frontend.view.code_view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.program_structure.viewholder.StructureType;

import java.util.ArrayList;

/**
 * Created by Duy on 26-Apr-17.
 */

public class CodeSuggestAdapter extends ArrayAdapter<SuggestItem> {
    private static final String TAG = "CodeSuggestAdapter";
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<SuggestItem> source;
    private ArrayList<SuggestItem> filterData;
    private Filter codeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SuggestItem) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterData.clear();
            if (constraint != null) {
                for (SuggestItem item : source) {
                    if (item.getName().startsWith(constraint.toString())) {
                        filterData.add(item);
                    }
                }
                filterResults.values = filterData;
                filterResults.count = filterData.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SuggestItem> filterList = (ArrayList<SuggestItem>) results.values;
            clear();
            if (results != null && results.count > 0) {
                addAll(filterList);
            }
            notifyDataSetChanged();
        }
    };

    public CodeSuggestAdapter(@NonNull Context context, @LayoutRes int resID) {
        super(context, resID);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        source = new ArrayList<>();
        filterData = new ArrayList<>();
    }

    public CodeSuggestAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull ArrayList<SuggestItem> source) {
        super(context, resource);
        this.context = context;
        this.source = new ArrayList<>();
        filterData = new ArrayList<>();
    }


    public ArrayList<SuggestItem> getSource() {
        return source;
    }

    public void setSource(ArrayList<SuggestItem> source) {
        this.source = source;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position > filterData.size()- 1) return convertView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.code_hint, parent, false);
        }
        final SuggestItem item = filterData.get(position);
        TextView txtName = (TextView) convertView.findViewById(R.id.txt_title);
        txtName.setText(item.getName());
        View btnInfo = convertView.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        final ImageView iconView = (ImageView) convertView.findViewById(R.id.img_icon);
        String prefix = StructureType.ICONS[item.getType()];
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(StructureType.COLORS_FOREGROUND[item.getType()]).bold()
                .endConfig()
                .buildRound(prefix, StructureType.COLORS_BACKGROUND[item.getType()]);
        iconView.setImageDrawable(drawable);
        return convertView;
    }

    public void clearData() {
        source.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return codeFilter;
    }

}
