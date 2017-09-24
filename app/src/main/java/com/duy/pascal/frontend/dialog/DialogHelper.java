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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.code.ExceptionManager;
import com.duy.pascal.frontend.editor.EditorActivity;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;

/**
 * Created by Duy on 9/24/2017.
 */

public class DialogHelper {
    public static Dialog createFinishDialog(final Activity context, CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        context.finish();
                    }
                });
        return builder.create();
    }

    public static Dialog createFinishDialog(final Activity context, int title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        context.finish();
                    }
                });
        return builder.create();
    }

    public static AlertDialog createMsgDialog(Context context, CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

    @SuppressWarnings("ConstantConditions")
    public static AlertDialog showErrorDialog(final EditorActivity context, final Exception e) {
        ExceptionManager exceptionManager = new ExceptionManager(context);
        //create builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_show_error);
        //show dialog
        final AlertDialog dialog = builder.create();
        dialog.show();

        //set title and message for dialog
        TextView txtTitle = dialog.findViewById(R.id.txt_name);
        txtTitle.setText(context.getString(R.string.compile_error));
        TextView txtMsg = dialog.findViewById(R.id.txt_message);
        txtMsg.setText(exceptionManager.getMessage(e));

        //set event for button
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        if (e instanceof ParsingException) {
            if (((ParsingException) e).getCanAutoFix()) {
                //set event for button Auto fix
                dialog.findViewById(R.id.btn_auto_fix).setVisibility(View.VISIBLE);
                if (e instanceof UnknownIdentifierException) {
                    final RadioGroup container = dialog.findViewById(R.id.container_define);
                    container.setVisibility(View.VISIBLE);
                    dialog.findViewById(R.id.btn_auto_fix).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int checkedRadioButtonId = container.getCheckedRadioButtonId();
                            switch (checkedRadioButtonId) {
                                case R.id.rad_var:
                                    ((UnknownIdentifierException) e).setFitType(UnknownIdentifierException.DefineType.DECLARE_VAR);
                                    break;
                                case R.id.rad_fun:
                                    ((UnknownIdentifierException) e).setFitType(UnknownIdentifierException.DefineType.DECLARE_FUNCTION);
                                    break;
                                case R.id.rad_const:
                                    ((UnknownIdentifierException) e).setFitType(UnknownIdentifierException.DefineType.DECLARE_CONST);
                                    break;
                            }
                            context.autoFix((ParsingException) e);
                            dialog.cancel();
                        }
                    });
                } else {
                    dialog.findViewById(R.id.btn_auto_fix).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.autoFix((ParsingException) e);
                            dialog.cancel();
                        }
                    });

                }
            }
        }
        return dialog;
    }
}
