package com.duy.pascal.frontend.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 08-Apr-17.
 */

public class DialogFragmentErrorMsg extends AppCompatDialogFragment {
    public static final String TAG = DialogFragmentErrorMsg.class.getSimpleName();

    public static DialogFragmentErrorMsg newInstance(CharSequence lineError, CharSequence msg) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence("line", lineError);
        bundle.putCharSequence("msg", msg);
        DialogFragmentErrorMsg dialogFragmentErrorMsg = new DialogFragmentErrorMsg();
        dialogFragmentErrorMsg.setArguments(bundle);
        return dialogFragmentErrorMsg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_show_error, container, false);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtLine = (TextView) view.findViewById(R.id.txt_line);
        txtLine.setText(getArguments().getCharSequence("line"));

        TextView txtMsg = (TextView) view.findViewById(R.id.txt_message);
        txtMsg.setText(getArguments().getCharSequence("msg"));

        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.compile_error));
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

}
