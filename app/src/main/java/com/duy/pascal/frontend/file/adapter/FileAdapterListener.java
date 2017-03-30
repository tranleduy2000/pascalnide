package com.duy.pascal.frontend.file.adapter;

import android.view.View;

/**
 * Created by Duy on 30-Mar-17.
 */

public interface FileAdapterListener {
    int ACTION_REMOVE = 0;
    int ACTION_CLICK = 1;
    int ACTION_LONG_CLICK = 2;

    void onItemClick(View v, String nameOfFle, int action);

    void onRemoveClick(View view, String nameOfFile, int action);
}
