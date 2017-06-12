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

package com.duy.pascal.frontend.onlineexpample.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.onlineexpample.adapters.ExamplePageAdapter;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Duy on 12-Jun-17.
 */

public class ExampleActivity extends AbstractAppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    MaterialSearchView searchView;
    Toolbar toolbar;
    private ApplicationFileManager mFileManager;
    private ExamplePageAdapter pagerAdapter;
    private MaterialViewPager mMaterialViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_examples);

        FirebaseAnalytics.getInstance(this).logEvent("ExampleActivity", new Bundle());
        mFileManager = new ApplicationFileManager(this);
        bindView();
    }

    private void bindView() {

        mMaterialViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = mMaterialViewPager.getToolbar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.code_sample);

        viewPager = mMaterialViewPager.getViewPager();
        pagerAdapter = new ExamplePageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        mMaterialViewPager.getPagerTitleStrip().setViewPager(viewPager);
        mMaterialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });
    }
}
