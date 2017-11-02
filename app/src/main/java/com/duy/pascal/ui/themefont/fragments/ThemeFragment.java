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

package com.duy.pascal.ui.themefont.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duy.pascal.ui.BaseFragment;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.purchase.Premium;
import com.duy.pascal.ui.themefont.activities.CustomThemeActivity;
import com.duy.pascal.ui.themefont.adapter.ThemeAdapter;
import com.duy.pascal.ui.themefont.model.CodeTheme;
import com.duy.pascal.ui.themefont.themes.ThemeManager;

/**
 * Created by Duy on 17-May-17.
 */

public class ThemeFragment extends BaseFragment {

    private static final int REQ_CREATE_NEW_THEME = 20023;
    @Nullable
    private ThemeAdapter mCodeThemeAdapter;
    private RecyclerView mRecyclerView;
    @Nullable
    private OnThemeSelectListener mOnThemeSelect;
    private FloatingActionButton mFabCreate;

    public static ThemeFragment newInstance() {
        Bundle args = new Bundle();
        ThemeFragment fragment = new ThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnThemeSelect = (OnThemeSelectListener) getActivity();
        } catch (Exception ignored) {

        }
    }

    @Override
    protected int getRootId() {
        return (R.layout.fragment_theme);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFabCreate = view.findViewById(R.id.btn_create);
        mFabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Premium.isPremiumUser(getActivity())) {
                    Intent intent = new Intent(getActivity(), CustomThemeActivity.class);
                    startActivityForResult(intent, REQ_CREATE_NEW_THEME);
                } else {
                    showDialogUpgrade();
                }
            }
        });


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mCodeThemeAdapter = new ThemeAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCodeThemeAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFabCreate.isShown()) {
                    mFabCreate.hide();
                } else if (dy < 0 && !mFabCreate.isShown()) {
                    mFabCreate.show();
                }
            }
        });
        mCodeThemeAdapter.setOnThemeSelectListener(mOnThemeSelect);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CREATE_NEW_THEME:
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCodeThemeAdapter != null) {
                            ThemeManager.reload(getContext());
                            mCodeThemeAdapter.reload(getContext());
                        }
                    }
                });
                break;
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return mCodeThemeAdapter;
    }

    public void notifyDataSetChanged() {
        if (mCodeThemeAdapter != null) {
            mCodeThemeAdapter.notifyDataSetChanged();
        }
    }

    public interface OnThemeSelectListener {
        void onThemeSelected(CodeTheme name);
    }

}