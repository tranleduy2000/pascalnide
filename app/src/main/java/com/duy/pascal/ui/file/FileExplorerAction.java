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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.duy.pascal.ui.BuildConfig;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.common.utils.UIUtils;
import com.duy.pascal.ui.file.listener.OnClipboardPasteFinishListener;
import com.duy.pascal.ui.file.util.FileUtils;
import com.duy.pascal.ui.file.util.MimeTypes;
import com.duy.pascal.ui.file.util.OnCheckedChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */

public class FileExplorerAction implements OnCheckedChangeListener, ActionMode.Callback, ShareActionProvider.OnShareTargetSelectedListener {
    private final FileExplorerView mView;
    private final Context context;
    private final FileClipboard fileClipboard;
    private final ExplorerContext mExplorerContext;
    private ActionMode mActionMode;
    private List<File> checkedList = new ArrayList<>();
    private ShareActionProvider mShareActionProvider;
    private MenuItem renameMenu;
    private MenuItem mShareMenu;
    @Nullable
    private Dialog mDialog;

    public FileExplorerAction(Context context, FileExplorerView view, FileClipboard fileClipboard, ExplorerContext explorerContext) {
        this.mView = view;
        this.context = context;
        this.fileClipboard = fileClipboard;
        this.mExplorerContext = explorerContext;
    }

    @Override
    public void onCheckedChanged(File file, int position, boolean checked) {
        if (checked) {
            checkedList.add(file);
        } else {
            checkedList.remove(file);
        }
    }

