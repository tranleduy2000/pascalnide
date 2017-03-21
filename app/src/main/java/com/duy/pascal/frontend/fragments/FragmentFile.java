package com.duy.pascal.frontend.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.adapters.AdapterDetailedList;
import com.duy.pascal.frontend.file.FileListener;
import com.duy.pascal.frontend.file.FileManager;
import com.duy.pascal.frontend.utils.AlphanumComparator;
import com.duy.pascal.frontend.utils.Build;
import com.duy.pascal.frontend.utils.PreferenceHelper;
import com.github.clans.fab.FloatingActionMenu;
import com.spazedog.lib.rootfw4.RootFW;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Created by Duy on 15-Mar-17.
 */

public class FragmentFile extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FileListener listener;
    private FloatingActionMenu fabMenu;
    private ListView listFiles;
    private Activity activity;
    private View root;
    private String currentFolder;
    private boolean wantAFile = true;
    private MenuItem mSearchViewMenuItem;
    private SearchView mSearchView;
    private Filter filter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onResume() {
        super.onResume();
        if (activity == null) activity = getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        try {
            listener = (FileListener) activity;
        } catch (Exception ignored) {
            listener = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_file_view, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentFolder = FileManager.getApplicationPath();
        wantAFile = true; //action == Actions.SelectFile;
        listFiles = (ListView) root.findViewById(R.id.list_file);
        listFiles.setOnItemClickListener(this);
        listFiles.setOnItemLongClickListener(this);
        listFiles.setTextFilterEnabled(true);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.key_word_color));
        fabMenu = (FloatingActionMenu) root.findViewById(R.id.fab_menu);
        fabMenu.findViewById(R.id.action_new_file).setOnClickListener(this);
        fabMenu.findViewById(R.id.action_new_folder).setOnClickListener(this);
        new UpdateList().execute(currentFolder);
    }

    public boolean onQueryTextChange(String newText) {
        if (filter == null)
            return true;
        if (TextUtils.isEmpty(newText)) {
            filter.filter(null);
        } else {
            filter.filter(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        if (name.equals("..")) {
            if (currentFolder.equals("/")) {
                new UpdateList().execute(PreferenceHelper.getWorkingFolder(activity));
            } else {
                File tempFile = new File(currentFolder);
                if (tempFile.isFile()) {
                    tempFile = tempFile.getParentFile()
                            .getParentFile();
                } else {
                    tempFile = tempFile.getParentFile();
                }
                new UpdateList().execute(tempFile.getAbsolutePath());
            }
            return;
        } else if (name.equals(getString(R.string.home))) {
            // TODO: 14-Mar-17
            new UpdateList().execute(PreferenceHelper.getWorkingFolder(activity));
            return;
        }

        final File selectedFile = new File(currentFolder, name);

        if (selectedFile.isFile() && wantAFile) {
            // TODO: 15-Mar-17
            if (listener != null) listener.onFileClick(selectedFile);
        } else if (selectedFile.isDirectory()) {
            new UpdateList().execute(selectedFile.getAbsolutePath());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mSearchViewMenuItem = menu.findItem(R.id.im_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchViewMenuItem);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
//        if (i == android.R.id.home) {
//
//            return true;
//        } else if (i == R.id.im_select_folder) {
//            finishWithResult(new File(currentFolder));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * show dialog create new file
     */
    private void createNewFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.new_file);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = (EditText) alertDialog.findViewById(R.id.edit_file_name);
        Button btnOK = (Button) alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert btnOK != null;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }

                RadioButton checkBoxPas = (RadioButton) alertDialog.findViewById(R.id.rad_pas);
                RadioButton checkBoxInp = (RadioButton) alertDialog.findViewById(R.id.rad_inp);

                if (checkBoxInp.isChecked()) fileName += ".inp";
                else if (checkBoxPas.isChecked()) fileName += ".pas";

                //create new file
                File file = new File(currentFolder, fileName);
                try {
                    file.createNewFile();
                    new UpdateList().execute(currentFolder);
                } catch (IOException e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }
        });

    }


    private void createNewFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.new_folder);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = (EditText) alertDialog.findViewById(R.id.edit_file_name);
        final TextInputLayout textInputLayout = (TextInputLayout) alertDialog.findViewById(R.id.hint);
        textInputLayout.setHint(getString(R.string.enter_new_folder_name));
        Button btnOK = (Button) alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert btnOK != null;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }
                //create new file
                File file = new File(currentFolder, fileName);
                file.mkdirs();
                new UpdateList().execute(currentFolder);
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_new_file:
                createNewFile();
                fabMenu.close(true);
                break;
            case R.id.action_new_folder:
                createNewFolder();
                fabMenu.close(true);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        if (name.equals("..")) {
            if (currentFolder.equals("/")) {
                new UpdateList().execute(PreferenceHelper.getWorkingFolder(activity));
            } else {
                File tempFile = new File(currentFolder);
                if (tempFile.isFile()) {
                    tempFile = tempFile.getParentFile()
                            .getParentFile();
                } else {
                    tempFile = tempFile.getParentFile();
                }
                new UpdateList().execute(tempFile.getAbsolutePath());
            }
            return false;
        } else if (name.equals(getString(R.string.home))) {
            // TODO: 14-Mar-17
            new UpdateList().execute(PreferenceHelper.getWorkingFolder(activity));
            return false;
        }

        final File selectedFile = new File(currentFolder, name);

        if (selectedFile.isFile() && wantAFile) {
            // TODO: 15-Mar-17
            if (listener != null) listener.onFileLongClick(selectedFile);
        } else if (selectedFile.isDirectory()) {
//            new UpdateList().execute(selectedFile.getAbsolutePath());
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new UpdateList().execute(currentFolder);
    }

    private class UpdateList extends AsyncTask<String, Void, LinkedList<AdapterDetailedList.FileDetail>> {

        String exceptionMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mSearchView != null) {
                mSearchView.setIconified(true);
                MenuItemCompat.collapseActionView(mSearchViewMenuItem);
                mSearchView.setQuery("", false);
            }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected LinkedList<AdapterDetailedList.FileDetail> doInBackground(final String... params) {
            try {

                final String path = params[0];
                if (TextUtils.isEmpty(path)) {
                    return null;
                }

                File tempFolder = new File(path);
                if (tempFolder.isFile()) {
                    tempFolder = tempFolder.getParentFile();
                }

                String[] unopenableExtensions = {"apk", "mp3", "mp4", "png", "jpg", "jpeg"};

                final LinkedList<AdapterDetailedList.FileDetail> fileDetails = new LinkedList<>();
                final LinkedList<AdapterDetailedList.FileDetail> folderDetails = new LinkedList<>();
                currentFolder = tempFolder.getAbsolutePath();

                if (!tempFolder.canRead()) {
                    if (RootFW.connect()) {
                        com.spazedog.lib.rootfw4.utils.File folder = RootFW.getFile(currentFolder);
                        com.spazedog.lib.rootfw4.utils.File.FileStat[] stats = folder.getDetailedList();

                        if (stats != null) {
                            for (com.spazedog.lib.rootfw4.utils.File.FileStat stat : stats) {
                                if (stat.type().equals("d")) {
                                    folderDetails.add(new AdapterDetailedList.FileDetail(stat.name(),
                                            getString(R.string.folder),
                                            ""));
                                } else if (!FilenameUtils.isExtension(stat.name().toLowerCase(), unopenableExtensions)
                                        && stat.size() <= Build.MAX_FILE_SIZE * FileUtils.ONE_KB) {
                                    final long fileSize = stat.size();
                                    //SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
                                    //String date = format.format("");
                                    fileDetails.add(new AdapterDetailedList.FileDetail(stat.name(),
                                            FileUtils.byteCountToDisplaySize(fileSize), ""));
                                }
                            }
                        }
                    }
                } else {
                    File[] files = tempFolder.listFiles();

                    Arrays.sort(files, getFileNameComparator());

                    if (files != null) {
                        for (final File f : files) {
                            if (f.isDirectory()) {
                                folderDetails.add(new AdapterDetailedList.FileDetail(f.getName(),
                                        getString(R.string.folder),
                                        ""));
                            } else if (f.isFile()
                                    && !FilenameUtils.isExtension(f.getName().toLowerCase(), unopenableExtensions)
                                    && FileUtils.sizeOf(f) <= Build.MAX_FILE_SIZE * FileUtils.ONE_KB) {
                                final long fileSize = f.length();
                                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
                                String date = format.format(f.lastModified());
                                fileDetails.add(new AdapterDetailedList.FileDetail(f.getName(),
                                        FileUtils.byteCountToDisplaySize(fileSize), date));
                            }
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

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(final LinkedList<AdapterDetailedList.FileDetail> names) {
            if (names != null) {
                boolean isRoot = currentFolder.equals("/");
                AdapterDetailedList mAdapter = new AdapterDetailedList(activity, names, isRoot);
                listFiles.setAdapter(mAdapter);
                filter = mAdapter.getFilter();
            }
            if (exceptionMessage != null) {
                Toast.makeText(activity, exceptionMessage, Toast.LENGTH_SHORT).show();
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            super.onPostExecute(names);
        }

        final AlphanumComparator getFileNameComparator() {
            return new AlphanumComparator() {
                /**
                 * {@inheritDoc}
                 */
                @Override
                public String getTheString(Object obj) {
                    return ((File) obj).getName().toLowerCase();
                }
            };
        }
    }
}
