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

package com.duy.pascal.frontend.code_editor;

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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.pager.PageDescriptor;
import com.commonsware.cwac.pager.SimplePageDescriptor;
import com.duy.pascal.frontend.Dlog;
import com.duy.pascal.frontend.EditorControl;
import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.activities.AbstractAppCompatActivity;
import com.duy.pascal.frontend.code.CompileManager;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.file.FileActionListener;
import com.duy.pascal.frontend.file.FragmentFileManager;
import com.duy.pascal.frontend.file.TabFileUtils;
import com.duy.pascal.frontend.setting.PascalPreferences;
import com.duy.pascal.frontend.view.SymbolListView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duy on 09-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public abstract class BaseEditorActivity extends AbstractAppCompatActivity //for debug
        implements SymbolListView.OnKeyListener,
        EditorControl, FileActionListener {
    protected final static String TAG = BaseEditorActivity.class.getSimpleName();
    protected final boolean SELECT = true;
    protected final boolean SAVE_LAST_FILE = true;
    protected final boolean UN_SELECT = false;
    protected final boolean UN_SAVE_LAST_FILE = false;
    protected ApplicationFileManager mFileManager;
    protected EditorPagerAdapter pagerAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.recycler_view)
    SymbolListView mKeyList;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.container_symbol)
    View mContainerSymbol;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private KeyBoardEventListener keyBoardListener;

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
        tabLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
    }

    /**
     * show appbar layout when keyboard gone
     */
    private void showAppBar() {
        tabLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        mFileManager = new ApplicationFileManager(this);
        setupActionBar();
        setupPageView();
        this.keyBoardListener = new KeyBoardEventListener(this);
        mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyBoardListener);
    }

    protected void setupPageView() {
        ArrayList<File> listFile = TabFileUtils.getTabFiles(this);
        ArrayList<PageDescriptor> pages = new ArrayList<>();
        for (File file : listFile) {
            pages.add(new SimplePageDescriptor(file.getPath(), file.getName()));
        }
        pagerAdapter = new EditorPagerAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        invalidateTab();

        if (pagerAdapter.getCount() == 0) {
            String fileName = Integer.toHexString((int) System.currentTimeMillis());
            String filePath = mFileManager.createNewFileInMode(fileName);
            addNewPageEditor(new File(filePath), SELECT, UN_SAVE_LAST_FILE);
        }

        int pos = mPascalPreferences.getInt(PascalPreferences.TAB_POSITION_FILE);
        if (pagerAdapter.getCount() > pos) {
            viewPager.setCurrentItem(pos);
        }
    }

    private void invalidateTab() {
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            final TabLayout.Tab tab = tabLayout.getTabAt(i);
            View view = null;
            if (tab != null) {
                tab.setCustomView(R.layout.item_tab_file);
                view = tab.getCustomView();
            }

            if (view != null) {
                View vClose = view.findViewById(R.id.img_close);
                final int position = i;
                vClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removePage(position, SAVE_LAST_FILE);
                    }
                });
                TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
                txtTitle.setText(pagerAdapter.getPageTitle(i));
                txtTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(position);
                    }
                });
            }

            if (i == viewPager.getCurrentItem()) {
                if (tab != null) {
                    tab.select();
                }
            }
        }
    }

    private void setupActionBar() {
        //setup action bar
        setSupportActionBar(toolbar);
        setTitle("");
/*
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();*/
    }

    protected void removePage(int position, boolean saveLastFile) {
        Fragment existingFragment = pagerAdapter.getExistingFragment(position);
        if (existingFragment == null) {
            if (Dlog.DEBUG) Log.d(TAG, "removePage: " + "null page " + position);
            return;
        }
        //save file and remove tab entry
        if (saveLastFile) {
            saveFile();
        }
        //delete in database
        String filePath = existingFragment.getTag();
        mFileManager.removeTabFile(filePath);

        //remove page
        pagerAdapter.remove(position);
        invalidateTab();
        Toast.makeText(this, getString(R.string.closed) + " " + filePath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Add new page for editor
     * Check if not in list file, add it to tab and select tab of file
     *
     * @param file          - file need load
     * @param selectNewPage - if <code>true</code>, the tab of file will be selected when initialized
     * @param saveLastFile  - if <code>true</code>, save last file
     */
    protected void addNewPageEditor(@NonNull File file, boolean selectNewPage, boolean saveLastFile) {
        int position = pagerAdapter.getPositionForTag(file.getPath());
        if (position != -1) { //existed in list file
            //check need select tab
            if (selectNewPage) {
                if (saveLastFile) {
                    EditorFragment currentFragment = pagerAdapter.getCurrentFragment();
                    if (currentFragment != null) {
                        currentFragment.saveFile();
                    }
                }
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if (tab != null) {
                    tab.select();
                    viewPager.setCurrentItem(position);
                }
            }
        } else { //new file
            if (saveLastFile) {
                EditorFragment currentFragment = pagerAdapter.getCurrentFragment();
                if (currentFragment != null) {
                    currentFragment.saveFile();
                }
            }

            if (pagerAdapter.getCount() >= mPascalPreferences.getMaxPage()) {
                Fragment existingFragment = pagerAdapter.getExistingFragment(0);
                if (existingFragment != null) {
                    mFileManager.removeTabFile(existingFragment.getTag());
                    removePage(0, UN_SAVE_LAST_FILE);
                }
            }

            //add to database
            mFileManager.addNewPath(file.getPath());

            //new page
            pagerAdapter.add(new SimplePageDescriptor(file.getPath(), file.getName()));
            invalidateTab();

            if (selectNewPage) {
                int indexOfNewPage = pagerAdapter.getCount() - 1;
                TabLayout.Tab tab = tabLayout.getTabAt(indexOfNewPage);
                if (tab != null) {
                    tab.select();
                    viewPager.setCurrentItem(indexOfNewPage);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPascalPreferences.put(PascalPreferences.TAB_POSITION_FILE, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
                String filePath = intent.getStringExtra(CompileManager.FILE_PATH);
                //No need save last file because it is the frist file
                addNewPageEditor(new File(filePath), SELECT, UN_SAVE_LAST_FILE);
                //Remove path
                intent.removeExtra(CompileManager.FILE_PATH);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra(CompileManager.FILE_PATH) != null) {
            String filePath = intent.getStringExtra(CompileManager.FILE_PATH);
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                return;
            }
            addNewPageEditor(file, SELECT, UN_SAVE_LAST_FILE);
            //remove path
            intent.removeExtra(CompileManager.FILE_PATH);
        }
    }

    protected abstract String getCode();

    /**
     * delete a file
     *
     * @param file - file need delete
     * @return true if the file is deleted
     */
    @Override
    public boolean doRemoveFile(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_file_msg) + file.getName());
        builder.setTitle(R.string.delete_file);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = pagerAdapter.getPositionForTag(file.getPath());
                boolean success = mFileManager.deleteFile(file);
                if (success) {
                    if (position >= 0) {
                        removePage(position, false);
                    }
                    Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();

                //reload file
                FragmentFileManager fragmentSelectFile =
                        (FragmentFileManager) getSupportFragmentManager().findFragmentByTag("fragment_file_view");
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

    /**
     * @return current file selected
     */
    @Nullable
    protected File getCurrentFile() {
        EditorFragment editorFragment = pagerAdapter.getCurrentFragment();
        if (editorFragment != null) {
            String filePath = editorFragment.getFilePath();
            return new File(filePath);
        }
        return null;
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
//                            mFileManager.saveFile(getCurrentFile().getParent() + "/" + fileName, mCodeEditor.getCleanText());
                        } else {
//                            mFileManager.saveFile(mFileManager.createNewFileInMode(fileName), mCodeEditor.getCleanText());
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
    protected void onDestroy() {
        super.onDestroy();
        closeKeyBoard();
        mDrawerLayout.getViewTreeObserver()
                .removeGlobalOnLayoutListener(keyBoardListener);

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

            // Hide operator
            int hideType = InputMethodManager.HIDE_NOT_ALWAYS;

            // Hide the KeyBoard
            inputManager.hideSoftInputFromWindow(windowToken, hideType);
        }
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
