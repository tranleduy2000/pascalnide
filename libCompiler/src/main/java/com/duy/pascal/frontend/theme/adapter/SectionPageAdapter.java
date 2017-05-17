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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 17-May-17.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ThemeFontFragment.newInstance(ThemeFontFragment.FONT);
            case 1:
                return ThemeFontFragment.newInstance(ThemeFontFragment.THEME);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.font);
            case 1:
                return context.getString(R.string.theme);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Created by Duy on 17-May-17.
     */

    public static class ThemeFontFragment extends Fragment {

        public static final int FONT = 0;
        public static final int THEME = 1;
        private RecyclerView mRecyclerView;

        public static ThemeFontFragment newInstance(int position) {

            Bundle args = new Bundle();
            args.putInt("type", position);
            ThemeFontFragment fragment = new ThemeFontFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_font_theme, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            int type = getArguments().getInt("type");
            switch (type) {
                case THEME:
                    ThemeAdapter codeThemeAdapter = new ThemeAdapter(getContext());
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(codeThemeAdapter);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL));
                    break;
                case FONT:

                    FontAdapter fontAdapter = new FontAdapter(getContext());
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mRecyclerView.setAdapter(fontAdapter);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL));

                    break;
            }


        }
    }
}
