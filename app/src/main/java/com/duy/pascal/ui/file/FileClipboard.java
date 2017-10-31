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

package com.duy.pascal.ui.file;

import android.content.Context;
import android.text.TextUtils;

import com.duy.pascal.ui.common.app.ProgressDialog;
import com.duy.pascal.ui.common.task.JecAsyncTask;
import com.duy.pascal.ui.common.task.TaskResult;
import com.duy.pascal.ui.common.utils.UIUtils;
import com.duy.pascal.ui.file.listener.OnClipboardDataChangedListener;
import com.duy.pascal.ui.file.listener.OnClipboardPasteFinishListener;
import com.duy.pascal.ui.file.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FileClipboard {
    private List<File> mClipList = new ArrayList<>();
    private boolean isCopy;
    private OnClipboardDataChangedListener mOnClipboardDataChangedListener;

    public boolean canPaste() {
        return !mClipList.isEmpty();
    }

    public void setData(boolean isCopy, List<File> data) {
        this.isCopy = isCopy;
        mClipList.clear();
        mClipList.addAll(data);
        if (mOnClipboardDataChangedListener != null) {
            mOnClipboardDataChangedListener.onClipboardDataChanged();
        }
    }

    public void paste(Context context, File currentDirectory, OnClipboardPasteFinishListener listener) {
        if (!canPaste())
            return;

        ProgressDialog dlg = new ProgressDialog(context);
        PasteTask task = new PasteTask(listener, mClipList, isCopy);
        task.setProgress(dlg);
        task.execute(currentDirectory);
    }

    public void showPasteResult(Context context, int count, String error) {
        if (TextUtils.isEmpty(error)) {
            UIUtils.toast(context, com.jecelyin.android.file_explorer.R.string.x_items_completed, count);
        } else {
            UIUtils.toast(context, com.jecelyin.android.file_explorer.R.string.x_items_completed_and_error_x, count, error);
        }
    }

    public void setOnClipboardDataChangedListener(OnClipboardDataChangedListener onClipboardDataChangedListener) {
        this.mOnClipboardDataChangedListener = onClipboardDataChangedListener;
    }

    private static class PasteTask extends JecAsyncTask<File, File, Integer> {
        private final OnClipboardPasteFinishListener mListener;
        private StringBuilder errorMsg = new StringBuilder();
        private List<File> mClipList;
        private boolean isCopy = false;

        PasteTask(OnClipboardPasteFinishListener listener, List<File> mClipList, boolean isCopy) {
            this.mListener = listener;
            this.mClipList = mClipList;
            this.isCopy = isCopy;
        }

        @Override
        protected void onProgressUpdate(File... values) {
            getProgress().setMessage(values[0].getPath());
        }

        @Override
        protected void onRun(TaskResult<Integer> taskResult, File... params) throws Exception {
            File currentDirectory = params[0];
            int count = 0;
            for (File file : mClipList) {
                publishProgress(file);
                try {
                    if (file.isDirectory()) {
                        FileUtils.copyDirectory(file, currentDirectory, !isCopy);
                    } else {
                        FileUtils.copyFile(file, new File(currentDirectory, file.getName()), !isCopy);
                    }
                    count++;
                } catch (Exception e) {
                    errorMsg.append(e.getMessage()).append("\n");
                }
            }
            mClipList.clear();
            taskResult.setResult(count);
        }

        @Override
        protected void onSuccess(Integer integer) {
            if (mListener != null) {
                mListener.onFinish(integer, errorMsg.toString());
            }
        }

        @Override
        protected void onError(Exception e) {
            if (mListener != null) {
                mListener.onFinish(0, e.getMessage());
            }
        }
    }
}
