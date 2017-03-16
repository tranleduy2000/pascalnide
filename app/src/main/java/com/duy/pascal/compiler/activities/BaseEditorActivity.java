package com.duy.pascal.compiler.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.file_manager.FileManager;
import com.duy.pascal.compiler.view.LockableScrollView;
import com.duy.pascal.compiler.view.SymbolListView;
import com.duy.pascal.compiler.view.code_view.CodeView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 09-Mar-17.
 */

public abstract class BaseEditorActivity extends AbstractAppCompatActivity {
    protected String mFilePath = FileManager.getApplicationPath() + "new_file.pas";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view)
    SymbolListView mKeyList;
    //    @BindView(R.id.file_list)
//    FileListView mFilesView;
    @BindView(R.id.scroll)
    LockableScrollView mScrollView;
    @BindView(R.id.edit_editor)
    CodeView mCodeView;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private long lastTimeClickTab = System.currentTimeMillis();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        setTitle(R.string.app_name);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
        setupTab();
    }

    private void setupTab() {

        FileManager fileManager = new FileManager(this);
        ArrayList<File> listFile = fileManager.getListFile("");
        for (File file : listFile) {
            tabLayout.addTab(tabLayout.newTab().setText(file.getName()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO: 16-Mar-17  move to tab
                Log.d(TAG, "onTabSelected: ");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                long currentTime = System.currentTimeMillis();
                Log.d(TAG, "onTabReselected: ");
            }
        });
    }
}
