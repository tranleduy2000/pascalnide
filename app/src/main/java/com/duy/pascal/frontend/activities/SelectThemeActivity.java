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

package com.duy.pascal.frontend.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.adapters.CodeThemeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 12-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class SelectThemeActivity extends AbstractAppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);
        ButterKnife.bind(this);
        setupActionBar();

        CodeThemeAdapter codeThemeAdapter = new CodeThemeAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(codeThemeAdapter);
//        ThemeHorizontalAdapter adapter = new ThemeHorizontalAdapter(this);
//        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.setScrollDuration(500);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        setTitle(R.string.theme);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);

    }

}
