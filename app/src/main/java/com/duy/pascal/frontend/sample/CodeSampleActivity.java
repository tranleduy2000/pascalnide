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

package com.duy.pascal.frontend.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.activities.EditorActivity;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeSampleActivity extends AbstractAppCompatActivity implements CodeSampleAdapter.OnCodeClickListener {

    final String TAG = getClass().getSimpleName();

    private final String[] categories;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ApplicationFileManager fileManager;
    private SearchHistoryTable mHistoryDatabase;

    public CodeSampleActivity() {
        categories = new String[]{"Basic", "System", "Crt", "Dos", "Graph", "Math", "Android", "More"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileManager = new ApplicationFileManager(getApplicationContext());
        setContentView(R.layout.activity_code_sample);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_code_sample);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CodePagerAdapter pagerAdapter = new CodePagerAdapter(getSupportFragmentManager(), categories);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        mHistoryDatabase = new SearchHistoryTable(this);
        searchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }

            @Override
            public boolean onOpen() {
                return false;
            }
        });
        searchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                openDrawer();
            }

        });
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPlay(String code) {
        //create file temp
        fileManager.setContentFileTemp(code);

        //set file temp for run
        Intent intent = new Intent(this, ExecuteActivity.class);

        //this code is verified, do not need compile
        intent.putExtra(CompileManager.FILE_PATH, fileManager.getTempFile().getPath());
        startActivity(intent);
    }

    @Override
    public void onEdit(String code) {
        //create file temp
        String file = fileManager.createNewFileInMode("sample_" + Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        fileManager.saveFile(file, code);

        //set file temp for run
        Intent intent = new Intent(this, EditorActivity.class);

        //this code is verified, do not need compile
        intent.putExtra(CompileManager.FILE_PATH, file);
        startActivity(intent);
    }


}
