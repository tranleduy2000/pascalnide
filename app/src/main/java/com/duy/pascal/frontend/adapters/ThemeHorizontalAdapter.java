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

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CodeSample;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.editor_view.EditorView;

import java.util.ArrayList;
import java.util.Collections;

public class ThemeHorizontalAdapter extends PagerAdapter {
    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Object> mThemes = new ArrayList<>();
    private PascalPreferences mPascalPreferences;

    public ThemeHorizontalAdapter(final Activity context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mPascalPreferences = new PascalPreferences(context);
        addDefaultData();
    }

    private void addDefaultData() {
        Collections.addAll(mThemes, mContext.getResources().getStringArray(R.array.code_themes));
        for (Integer i = 0; i < 20; i++) {
            mThemes.add(i);
        }
    }

    @Override
    public int getCount() {
        return mThemes.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.code_theme_item, container, false);
        setupItem(view, position);
        container.addView(view);
        return view;
    }

    private void setupItem(View view, final int position) {
        EditorView editorView = (EditorView) view.findViewById(R.id.code_view);
        if ((mThemes.get(position) instanceof String)) {
            editorView.setTheme((String) mThemes.get(position));
        } else {
            editorView.setTheme((int) mThemes.get(position));
        }

        editorView.setTextHighlighted(CodeSample.DEMO_THEME);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(String.valueOf(mThemes.get(position)));
        view.findViewById(R.id.btn_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPascalPreferences.put(mContext.getString(R.string.key_code_theme),
                        String.valueOf(mThemes.get(position)));
                mContext.finish();
            }
        });
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}