    @Override
    public void onCheckedChanged(int checkedCount) {
        if (checkedCount > 0) {
            if (mActionMode == null) {
                mActionMode = mView.startActionMode(this);
            }
            mActionMode.setTitle(context.getString(R.string.selected_x_items, checkedCount));
        } else {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        menu.add(0, R.id.select_all, 0, R.string.select_all).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, R.id.cut, 0, R.string.cut).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(0, R.id.copy, 0, R.string.copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        MenuItem pasteMenu = menu.add(0, R.id.paste, 0, R.string.paste);
        pasteMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        pasteMenu.setEnabled(fileClipboard.canPaste());

        renameMenu = menu.add(0, R.id.rename, 0, R.string.rename);
        renameMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        mShareMenu = menu.add(0, R.id.share, 0, R.string.share);
        mShareMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        mShareActionProvider = new ShareActionProvider(context);
        mShareActionProvider.setOnShareTargetSelectedListener(this);
        MenuItemCompat.setActionProvider(mShareMenu, mShareActionProvider);

        menu.add(0, R.id.delete, 0, R.string.delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        mShareMenu.setEnabled(canShare());
        renameMenu.setEnabled(checkedList.size() == 1);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.select_all) {
            if (!item.isChecked()) {
                mView.setSelectAll(true);
                item.setChecked(true);
                item.setTitle(R.string.cancel_select_all);
            } else {
                mView.setSelectAll(false);
            }
        } else if (id == R.id.copy && !checkedList.isEmpty()) {
            fileClipboard.setData(true, checkedList);
            destroyActionMode();
        } else if (id == R.id.cut && !checkedList.isEmpty()) {
            fileClipboard.setData(false, checkedList);
            destroyActionMode();
        } else if (id == R.id.paste) {
            destroyActionMode();
            fileClipboard.paste(context, mExplorerContext.getCurrentDirectory(), new OnClipboardPasteFinishListener() {
                @Override
                public void onFinish(int count, String error) {
                    fileClipboard.showPasteResult(context, count, error);
                }
            });
        } else if (id == R.id.rename) {
            doRenameAction();
        } else if (id == R.id.share) {
            shareFile();
        } else if (id == R.id.delete) {
            doDeleteAction();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mShareActionProvider.setOnShareTargetSelectedListener(null);
        mShareActionProvider = null;
        checkedList.clear();
        mView.setSelectAll(false);
        renameMenu = null;
        mShareMenu = null;
        mActionMode = null;
    }

    public void destroy() {
        if (mDialog != null) mDialog.cancel();
        destroyActionMode();
    }

    private void destroyActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    private boolean canShare() {
        for (File file : checkedList) {
            if (!(file instanceof File) || !file.isFile())
                return false;
        }
        return true;
    }

    private void doRenameAction() {
        if (checkedList.size() != 1)
            return;

        final File file = checkedList.get(0);
        UIUtils.showInputDialog(context, R.string.rename, 0, file.getName(), 0, new UIUtils.OnShowInputCallback() {
            @Override
            public void onConfirm(CharSequence input) {
                if (TextUtils.isEmpty(input)) {
                    return;
                }
                if (file.getName().equals(input)) {
                    destroyActionMode();
                    return;
                }
                File dest = new File(file.getParentFile(), input.toString());
                boolean result = file.renameTo(dest);
                if (!result) {
                    UIUtils.toast(context, R.string.rename_fail);
                    return;
                }
                mView.refresh();
                destroyActionMode();
            }
        });
    }

    private void shareFile() {
        if (checkedList.isEmpty() || mShareActionProvider == null)
            return;
        try {
            Intent shareIntent = new Intent();
            if (checkedList.size() == 1) {
                File file = new File(checkedList.get(0).getPath());
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType(MimeTypes.getInstance().getMimeType(file.getPath()));

                Uri fileUri;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                } else {
                    fileUri = Uri.fromFile(file);
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

                ArrayList<Uri> streams = new ArrayList<>();
                for (File file : checkedList) {
                    if (!(file instanceof File)) {
                        throw new ExplorerException(context.getString(R.string.can_not_share_x, file + " isn't File"));
                    }
                    File File = new File(file.getPath());
                    Uri fileUri;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", File);
                    } else {
                        fileUri = Uri.fromFile(File);
                    }
                    streams.add(fileUri);
                }

                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, streams);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            mShareActionProvider.setShareIntent(shareIntent);
        } catch (Exception e) {
        }
    }

    private void doDeleteAction() {
        for (File file : checkedList) {
            FileUtils.deleteRecursive(file);
        }
        mView.refresh();
        destroyActionMode();
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
        destroyActionMode();
        return false;
    }

    public void doCreateFolder(@Nullable final FileActionCallback callback) {
        if (mDialog != null) mDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.new_folder);
        builder.setView(R.layout.dialog_input);
        mDialog = builder.create();
        mDialog.show();
        final EditText editText = mDialog.findViewById(R.id.edit_input);
        TextInputLayout textInputLayout = mDialog.findViewById(R.id.hint);
        textInputLayout.setHint(context.getString(R.string.enter_new_folder_name));

        Button btnOK = mDialog.findViewById(R.id.btn_ok);
        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    editText.setError(context.getString(R.string.enter_new_file_name));
                    return;
                }
                final File folder = new File(mExplorerContext.getCurrentDirectory(), input);
                boolean result = folder.mkdirs();
                if (!result) {
                    UIUtils.toast(context, R.string.can_not_create_folder);
                    return;
                }
                mView.refresh();
                if (callback != null) {
                    callback.onSelectFile(new File(folder.getPath()));
                }
                destroyActionMode();
                mDialog.cancel();
            }
        });
    }

    public void showDialogCreateFile(@Nullable final FileActionCallback mCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.new_file);
        builder.setView(R.layout.dialog_new_file);

        mDialog = builder.create();
        mDialog.show();

        final EditText editText = mDialog.findViewById(R.id.edit_input);
        Button btnOK = mDialog.findViewById(R.id.btn_ok);
        Button btnCancel = mDialog.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (TextUtils.isEmpty(fileName)) {
                    editText.setError(context.getString(R.string.enter_new_file_name));
                    return;
                }

                RadioButton checkBoxPas = mDialog.findViewById(R.id.rad_pas);
                RadioButton checkBoxInp = mDialog.findViewById(R.id.rad_inp);

                if (checkBoxInp != null && checkBoxInp.isChecked()) {
                    fileName += ".inp";
                } else if (checkBoxPas.isChecked()) {
                    fileName += ".pas";
                }

                final File file = new File(mExplorerContext.getCurrentDirectory(), fileName);
                boolean result = new FileManager(context).createNewFile(file.getPath()) != null;
                if (!result) {
                    editText.setError(context.getString(R.string.can_not_create_file));
                    return;
                }
                mView.refresh();
                if (mCallback != null) {
                    mCallback.onSelectFile(new File(file.getPath()));
                }
                destroyActionMode();
                mDialog.cancel();
            }
        });
    }
}
