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

package com.duy.pascal.ui.themefont.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.purchase.Premium;
import com.duy.pascal.ui.purchase.activities.InAppPurchaseActivity;
import com.duy.pascal.ui.themefont.adapter.SectionPageAdapter;
import com.duy.pascal.ui.themefont.fragments.ThemeFragment;
import com.duy.pascal.ui.themefont.model.CodeTheme;
import com.google.firebase.analytics.FirebaseAnalytics;


/**
 * Created by Duy on 12-Mar-17.
 */


public class ThemeFontActivity extends InAppPurchaseActivity implements ThemeFragment.OnThemeSelectListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionPageAdapter mAdapter;

    @Override
    public void updateUiPremium() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_font);
        mToolbar = findViewById(R.id.toolbar);
        setupToolbar();

        FirebaseAnalytics.getInstance(this).logEvent("open_choose_font_theme", new Bundle());

        mViewPager = findViewById(R.id.view_pager);
        mAdapter = new SectionPageAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.theme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onThemeSelected(CodeTheme codeTheme) {
        if (codeTheme.isPremium() && !Premium.isPremiumUser(this)) {
            showDialogUpgrade();
        } else {
            getPreferences().setTheme(codeTheme.getName());
            String text = getString(R.string.select) + " " + codeTheme.getName();
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }


}
