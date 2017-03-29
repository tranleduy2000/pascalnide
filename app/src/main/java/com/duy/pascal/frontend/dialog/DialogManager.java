package com.duy.pascal.frontend.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 29-Mar-17.
 */

public class DialogManager {
    public static android.support.v7.app.AlertDialog createDialog(final Activity activity, String title, String msg) {
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
}
