package com.duy.pascal.compiler.file_manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Duy on 11-Feb-17.
 */

public class FileListView extends RecyclerView {
    private static final String TAG = FileListView.class.getSimpleName();
    /**
     * handler
     */
    private Handler handler = new Handler();
    private FileAdapter mFileAdapter;
    private FileManager mFileManager;
    private Database mDatabase;

    public FileListView(Context context) {
        super(context);
        setup(context);

    }

    public FileListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public FileListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    /**
     * set up recycle view, include layout manager, adapter , listener
     *
     * @param context - android context
     */
    private void setup(Context context) {
        /**
         * if in edit, do not init view
         */
        if (isInEditMode()) return;
        mFileManager = new FileManager(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(linearLayoutManager);
        setHasFixedSize(true);
        mDatabase = new Database(context);
        mFileAdapter = new FileAdapter(context);
        setAdapter(mFileAdapter);
        reload();
    }

    public void setListener(FileListener fileListener) {
        mFileAdapter.setListener(fileListener);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        return true;
//    }

    public void reload() {
        new LoadFileTask().execute(mFileManager.getCurrentPath());
    }

    private class LoadFileTask extends AsyncTask<String, Void, ArrayList<File>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected ArrayList<File> doInBackground(String... params) {
            Log.d(TAG, "doInBackground: " + params[0]);
            return mFileManager.getListFile(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);
            mFileAdapter.setFileList(files);
        }

    }


}
