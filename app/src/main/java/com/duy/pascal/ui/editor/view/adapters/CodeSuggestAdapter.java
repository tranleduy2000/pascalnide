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

package com.duy.pascal.ui.editor.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.DescriptionImpl;
import com.duy.pascal.ui.setting.PascalPreferences;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Duy on 26-Apr-17.
 */
public class CodeSuggestAdapter extends ArrayAdapter<Description> {
    private static final String TAG = "CodeSuggestAdapter";
    private final Context mContext;
    private final int mColorKeyWord;
    private final int mColorNormal;
    private final int colorVariable = 0xffFFB74D;
    private LayoutInflater mInflater;
    private ArrayList<Description> mOirignal;
    private ArrayList<Description> mSuggestion;
    private int mLayoutId;
    private PascalPreferences mSetting;
    private Filter codeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (resultValue == null) {
                return "";
            }
            return ((Description) resultValue).getInsertText();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            mSuggestion.clear();
            if (constraint != null) {
                for (Description item : mOirignal) {
                    if (item.getName().toLowerCase().compareTo(constraint.toString().toLowerCase()) == 0) {
                        mSuggestion.add(item);
                    }
                }
                filterResults.values = mSuggestion;
                filterResults.count = mSuggestion.size();
            }
            return filterResults;
        }

        @Override
        @SuppressWarnings("unchecked")

        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DescriptionImpl> filteredList = (ArrayList<DescriptionImpl>) results.values;
            clear();
            if (filteredList != null && filteredList.size() > 0) {
                addAll(filteredList);
            }
            notifyDataSetChanged();
        }
    };

    public CodeSuggestAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Description> objects) {
        super(context, resource, objects);
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mOirignal = (ArrayList<Description>) objects.clone();
        this.mSuggestion = new ArrayList<>();
        this.mLayoutId = resource;
        this.mColorKeyWord = context.getResources().getColor(R.color.color_key_word_color);
        this.mColorNormal = context.getResources().getColor(android.R.color.primary_text_dark);
        this.mSetting = new PascalPreferences(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, null);
        }

        final Description item = getItem(position);
        if (item != null) {
            TextView txtHeader = convertView.findViewById(R.id.txt_header);
            txtHeader.setVisibility(View.VISIBLE);
            txtHeader.setTypeface(Typeface.MONOSPACE);
            txtHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSetting.getEditorTextSize());

            TextView txtName = convertView.findViewById(R.id.txt_name);
            txtName.setTypeface(Typeface.MONOSPACE);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSetting.getEditorTextSize());
            txtName.setText(item.getHeader());
            switch (item.getKind()) {
                case DescriptionImpl.KIND_KEYWORD:
                    txtHeader.setVisibility(View.INVISIBLE);
                    break;
                case DescriptionImpl.KIND_VARIABLE:
                    txtHeader.setText("v");
                    break;
                case DescriptionImpl.KIND_CONST:
                    txtHeader.setText("c");
                    break;
                case DescriptionImpl.KIND_FUNCTION:
                    txtHeader.setText("f");
                    break;
                case DescriptionImpl.KIND_PROCEDURE:
                    txtHeader.setText("p");
                    break;
                case DescriptionImpl.KIND_TYPE:
                    txtHeader.setText("t");
                    break;
                default:
                    txtHeader.setVisibility(View.INVISIBLE);
                    break;
            }
            View btnInfo = convertView.findViewById(R.id.img_info);
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getDescription() == null || item.getDescription().isEmpty()) {
                        Toast.makeText(mContext, R.string.no_document, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, item.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return convertView;
    }


    public void clearAllData() {
        super.clear();
        mOirignal.clear();
    }

    public void addData(@NonNull Collection<? extends Description> collection) {
        addAll(collection);
        mOirignal.addAll(collection);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return codeFilter;
    }


}
