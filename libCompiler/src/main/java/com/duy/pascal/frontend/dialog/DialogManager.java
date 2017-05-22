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
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 29-Mar-17.
 */

public class DialogManager {
    public static android.support.v7.app.AlertDialog createFinishDialog(final Activity activity,
                                                                        CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });
        return builder.create();

    }

    public static android.support.v7.app.AlertDialog createMsgDialog(final Activity activity,
                                                                     CharSequence title, CharSequence msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();

    }

    public static android.support.v7.app.AlertDialog createFinishDialog(final Activity activity,
                                                                        CharSequence title, CharSequence msg,
                                                                        int resourceIcon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(resourceIcon);
        builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.finish();
            }
        });
        return builder.create();

    }

    public static AlertDialog createDialogReportBug(final Activity activity, final String code) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.report_bug).setView(R.layout.report_bug_dialog).setIcon(R.drawable.ic_bug_report_white_24dp);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editTitle = (EditText) alertDialog.findViewById(R.id.edit_title);
        final EditText editContent = (EditText) alertDialog.findViewById(R.id.edit_content);
        final Button btnSend = (Button) alertDialog.findViewById(R.id.btn_email);
        final EditText editExpect = (EditText) alertDialog.findViewById(R.id.edit_expect);
        assert btnSend != null;
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBug();
                alertDialog.cancel();
            }

            private void sendBug() {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"tranleduy1233@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Report bug for PASCAL NIDE: " + editTitle.getText().toString());

                String content = "Cause: \n" + editContent.getText().toString() + "\n" +
                        "Expect:  " + editExpect.getText().toString() + "\n" + "Code:\n" + code;

                i.putExtra(Intent.EXTRA_TEXT, content);

                try {
                    activity.startActivity(Intent.createChooser(i, activity.getString(R.string.send_mail)));
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(activity, R.string.no_mail_clients, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return alertDialog;
    }

    public void onDestroy() {

    }
}
