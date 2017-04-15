package com.duy.pascal.frontend.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.file.FileListener;
import com.duy.pascal.frontend.file.FragmentSelectFile;
import com.duy.pascal.frontend.file.TabFileUtils;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.LockableScrollView;
import com.duy.pascal.frontend.view.SymbolListView;
import com.duy.pascal.frontend.view.code_view.CodeView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 09-Mar-17.
 */

public abstract class FileEditorActivity extends AbstractAppCompatActivity
        implements SymbolListView.OnKeyListener,
        EditorControl, FileListener {
    protected final static String TAG = FileEditorActivity.class.getSimpleName();

    protected String mFilePath = ApplicationFileManager.getApplicationPath() + "new_file.pas";
    protected ApplicationFileManager mFileManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view)
    SymbolListView mKeyList;
    @BindView(R.id.vertical_scroll)
    LockableScrollView mScrollView;
    @BindView(R.id.edit_editor)
    CodeView mCodeView;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    ArrayList<File> listFile = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        mFileManager = new ApplicationFileManager(this);

//        new LoadTabFile().execute();
        initContent();
        loadTabFile();
    }

    private void loadTabFile() {
        listFile = TabFileUtils.getTabFiles(FileEditorActivity.this);
        for (File file : listFile) {
            addNewTab(file);
        }
        if (listFile.isEmpty()) {//empty file
            createEmptyFile();
        } else {
            int pos = (mPascalPreferences.getInt(PascalPreferences.TAB_POSITION_FILE));
            TabLayout.Tab tab = tabLayout.getTabAt((pos));
            if (tab != null) {
                selectTab(tab, false);
            }
        }
    }


    private void initContent() {
        //setup action bar
        setSupportActionBar(toolbar);
        setTitle("");

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    protected TabLayout.Tab createNewTab(File file) {
        final TabLayout.Tab tab = tabLayout.newTab().setText(file.getName());
        tab.setCustomView(R.layout.item_tab_file);
        View root = tab.getCustomView();
        if (root != null) {
            View vClose = root.findViewById(R.id.img_close);
            vClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTab(tab, true);
                }
            });
            TextView txtTitle = (TextView) root.findViewById(R.id.txt_title);
            txtTitle.setText(file.getName());
            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tab.isSelected()) {
                        tab.select();
                        selectTab(tab, true);
                    }
                }
            });
        }
        return tab;
    }

    protected void removeTab(TabLayout.Tab tab, boolean saveLastFile, boolean createNewFileIfNeed) {
        //get position
        int position = tab.getPosition();
        mCodeView.clearHistory();

        //set last position to view
        if (position > 0) {
            //save file and remove tab entry
            if (saveLastFile)
                mFileManager.saveFile(listFile.get(position), getCode());

            mFileManager.removeTabFile(listFile.get(position).getPath());
            listFile.remove(position);
            tabLayout.removeTab(tab);

            //load near tab file
            loadFile(listFile.get(position - 1).getPath());
            //toast
            Toast.makeText(this, R.string.closed, Toast.LENGTH_SHORT).show();
        } else if (position == 0) {
            if (listFile.size() >= 2) {
                //save file and remove tab entry
                if (saveLastFile)
                    mFileManager.saveFile(listFile.get(position), getCode());
                //remove tab
                mFileManager.removeTabFile(listFile.get(position).getPath());
                listFile.remove(position);
                tabLayout.removeTab(tab);

                //choose tab index 0, because remove index 0
                selectTab(tabLayout.getTabAt(0), false);
                Toast.makeText(this, R.string.closed, Toast.LENGTH_SHORT).show();
            } else { //size of list = 1, position = 0
                if (createNewFileIfNeed) {
                    //save file
                    if (saveLastFile)
                        mFileManager.saveFile(listFile.get(position), getCode());

                    //remove tab in position
                    mFileManager.removeTabFile(listFile.get(position).getPath());
                    listFile.remove(position);
                    tabLayout.removeTab(tab);

                    createEmptyFile();
                } else//dont remove file
                    Toast.makeText(this, R.string.cant_close_file, Toast.LENGTH_SHORT).show();
            }
        }
        //show toast
    }

    /**
     * remove tab
     *
     * @param tab - tab for remove
     */
    protected void removeTab(TabLayout.Tab tab, boolean saveLastFile) {
        removeTab(tab, saveLastFile, false);
    }

    private void createEmptyFile() {
        //auto create empty file
        //create new file
        String filePath =
                mFileManager.createNewFile(ApplicationFileManager.getApplicationPath() + "new_" +
                        Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        File file = new File(filePath);
        //load to view
        addNewTab(file);
        mCodeView.clearHistory();
        mFileManager.addNewPath(filePath);
        listFile.add(file);
        selectTab(tabLayout.getTabAt(0), false);
    }

    /**
     * move to tab
     *
     * @param tab  - tab for select
     * @param save - <code>true</code> save last file,
     */
    protected void selectTab(TabLayout.Tab tab, boolean save) {
        if (tab == null) {
            Log.d(TAG, "selectTab: tab is null");
            return;
        }
        moveToTab(tab.getPosition());

        /**
         * save history for undo redo
         */
//        mCodeView.saveHistory(mFilePath);
        if (save) mFileManager.saveFile(mFilePath, getCode());

        loadFile(listFile.get(tab.getPosition()).getPath());
        /**
         * restore history undo redo of file
         */
        mCodeView.clearHistory();
//        mCodeView.restoreHistory(mFilePath);
    }

    protected void addNewTab(File file) {
        TabLayout.Tab tab = createNewTab(file);
        tabLayout.addTab(tab);
    }


    /**
     * check file if not in list file, add it to tab and select tab of file
     */
    protected void addNewFile(File file, boolean saveLastFile) {
        if (file.exists()) {
            int index = listFile.indexOf(file);
            if (index != -1) {
                selectTab(tabLayout.getTabAt(index), saveLastFile);
            } else {
                mFileManager.addNewPath(file.getPath());
                listFile.add(file);
                addNewTab(file);
                selectTab(tabLayout.getTabAt(listFile.size() - 1), saveLastFile);
            }
        } else {
        }
    }

    protected void moveToTab(final int i) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.select();
                }
            }
        }, 30);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPascalPreferences.put(PascalPreferences.TAB_POSITION_FILE, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
                mFilePath = intent.getStringExtra(CompileManager.FILE_PATH);
                addNewFile(new File(mFilePath), false);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
            mFilePath = intent.getStringExtra(CompileManager.FILE_PATH);
            addNewFile(new File(mFilePath), false);
        }
    }

    protected abstract String getCode();

    protected abstract void loadFile(String path);


    // TODO: 15-Mar-17 code delete file
    @Override
    public boolean doRemoveFile(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_file_msg) + file.getName());
        builder.setTitle(R.string.delete_file);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = listFile.indexOf(file);
                boolean success = mFileManager.deleteFile(file);
                if (success) {
                    if (position >= 0) {
                        removeTab(tabLayout.getTabAt(position), false, true);
                    }
                    Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();

                //reload file
                FragmentSelectFile fragmentSelectFile =
                        (FragmentSelectFile) getSupportFragmentManager().findFragmentByTag("fragment_file_view");
                if (fragmentSelectFile != null) {
                    fragmentSelectFile.refresh();
                } else {
                    Log.d(TAG, "onClick: Fragment file is null");
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
        return false;
    }

    protected File getCurrentFile() {
        return listFile.get(tabLayout.getSelectedTabPosition());
    }

    @Override
    public void saveAs() {
        final AppCompatEditText edittext = new AppCompatEditText(this);
        edittext.setHint(R.string.enter_new_file_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_as)
                .setView(edittext)
                .setIcon(R.drawable.ic_create_new_folder_white_24dp)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fileName = edittext.getText().toString();
                        dialog.cancel();
                        File currentFile = getCurrentFile();
                        if (currentFile != null) {
                            mFileManager.saveFile(getCurrentFile().getParent() + "/" + fileName,
                                    mCodeView.getCleanText());
                        } else {
                            mFileManager.saveFile(mFileManager.createNewFileInMode(fileName),
                                    mCodeView.getCleanText());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    /**
     * load lasted file in database and set text to tablayout
     */
    private class LoadTabFile extends AsyncTask<Void, File, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            listFile = TabFileUtils.getTabFiles(FileEditorActivity.this);
            boolean result = false;
            for (File file : listFile) {
                publishProgress(file);
                result = true;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(File... values) {
            super.onProgressUpdate(values);
            addNewTab(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (!result) {//empty file
                createEmptyFile();
            } else {
                int pos = (mPascalPreferences.getInt(PascalPreferences.TAB_POSITION_FILE));
                TabLayout.Tab tab = tabLayout.getTabAt((pos));
                if (tab != null) {
                    selectTab(tab, false);
                }
            }
        }
    }

}
