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

package com.duy.pascal.ui.file.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.file.FileActionListener;
import com.duy.pascal.ui.file.FileExplorerView;
import com.duy.pascal.ui.file.FileManager;
import com.duy.pascal.ui.file.PreferenceHelper;
import com.duy.pascal.ui.file.adapter.FileAdapterListener;
import com.duy.pascal.ui.file.adapter.FileDetail;
import com.duy.pascal.ui.file.adapter.FileListAdapter;
import com.duy.pascal.ui.utils.Build;
import com.github.clans.fab.FloatingActionMenu;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

//import butterknife.OnClick;


/**
 * Created by Duy on 15-Mar-17.
 */
@Deprecated
public class FragmentFileManager extends Fragment implements
        View.OnClickListener, View.OnLongClickListener,
        SwipeRefreshLayout.OnRefreshListener, FileAdapterListener, SearchView.OnQueryTextListener,
        FileExplorerView {

    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_SIZE = 2;
    private static final int SORT_BY_DATE = 3;
    private final Handler handler = new Handler();
    private FileActionListener mListener;
    private FloatingActionMenu mFabMenu;
    private RecyclerView mRecycleView;
    private String mCurrentFolder;
    private boolean mWantAFile = true;
    private SearchView mSearchView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mSortMode = SORT_BY_NAME;
    @Nullable
    private FileListAdapter mAdapter;
    private String mQueryText = "";
    private final Runnable searchHandler = new Runnable() {
        @Override
        public void run() {
            if (mAdapter != null) {
                mAdapter.query(mQueryText);
            }
        }
    };
    private TextView mTxtPath;

    public void sortBySize() {
        this.mSortMode = SORT_BY_SIZE;
        mSwipeRefreshLayout.setRefreshing(true);
        Toast.makeText(getContext(), R.string.sort_size, Toast.LENGTH_SHORT).show();
        new UpdateList(mCurrentFolder).execute();
    }

    public void sortByName() {
        this.mSortMode = SORT_BY_NAME;
        mSwipeRefreshLayout.setRefreshing(true);
        Toast.makeText(getContext(), R.string.sort_name, Toast.LENGTH_SHORT).show();
        new UpdateList(mCurrentFolder).execute();
    }

    public void sortByDate(View view) {
        this.mSortMode = SORT_BY_DATE;
        mSwipeRefreshLayout.setRefreshing(true);
        Toast.makeText(getContext(), R.string.sort_date, Toast.LENGTH_SHORT).show();
        new UpdateList(mCurrentFolder).execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (FileActionListener) getActivity();
        } catch (Exception ignored) {
            mListener = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCurrentFolder = FileManager.getSrcPath(getContext()).getPath();
        mWantAFile = true; //action == Actions.SelectFile;

        bindView(view);

        //load file
        new UpdateList(mCurrentFolder).execute();
    }

    private void bindView(View view) {
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);

        mRecycleView = view.findViewById(R.id.list_file);
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mSwipeRefreshLayout = view.findViewById(R.id.refresh_view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.color_key_word_color));

        mFabMenu = view.findViewById(R.id.fab_menu);
        mFabMenu.findViewById(R.id.action_new_file).setOnClickListener(this);
        mFabMenu.findViewById(R.id.action_new_folder).setOnClickListener(this);

        view.findViewById(R.id.img_sort_name).setOnClickListener(this);
        view.findViewById(R.id.img_sort_date).setOnClickListener(this);
        view.findViewById(R.id.img_sort_size).setOnClickListener(this);

        mTxtPath = view.findViewById(R.id.txt_path);
    }


    /**
     * show dialog create new file
     */
    @Override
    public void createNewFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.new_file);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = alertDialog.findViewById(R.id.edit_input);
        Button btnOK = alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel);
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }

                RadioButton checkBoxPas = alertDialog.findViewById(R.id.rad_program);
                RadioButton checkBoxInp = alertDialog.findViewById(R.id.rad_inp);

                if (checkBoxInp != null && checkBoxInp.isChecked()) fileName += ".inp";
                else if (checkBoxPas.isChecked()) fileName += ".pas";

                //create new file
                File file = new File(mCurrentFolder, fileName);
                try {
                    file.createNewFile();
                    new UpdateList(mCurrentFolder).execute();
                } catch (IOException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onPrepareDeleteFile(File file) {

    }

    @Override
    public void show(File file) {

    }

    public void createNewFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.new_folder);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = alertDialog.findViewById(R.id.edit_input);
        final TextInputLayout textInputLayout = alertDialog.findViewById(R.id.hint);
        
        textInputLayout.setHint(getString(R.string.enter_new_folder_name));
        Button btnOK = alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = alertDialog.findViewById(R.id.btn_cancel);
        
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText != null ? editText.getText().toString() : null;
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }
                //create new file
                File file = new File(mCurrentFolder, fileName);
                file.mkdirs();
                new UpdateList(mCurrentFolder).execute();
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.action_new_file:
                createNewFile();
                mFabMenu.close(true);
                break;
            case R.id.action_new_folder:
                createNewFolder();
                mFabMenu.close(true);
                break;
            case R.id.img_sort_date:
                sortByDate(v);
                break;
            case R.id.img_sort_name:
                sortByName();
                break;
            case R.id.img_sort_size:
                sortBySize();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onRefresh() {
        new UpdateList(mCurrentFolder).execute();
    }

    @Override
    public void onItemClick(View v, String name, int action) {
        if (action == ACTION_LONG_CLICK) {
            if (name.equals("..")) {
                if (mCurrentFolder.equals("/")) {
                    new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
                } else {
                    File tempFile = new File(mCurrentFolder);
                    if (tempFile.isFile()) {
                        tempFile = tempFile.getParentFile()
                                .getParentFile();
                    } else {
                        tempFile = tempFile.getParentFile();
                    }
                    new UpdateList(tempFile.getAbsolutePath()).execute();
                }
            } else if (name.equals(getString(R.string.home))) {
                // TODO: 14-Mar-17
                new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
            }

            final File selectedFile = new File(mCurrentFolder, name);

            if (selectedFile.isFile() && mWantAFile) {
                // TODO: 15-Mar-17
                if (mListener != null) mListener.onFileLongClick(selectedFile);
            } else if (selectedFile.isDirectory()) {
//            new UpdateList().execute(selectedFile.getAbsolutePath());
            }
        } else if (action == ACTION_CLICK) {
            if (name.equals("..")) {
                if (mCurrentFolder.equals("/")) {
                    new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
                } else {
                    File tempFile = new File(mCurrentFolder);
                    if (tempFile.isFile()) {
                        tempFile = tempFile.getParentFile()
                                .getParentFile();
                    } else {
                        tempFile = tempFile.getParentFile();
                    }
                    new UpdateList(tempFile.getAbsolutePath()).execute();
                }
                return;
            } else if (name.equals(getString(R.string.home))) {
                // TODO: 14-Mar-17
                new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
                return;
            }

            final File selectedFile = new File(mCurrentFolder, name);

            if (selectedFile.isFile() && mWantAFile) {
                // TODO: 15-Mar-17
                if (mListener != null) mListener.onFileSelected(selectedFile);
            } else if (selectedFile.isDirectory()) {
                new UpdateList(selectedFile.getAbsolutePath()).execute();
            }
        }
    }

    private void doRemoveFile(final File file) {
        mListener.doRemoveFile(file);
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void setSelectAll(boolean checked) {

    }

    @Override
    public void refresh() {
        new UpdateList(mCurrentFolder).execute();
    }

    @Override
    public void finish() {

    }

    @Override
    public void onRemoveClick(View view, String name, int action) {
        if (name.equals("..")) {
            if (mCurrentFolder.equals("/")) {
                new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
            } else {
                File tempFile = new File(mCurrentFolder);
                if (tempFile.isFile()) {
                    tempFile = tempFile.getParentFile()
                            .getParentFile();
                } else {
                    tempFile = tempFile.getParentFile();
                }
                new UpdateList(tempFile.getAbsolutePath()).execute();
            }
            return;
        } else if (name.equals(getString(R.string.home))) {
            // TODO: 14-Mar-17
            new UpdateList(PreferenceHelper.getWorkingFolder(getContext())).execute();
            return;
        }

        File selectedFile = new File(mCurrentFolder, name);
        doRemoveFile(selectedFile);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.mQueryText = newText;
        handler.removeCallbacks(searchHandler);
        handler.postDelayed(searchHandler, 100);
        return true;
    }

    private class UpdateList extends AsyncTask<Void, Void, LinkedList<FileDetail>> {
        private String path;
        private String exceptionMessage;

        public UpdateList(@NonNull String path) {
            this.path = path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTxtPath.setText(path);
            if (mSearchView != null) {
                mSearchView.setIconified(true);
                mSearchView.setQuery("", false);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected LinkedList<FileDetail> doInBackground(final Void... params) {
            try {
                if (TextUtils.isEmpty(path)) {
                    return null;
                }

                File tempFolder = new File(path);
                if (tempFolder.isFile()) {
                    tempFolder = tempFolder.getParentFile();
                }

                String[] canOpen = {"txt", "pas", "nide", "inp", "out"};

                final LinkedList<FileDetail> fileDetails = new LinkedList<>();
                final LinkedList<FileDetail> folderDetails = new LinkedList<>();
                mCurrentFolder = tempFolder.getAbsolutePath();

                if (!tempFolder.canRead()) {

                } else {
                    File[] files = tempFolder.listFiles();
                    if (mSortMode == SORT_BY_SIZE) {
                        Arrays.sort(files, getFileSizeComparator());
                    } else if (mSortMode == SORT_BY_NAME) {
                        Arrays.sort(files, getFileNameComparator());
                    } else if (mSortMode == SORT_BY_DATE) {
                        Arrays.sort(files, getFileDateComparator());
                    }

                    for (final File f : files) {
                        if (f.isDirectory() && !f.getName().equalsIgnoreCase("fonts")
                                && !f.isHidden()) {
                            folderDetails.add(new FileDetail(f.getName(), getString(R.string.folder), ""));
                        } else if (f.isFile()
                                && FilenameUtils.isExtension(f.getName().toLowerCase(), canOpen)
                                && FileUtils.sizeOf(f) <= Build.MAX_FILE_SIZE * FileUtils.ONE_KB
                                && !f.isHidden()) {
                            final long fileSize = f.length();
                            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a", Locale.getDefault());
                            String date = format.format(f.lastModified());
                            fileDetails.add(new FileDetail(f.getName(),
                                    FileUtils.byteCountToDisplaySize(fileSize), date));
                        }
                    }
                }

                folderDetails.addAll(fileDetails);
                return folderDetails;
            } catch (Exception e) {
                exceptionMessage = e.getMessage();
                return null;
            }
        }

        private Comparator<? super File> getFileNameComparator() {
            return new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }

        private Comparator<? super File> getFileDateComparator() {
            return new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.lastModified() == o2.lastModified()) {
                        return 0;
                    }
                    if (o1.lastModified() > o2.lastModified()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            };
        }

        private Comparator<? super File> getFileSizeComparator() {
            return new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.length() == o2.length()) {
                        return 0;
                    }
                    if (o1.length() > o2.length()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            };
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(final LinkedList<FileDetail> names) {
            if (names != null) {
                boolean isRoot = mCurrentFolder.equals("/");
                mAdapter = new FileListAdapter(getContext(), names, isRoot, FragmentFileManager.this);
                mRecycleView.setAdapter(mAdapter);
            }
            if (exceptionMessage != null) {
                Toast.makeText(getContext(), exceptionMessage, Toast.LENGTH_SHORT).show();
            }
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
            super.onPostExecute(names);
        }

    }
}
