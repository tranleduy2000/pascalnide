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

package com.duy.pascal.frontend.code_editor.findreplace;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.setting.PascalPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Duy on 29-Apr-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class DialogFind extends AppCompatDialogFragment {

    public static final int ACTION_FIND = 1;
    public static final int ACTION_FIND_REPLAC = 2;
    @BindView(R.id.ckb_regex)
    CheckBox ckbRegex;
    @BindView(R.id.ckb_match_key)
    CheckBox ckbMatch;
    @BindView(R.id.txt_find)
    EditText editFind;
    @BindView(R.id.ckb_word_only)
    CheckBox ckbWordOnly;
    private PascalPreferences mPascalPreferences;
    @Nullable

    private FindAndReplaceListener findAndReplaceListener;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            findAndReplaceListener = (FindAndReplaceListener) getActivity();
        } catch (Exception ignored) {

        }
        mPascalPreferences = new PascalPreferences(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_find_and_replace, container, false);
        unbinder = ButterKnife.bind(this, view);
        editFind.setText(mPascalPreferences.getString(PascalPreferences.LAST_FIND));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_replace)
    public void onSubmit() {
        if (findAndReplaceListener != null) {
            findAndReplaceListener.onFind(
                    editFind.getText().toString(),
                    ckbRegex.isChecked(),
                    ckbWordOnly.isChecked(),
                    ckbMatch.isChecked());
        }
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel(View v) {
        dismiss();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        mPascalPreferences.put(PascalPreferences.LAST_FIND, editFind.getText().toString());
    }

    @Nullable
    public FindAndReplaceListener getFindAndReplaceListener() {
        return findAndReplaceListener;
    }

    public void setFindAndReplaceListener(@Nullable FindAndReplaceListener findAndReplaceListener) {
        this.findAndReplaceListener = findAndReplaceListener;
    }


}
