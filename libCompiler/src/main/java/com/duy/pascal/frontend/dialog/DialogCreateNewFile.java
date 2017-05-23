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

package com.duy.pascal.frontend.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code_editor.completion.Template;
import com.duy.pascal.frontend.file.ApplicationFileManager;

import java.io.File;

/**
 * Created by Duy on 10-Apr-17.
 */

public class DialogCreateNewFile extends AppCompatDialogFragment {
    public static final String TAG = DialogCreateNewFile.class.getSimpleName();
    private EditText mEditFileName;
    private Button btnOK, btnCancel;
    @Nullable
    private OnCreateNewFileListener listener;
    private RadioButton checkBoxPas;
    private RadioButton checkBoxInp;
    private RadioButton checkBoxUnit;
    private ApplicationFileManager mFileManager;

    public static DialogCreateNewFile getInstance() {
        return new DialogCreateNewFile();
    }

    public void setListener(@Nullable OnCreateNewFileListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            listener = (OnCreateNewFileListener) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mFileManager = new ApplicationFileManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_file, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEditFileName = (EditText) view.findViewById(R.id.edit_file_name);
        mEditFileName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    File file = doCreateFile();
                    if (listener != null && file != null) {
                        listener.onFileCreated(file);
                        listener.onCancel();
                    }
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        btnOK = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onCancel();
                dismiss();
            }
        });
        checkBoxPas = (RadioButton) view.findViewById(R.id.rad_pas);
        checkBoxInp = (RadioButton) view.findViewById(R.id.rad_inp);
        checkBoxUnit = (RadioButton) view.findViewById(R.id.rad_unit);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = doCreateFile();
                if (listener != null && file != null) {

                    listener.onFileCreated(file);
                    listener.onCancel();
                    dismiss();
                }
            }
        });

    }

    private File doCreateFile() {
        //get string path of in edit text
        String fileName = mEditFileName.getText().toString();
        if (fileName.isEmpty()) {
            mEditFileName.setError(getString(R.string.enter_new_file_name));
            return null;
        }
        if (checkBoxInp.isChecked() && !fileName.contains(".")) {
            fileName += ".inp";
        } else if (checkBoxPas.isChecked() && !fileName.contains(".")) {
            fileName += ".pas";
        }
        File file = new File(ApplicationFileManager.getApplicationPath() + fileName);
        if (file.exists()) {
            mEditFileName.setError(getString(R.string.file_exist));
            return null;
        }
        //create new file
        String filePath = mFileManager.createNewFile(ApplicationFileManager.getApplicationPath() + fileName);
        file = new File(filePath);
        if (checkBoxPas.isChecked()) {
            mFileManager.saveFile(file,
                    Template.createProgramTemplate(file.getName()));
        } else if (checkBoxUnit.isChecked()) {
            mFileManager.saveFile(file,
                    Template.createUnitTemplate(file.getName()));
        }
        return file;
    }

    public interface OnCreateNewFileListener {
        void onFileCreated(File file);

        void onCancel();
    }

}
