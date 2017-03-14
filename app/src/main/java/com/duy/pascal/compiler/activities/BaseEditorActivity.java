package com.duy.pascal.compiler.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.file_manager.FileListView;
import com.duy.pascal.compiler.file_manager.FileManager;
import com.duy.pascal.compiler.view.LockableScrollView;
import com.duy.pascal.compiler.view.SymbolListView;
import com.duy.pascal.compiler.view.code_view.HighlightEditor;

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
    @BindView(R.id.file_list)
    FileListView mFilesView;
    @BindView(R.id.scroll)
    LockableScrollView mScrollView;
    @BindView(R.id.edit_editor)
    HighlightEditor mHighlightEditor;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }
}
