package com.duy.pascal.compiler.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.data.Preferences;
import com.duy.pascal.compiler.data.TabFileUtils;
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
    protected long lastTimeClickTab = System.currentTimeMillis();
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
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileManager = new FileManager(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setTitle("");
        setupTab();
    }

    protected void setupTab() {
        listFile = TabFileUtils.getTabFiles(this);
        for (File file : listFile) {
            addTabFile(file);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doSelectTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected: ");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                long currentTime = System.currentTimeMillis();
//                if (currentTime - lastTimeClickTab < 200) {
//                    removeTab(tab);
//                } else
//                    lastTimeClickTab = currentTime;
//                Log.d(TAG, "onTabReselected: ");
            }
        });
    }

    protected TabLayout.Tab getTab(File file) {
        final TabLayout.Tab tab = tabLayout.newTab().setText(file.getName());
        tab.setCustomView(R.layout.item_tab_file);

        View root = tab.getCustomView();
        View vClose = tab.getCustomView().findViewById(R.id.img_close);
        vClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTab(tab);
            }
        });
        TextView txtTitle = (TextView) root.findViewById(R.id.txt_title);
        txtTitle.setText(file.getName());
        return tab;
    }

    /**
     * remove tab
     *
     * @param tab - tab for remove
     */
    protected void removeTab(TabLayout.Tab tab) {
        //get position
        int position = tab.getPosition();
        //save file and remove tab entry
        fileManager.saveFile(listFile.get(position), getCode());
        fileManager.removeTabFile(listFile.get(position).getPath());
        listFile.remove(position);
        tabLayout.removeTab(tab);

        //set last position to view
        if (position > 0) {
            loadFile(listFile.get(position - 1).getPath());
        } else if (position == 0) {
            // TODO: 17-Mar-17
        }
        //show toast
        Toast.makeText(this, "closed", Toast.LENGTH_SHORT).show();
    }

    protected void doSelectTab(TabLayout.Tab tab) {
        Log.d(TAG, "onTabSelected: ");
        lastTimeClickTab = System.currentTimeMillis();
        /**
         * save history for undo redo
         */
//        mCodeView.saveHistory(mFilePath);
        fileManager.saveFile(mFilePath, getCode());

        loadFile(listFile.get(tab.getPosition()).getPath());

        /**
         * restore history undo redo of file
         */
        mCodeView.clearStackHistory();
//        mCodeView.restoreHistory(mFilePath);
    }

    protected void addTabFile(File file) {
        TabLayout.Tab tab = getTab(file);
        tabLayout.addTab(tab);
    }

    protected void moveToTab(int i) {
        TabLayout.Tab tab = tabLayout.getTabAt(i);
        if (tab != null) {
            if (!tab.isSelected()) tab.select();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        moveToTab(mPreferences.getInt(Preferences.TAB_POSITION_FILE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences.put(Preferences.TAB_POSITION_FILE, tabLayout.getSelectedTabPosition());
    }

    class LoadTabFile extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

    protected abstract String getCode();

    protected abstract void loadFile(String path);
}
