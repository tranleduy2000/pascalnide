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

package com.duy.pascal.ui.themefont.fonts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.setting.PascalPreferences;

/**
 * Created by Duy on 17-May-17.
 */

public class ConsoleFontFragment extends BaseFragment implements SharedPreferences.OnSharedPreferenceChangeListener, OnFontSelectListener {

    public static final int FONT = 0;
    public static final int THEME = 1;
    protected PascalPreferences mPref;
    private FontAdapter2 mFontAdapter;
    private RecyclerView mRecyclerView;

    public static ConsoleFontFragment newInstance() {
        Bundle args = new Bundle();
        ConsoleFontFragment fragment = new ConsoleFontFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_font, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mFontAdapter = new FontAdapter2(getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFontAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mFontAdapter.setOnFontSelectListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPref = new PascalPreferences(context);
    }

    @Override
    public void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onFontSelected(FontEntry fontEntry) {
        mPref.setConsoleFont(fontEntry);
        String msg = String.format("%s %s", getString(R.string.select), fontEntry.name);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgradeClick() {
        showDialogUpgrade();
    }
}