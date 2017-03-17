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
    protected FileManager fileManager;
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
    ArrayList<File> listFile;
    private long lastTimeClickTab = System.currentTimeMillis();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileManager = new FileManager(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("");
        setupTab();
    }

    private void setupTab() {

        listFile = fileManager.getListFile(FileManager.getApplicationPath());
        for (File file : listFile) {
            tabLayout.addTab(tabLayout.newTab().setText(file.getName()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doSelectFile(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTimeClickTab < 200) {
                    fileManager.saveFile(listFile.get(tab.getPosition()), getCode());
                    listFile.remove(tab.getPosition());
                    tabLayout.removeTab(tab);
                } else lastTimeClickTab = currentTime;
                Log.d(TAG, "onTabReselected: ");
            }
        });
    }

    private void doSelectFile(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected: ");
        lastTimeClickTab = System.currentTimeMillis();
        /**
         * save history for undo redo
         */
        mCodeView.saveHistory(mFilePath);
        fileManager.saveFile(mFilePath, getCode());

        loadFile(listFile.get(tab.getPosition()).getPath());

        /**
         * restore history undo redo of file
         */
        mCodeView.clearStackHistory();
        mCodeView.restoreHistory(mFilePath);
    }

    protected abstract String getCode();

    protected abstract void loadFile(String path);
}
