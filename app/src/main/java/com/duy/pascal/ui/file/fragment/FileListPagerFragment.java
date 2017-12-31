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
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.common.listeners.OnItemClickListener;
import com.duy.pascal.ui.common.task.JecAsyncTask;
import com.duy.pascal.ui.common.task.TaskListener;
import com.duy.pascal.ui.common.task.TaskResult;
import com.duy.pascal.ui.common.utils.UIUtils;
import com.duy.pascal.ui.databinding.FileExplorerFragmentBinding;
import com.duy.pascal.ui.editor.EditorActivity;
import com.duy.pascal.ui.file.ExplorerContext;
import com.duy.pascal.ui.file.FileActionListener;
import com.duy.pascal.ui.file.FileClipboard;
import com.duy.pascal.ui.file.FileExplorerAction;
import com.duy.pascal.ui.file.FileExplorerView;
import com.duy.pascal.ui.file.Pref;
import com.duy.pascal.ui.file.activities.FileExplorerActivity;
import com.duy.pascal.ui.file.adapter.FileListItemAdapter;
import com.duy.pascal.ui.file.adapter.PathButtonAdapter;
import com.duy.pascal.ui.file.listener.OnClipboardPasteFinishListener;
import com.duy.pascal.ui.file.util.FileListSorter;
import com.duy.pascal.ui.utils.DLog;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class FileListPagerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, FileExplorerView, ExplorerContext, SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String TAG = "FileListPagerFragment";
    private FileListItemAdapter mAdapter;
    @Nullable
    private File mPath;
    private FileExplorerFragmentBinding binding;
    private PathButtonAdapter mPathAdapter;
    private ScanFilesTask mTask;
    private FileExplorerAction mAction;

    public static Fragment newFragment(File path) {
        DLog.d(TAG, "newFragment() called with: path = [" + path + "]");

        FileListPagerFragment f = new FileListPagerFragment();
        Bundle b = new Bundle();
        b.putSerializable("path", path);
        f.setArguments(b);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPath = (File) getArguments().getSerializable("path");
        binding = DataBindingUtil.inflate(inflater, R.layout.file_explorer_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FileClipboard fileClipboard = ((FileActionListener) getActivity()).getFileClipboard();
        mAction = new FileExplorerAction(getContext(), this, fileClipboard, this);
        mAdapter = new FileListItemAdapter();
        mAdapter.setOnCheckedChangeListener(mAction);
        mAdapter.setOnItemClickListener(this);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                binding.emptyLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.emptyLayout.setVisibility(mAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
                    }
                });

            }
        });

        binding.pathScrollView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mPathAdapter = new PathButtonAdapter();
        mPathAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                binding.pathScrollView.scrollToPosition(mPathAdapter.getItemCount() - 1);
            }
        });
        mPathAdapter.setPath(mPath);
        mPathAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                File file = mPathAdapter.getItem(position);
                switchToPath(file);
            }

            @Override
            public boolean onItemLongClick(int position, View view) {
                return false;
            }
        });
        binding.pathScrollView.setAdapter(mPathAdapter);

        binding.explorerSwipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).margin(getResources().getDimensionPixelSize(R.dimen.file_list_item_divider_left_margin), 0).build());
        binding.explorerSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.explorerSwipeRefreshLayout.setRefreshing(true);
            }
        });
        binding.nameFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        onRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        pref.unregisterOnSharedPreferenceChangeListener(this);
        if (mAction != null) {
            mAction.destroy();
        }
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTask != null) {
            mTask.cancel(true);
            mTask = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.paste_menu) {
            final FileClipboard fileClipboard = ((FileExplorerActivity) getActivity()).getFileClipboard();
            fileClipboard.paste(getContext(), getCurrentDirectory(), new OnClipboardPasteFinishListener() {
                @Override
                public void onFinish(int count, String error) {
                    onRefresh();
                    fileClipboard.showPasteResult(getContext(), count, error);
                }
            });
            item.setVisible(false);
        } else if (item.getItemId() == R.id.add_folder_menu) {
            mAction.doCreateFolder(null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        DLog.d(TAG, "onRefresh() called");

        UpdateRootInfo updateRootInfo = new UpdateRootInfo() {
            @Override
            public void onUpdate(File f) {
                mPath = f;
            }
        };
        mTask = new ScanFilesTask(getActivity(), mPath, updateRootInfo);
        mTask.setTaskListener(new TaskListener<File[]>() {
            @Override
            public void onCompleted() {
                if (binding.explorerSwipeRefreshLayout != null) {
                    binding.explorerSwipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.explorerSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 300);
                }
            }

            @Override
            public void onSuccess(File[] result) {
                if (mAdapter != null) {
                    mAdapter.setData(result);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                if (binding.explorerSwipeRefreshLayout == null) {
                    return;
                }
                binding.explorerSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.explorerSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 300);
                UIUtils.toast(getContext(), e);
            }
        });
        mTask.execute();
    }

    @Override
    public void onItemClick(int position, View view) {
        try {
            File file = mAdapter.getItem(position);
            FileActionListener callback = (FileActionListener) getActivity();
            if (!callback.onFileSelected(new File(file.getPath()))) {
                if (file.isDirectory()) {
                    switchToPath(file);
                }
            }
        } catch (ClassCastException e) {
        }
    }

    @Override
    public boolean onItemLongClick(int position, View view) {
//        try {
//            File file = adapter.getItem(position);
//            FileActionListener callback = (FileActionListener) getActivity();
//            return callback.onFileLongClick(new File(file.getPath()));
//        } catch (ClassCastException ignored) {
//        }
        return false;
    }

    public boolean onBackPressed() {
        File parent = mPath.getParentFile();
        if (parent == null || parent.getPath().startsWith(mPath.getPath())) {
            switchToPath(parent);
            return true;
        }
        return false;
    }

    private void switchToPath(File file) {
        mPath = file;
        mPathAdapter.setPath(file);
        Pref.getInstance(getContext()).setLastOpenPath(file.getPath());
        onRefresh();
    }

    @Override
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return ((AppCompatActivity) getActivity()).startSupportActionMode(callback);
    }

    @Override
    public void setSelectAll(boolean checked) {
        mAdapter.checkAll(checked);
    }

    @Override
    public void refresh() {
        onRefresh();
    }

    @Override
    public void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void createNewFolder() {
        mAction.doCreateFolder((FileActionListener) getActivity());
    }

    @Override
    public void createNewFile() {
        mAction.showDialogCreateFile((FileActionListener) getActivity());
    }

    @Override
    public void onPrepareDeleteFile(File file) {
        EditorActivity activity = (EditorActivity) getActivity();
        if (activity != null) {
            activity.onPrepareDeleteFile(file);
        }
    }

    @Override
    public void show(File file) {
        mPath = file;
        onRefresh();
    }

    @Override
    public File getCurrentDirectory() {
        return mPath;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "show_hidden_files":
            case "show_file_sort":
                onRefresh();
                break;
        }
    }

    private interface UpdateRootInfo {
        void onUpdate(File path);
    }

    private static class ScanFilesTask extends JecAsyncTask<Void, Void, File[]> {
        private UpdateRootInfo mUpdateRootInfo;
        private Context mContext;
        private File mPath;

        private ScanFilesTask(Context context, File path, UpdateRootInfo updateRootInfo) {
            this.mContext = context.getApplicationContext();
            this.mPath = path;
            this.mUpdateRootInfo = updateRootInfo;
        }

        @Override
        protected void onRun(final TaskResult<File[]> taskResult, Void... params) throws Exception {
            Pref pref = Pref.getInstance(mContext);
            final boolean showHiddenFiles = pref.isShowHiddenFiles();
            final int sortType = pref.getFileSortType();
            mUpdateRootInfo.onUpdate(mPath);
            File[] result = mPath.listFiles();
            if (result.length == 0) {
                taskResult.setResult(result);
                return;
            }
            if (!showHiddenFiles) {
                List<File> list = new ArrayList<>(result.length);
                for (File file : result) {
                    if (file.getName().charAt(0) == '.') {
                        continue;
                    }
                    list.add(file);
                }
                result = new File[list.size()];
                list.toArray(result);
            }
            Arrays.sort(result, new FileListSorter(true, sortType, true));
            taskResult.setResult(result);
        }
    }
}
