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

public class DialogFindAndReplace extends AppCompatDialogFragment {

    public static final int ACTION_FIND = 1;
    public static final int ACTION_FIND_REPLAC = 2;
    public static final String TAG = "DialogFindAndReplace";
    @BindView(R.id.ckb_regex)
    CheckBox ckbRegex;
    @BindView(R.id.ckb_match_key)
    CheckBox ckbMatch;
    @BindView(R.id.txt_find)
    EditText editFind;
    @BindView(R.id.edit_replace)
    EditText editReplace;
    private PascalPreferences mPascalPreferences;
    @Nullable

    private FindAndReplaceListener findAndReplaceListener;
    private Unbinder unbinder;
    private FindAndReplaceListener listener;

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
            findAndReplaceListener.onFindAndReplace(
                    editFind.getText().toString(),
                    editReplace.getText().toString(),
                    ckbRegex.isChecked(),
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
        mPascalPreferences.put(PascalPreferences.LAST_REPLACE, editReplace.getText().toString());
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
