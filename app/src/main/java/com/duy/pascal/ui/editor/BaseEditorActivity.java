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

package com.duy.pascal.ui.editor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.SimplePageDescriptor;
import com.duy.pascal.ui.EditorControl;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.activities.BaseActivity;
import com.duy.pascal.ui.code.CompileManager;
import com.duy.pascal.ui.file.FileActionCallback;
import com.duy.pascal.ui.file.FileClipboard;
import com.duy.pascal.ui.file.FileExplorerView;
import com.duy.pascal.ui.file.FileManager;
import com.duy.pascal.ui.file.Pref;
import com.duy.pascal.ui.file.fragment.FileListPagerFragment;
import com.duy.pascal.ui.file.io.LocalFile;
import com.duy.pascal.ui.file.util.FileListSorter;
import com.duy.pascal.ui.file.util.TabFileUtils;
import com.duy.pascal.ui.setting.PascalPreferences;
import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.ui.view.SymbolListView;
import com.github.clans.fab.FloatingActionMenu;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.duy.pascal.ui.R.id.action_new_folder;

/**
 * Created by Duy on 09-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseEditorActivity extends BaseActivity //for debug
        implements SymbolListView.OnKeyListener,
        EditorControl,
        FileActionCallback,
        EditorContext, View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "BaseEditorActivity";

    protected FileManager mFileManager;
    protected EditorPagerAdapter mPagerAdapter;
    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;
    protected DrawerLayout mDrawerLayout;
    protected SymbolListView mKeyList;
    protected NavigationView mNavigationView;
    protected TabLayout mTabLayout;
    protected View mContainerSymbol;
    protected ViewPager mViewPager;
    protected FloatingActionMenu mFabMenu;
    private KeyBoardEventListener mKeyBoardListener;
    private FileListPagerFragment mFileExplorer;
    private FileClipboard mFileClipboard;
    private PopupMenu mFileMenu;
    private MenuItem mPasteMenu;

    protected void onShowKeyboard() {
        hideAppBar();
    }

    protected void onHideKeyboard() {
        showAppBar();
    }

    /**
     * hide appbar layout when keyboard visible
     */
    private void hideAppBar() {
        mTabLayout.setVisibility(View.GONE);
    }

    /**
     * show appbar layout when keyboard gone
     */
    private void showAppBar() {
        mTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        bindView();
        setupToolbar();
        setupPageView();
        initFileView();
        loadFileFromIntent();
    }

    private void loadFileFromIntent() {
        DLog.d(TAG, "onResume() called");
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(CompileManager.FILE_PATH) != null) {
            String filePath = intent.getStringExtra(CompileManager.FILE_PATH);
            DLog.d(TAG, "onResume: path = " + filePath);
            //No need save last file because it is the first file
            addNewPageEditor(new File(filePath));
            //Remove path
            intent.removeExtra(CompileManager.FILE_PATH);
        }
    }

    private void bindView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mKeyList = findViewById(R.id.recycler_view);
        mFileManager = new FileManager(this);
        mNavigationView = findViewById(R.id.navigation_view);
        mTabLayout = findViewById(R.id.tab_layout);
        mContainerSymbol = findViewById(R.id.container_symbol);
        mViewPager = findViewById(R.id.view_pager);
    }

    private void initFileView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFileExplorer = (FileListPagerFragment) fragmentManager.findFragmentByTag(FileListPagerFragment.TAG);
        if (mFileExplorer == null) {
            LocalFile path = new LocalFile(FileManager.getSrcPath(this));
            mFileExplorer = (FileListPagerFragment) FileListPagerFragment.newFragment(path);
        }
        FragmentTransaction fm = fragmentManager.beginTransaction();
        fm.replace(R.id.file_explorer, mFileExplorer, FileListPagerFragment.TAG).commit();

        mFabMenu = findViewById(R.id.fab_menu);
        mFabMenu.findViewById(R.id.action_new_file).setOnClickListener(this);
        mFabMenu.findViewById(action_new_folder).setOnClickListener(this);

        View menuAnchor = findViewById(R.id.img_file_menus);
        mFileMenu = new PopupMenu(this, menuAnchor);
        mFileMenu.setOnMenuItemClickListener(this);

        Menu menu = mFileMenu.getMenu();
        getMenuInflater().inflate(R.menu.explorer_menu, menu);
        Pref pref = Pref.getInstance(this);
        menu.findItem(R.id.show_hidden_files_menu).setChecked(pref.isShowHiddenFiles());
        mPasteMenu = menu.findItem(R.id.paste_menu);
        int sortId;
        switch (pref.getFileSortType()) {
            case FileListSorter.SORT_DATE:
                sortId = R.id.sort_by_datetime_menu;
                break;
            case FileListSorter.SORT_SIZE:
                sortId = R.id.sort_by_size_menu;
                break;
            case FileListSorter.SORT_TYPE:
                sortId = R.id.sort_by_type_menu;
                break;
            default:
                sortId = R.id.sort_by_name_menu;
                break;
        }
        menu.findItem(sortId).setChecked(true);
        menuAnchor.setOnClickListener(this);

        findViewById(R.id.img_home_dir).setOnClickListener(this);
    }

    protected void setupPageView() {
        ArrayList<File> listFile = TabFileUtils.getTabFiles(this);
        ArrayList<PageDescriptor> pages = new ArrayList<>();
        for (File file : listFile) {
            pages.add(new SimplePageDescriptor(file.getPath(), file.getName()));
        }
        mPagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), pages);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        invalidateTab();

        if (mPagerAdapter.getCount() == 0) {
            String fileName = Integer.toHexString((int) System.currentTimeMillis()) + ".pas";
            String filePath = mFileManager.createNewFileInMode(fileName);
            addNewPageEditor(new File(filePath));
        }

        int pos = getPreferences().getInt(PascalPreferences.TAB_POSITION_FILE);
        if (mPagerAdapter.getCount() > pos) {
            mViewPager.setCurrentItem(pos);
        }
    }

    private void invalidateTab() {
        for (int index = 0; index < mPagerAdapter.getCount(); index++) {
            final TabLayout.Tab tab = mTabLayout.getTabAt(index);
            View view = LayoutInflater.from(this).inflate(R.layout.item_tab_file, null);
            if (tab != null) tab.setCustomView(view);
            View vClose = view.findViewById(R.id.img_close);
            final int position = index;
            vClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removePage(position);
                }
            });
            TextView txtTitle = view.findViewById(R.id.txt_name);
            txtTitle.setText(mPagerAdapter.getPageTitle(index));
            txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(position);
                }
            });
            if (index == mViewPager.getCurrentItem()) {
                if (tab != null) {
                    tab.select();
                }
            }
        }
    }

    protected void setupToolbar() {
        //setup action bar
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.app_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //attach listener hide/show keyboard
        mKeyBoardListener = new KeyBoardEventListener(this);
        mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(mKeyBoardListener);
    }

    /**
     * remove a page in <code>position</code>
     */
    protected void removePage(int position) {
        Fragment existingFragment = mPagerAdapter.getExistingFragment(position);
        if (existingFragment == null) {
            if (DLog.DEBUG) DLog.d(TAG, "removePage: " + "null page " + position);
            return;
        }
        //delete in database
        String filePath = existingFragment.getTag();
        mFileManager.removeTabFile(filePath);

        //remove page
        mPagerAdapter.remove(position);
        invalidateTab();
        String msg = getString(R.string.closed) + " " + new File(filePath).getName();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        invalidateTab();
    }

    /**
     * Add new page for editor
     * Check if not in list file, add it to tab and select tab of file
     *
     * @param file - file need load
     */
    protected void addNewPageEditor(@NonNull File file) {
        DLog.d(TAG, "addNewPageEditor() called with: file = [" + file + "]");
        int position = mPagerAdapter.getPositionForTag(file.getPath());
        if (position != -1) { //existed in list file
            TabLayout.Tab tab = mTabLayout.getTabAt(position);
            if (tab != null) {
                tab.select();
                mViewPager.setCurrentItem(position);
            }
        } else { //new file
            if (mPagerAdapter.getCount() >= getPreferences().getMaxPage()) {
                Fragment existingFragment = mPagerAdapter.getExistingFragment(0);
                if (existingFragment != null) {
                    mFileManager.removeTabFile(existingFragment.getTag());
                    removePage(0);
                }
            }
            //add to database
            mFileManager.addNewPath(file.getPath());

            //new page
            mPagerAdapter.add(new SimplePageDescriptor(file.getPath(), file.getName()));
            invalidateTab();
            int indexOfNewPage = mPagerAdapter.getCount() - 1;
            TabLayout.Tab tab = mTabLayout.getTabAt(indexOfNewPage);
            if (tab != null) {
                tab.select();
                mViewPager.setCurrentItem(indexOfNewPage);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferences().put(PascalPreferences.TAB_POSITION_FILE, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DLog.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
        if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
            String filePath = intent.getStringExtra(CompileManager.FILE_PATH);
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(this, R.string.file_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            addNewPageEditor(file);
            //remove path
            intent.removeExtra(CompileManager.FILE_PATH);
        }
    }

    protected abstract String getCode();

    @Override
    public boolean onSelectFile(@NonNull File file) {
        return false;
    }

    @Override
    public boolean onFileLongClick(@NonNull File file) {
        return false;
    }

    @Override
    public boolean doRemoveFile(@NonNull final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_file_msg) + " " + file.getName());
        builder.setTitle(R.string.delete_file);
        builder.setIcon(R.drawable.ic_delete_forever_white_24dp);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = mPagerAdapter.getPositionForTag(file.getPath());
                boolean success = mFileManager.deleteFile(file);
                if (success) {
                    if (position >= 0) {
                        removePage(position);
                    }
                    Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();

                //reload file
                FileExplorerView controller = (FileExplorerView) getSupportFragmentManager().findFragmentByTag("fragment_file_view");
                if (controller != null) {
                    controller.refresh();
                } else {
                    DLog.d(TAG, "onClick: Fragment file is null");
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

    @NonNull
    @Override
    public FileClipboard getFileClipboard() {
        if (mFileClipboard == null) {
            mFileClipboard = new FileClipboard();
        }
        return mFileClipboard;
    }

    /**
     * @return current file selected
     */
    @Nullable
    @Override
    public File getCurrentFile() {
        EditorFragment editorFragment = mPagerAdapter.getCurrentFragment();
        if (editorFragment != null) {
            String filePath = editorFragment.getFilePath();
            return new File(filePath);
        }
        return null;
    }

    @Override
    public boolean doCompile() {
        return false;
    }

    @Override
    public void saveAs() {
        saveFile();
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
                            try {
                                mFileManager.copy(currentFile.getPath(),
                                        currentFile.getParent() + "/" + fileName);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(BaseEditorActivity.this, R.string.can_not_save_file,
                                        Toast.LENGTH_SHORT).show();
                            }
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

    @Override
    public void findAndReplace() {

    }

    @Override
    public void runProgram() {

    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void saveFile() {

    }

    @Override
    public void showDocumentActivity() {

    }

    @Override
    public void createNewSourceFile(View view) {
        mFileExplorer.createNewFile();
    }

    @Override
    public void goToLine() {

    }

    @Override
    public void formatCode() {

    }

    @Override
    public void reportBug() {

    }

    @Override
    public void openTool() {

    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }

    @Override
    public void paste() {

    }

    @Override
    public void copyAll() {

    }

    @Override
    public void selectThemeFont() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeKeyBoard();
        mDrawerLayout.getViewTreeObserver()
                .removeGlobalOnLayoutListener(mKeyBoardListener);

    }

    // closes the soft keyboard
    protected void closeKeyBoard() throws NullPointerException {
        // Central system API to the overall input method framework (IMF) architecture
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            // Base interface for a remotable object
            IBinder windowToken = currentFocus.getWindowToken();

            // Hide type
            int hideType = InputMethodManager.HIDE_NOT_ALWAYS;

            // Hide the KeyBoard
            inputManager.hideSoftInputFromWindow(windowToken, hideType);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_file_menus:
                mFileMenu.show();
                break;
            case R.id.action_new_file:
                mFileExplorer.createNewFile();
                mFabMenu.close(true);
                break;
            case action_new_folder:
                mFileExplorer.createNewFolder();
                mFabMenu.close(true);
                break;
            case R.id.img_home_dir:
                mFileExplorer.show(FileManager.getSrcPath(this));
                break;
        }
    }

    @Override
    public void onKeyClick(View view, String text) {

    }

    @Override
    public void onKeyLongClick(String text) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        DLog.d(TAG, "onMenuItemClick() called with: item = [" + item + "]");

        Pref pref = Pref.getInstance(this);
        int id = item.getItemId();
        if (id == R.id.show_hidden_files_menu) {
            item.setChecked(!item.isChecked());
            pref.setShowHiddenFiles(item.isChecked());
        } else if (id == R.id.sort_by_name_menu) {
            item.setChecked(true);
            pref.setFileSortType(FileListSorter.SORT_NAME);
        } else if (id == R.id.sort_by_datetime_menu) {
            item.setChecked(true);
            pref.setFileSortType(FileListSorter.SORT_DATE);
        } else if (id == R.id.sort_by_size_menu) {
            item.setChecked(true);
            pref.setFileSortType(FileListSorter.SORT_SIZE);
        } else if (id == R.id.sort_by_type_menu) {
            item.setChecked(true);
            pref.setFileSortType(FileListSorter.SORT_TYPE);
        }
        return mFileExplorer.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the criteria is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    private class KeyBoardEventListener implements ViewTreeObserver.OnGlobalLayoutListener {
        BaseEditorActivity activity;

        KeyBoardEventListener(BaseEditorActivity activityIde) {
            this.activity = activityIde;
        }

        public void onGlobalLayout() {
            int i = 0;
            int navHeight = this.activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            navHeight = navHeight > 0 ? this.activity.getResources().getDimensionPixelSize(navHeight) : 0;
            int statusBarHeight = this.activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (statusBarHeight > 0) {
                i = this.activity.getResources().getDimensionPixelSize(statusBarHeight);
            }
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (activity.mDrawerLayout.getRootView().getHeight() - ((navHeight + i) + rect.height()) <= 0) {
                activity.onHideKeyboard();
            } else {
                activity.onShowKeyboard();
            }
        }
    }
}
