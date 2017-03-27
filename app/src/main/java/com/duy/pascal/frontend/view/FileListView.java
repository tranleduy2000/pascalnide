package com.duy.pascal.frontend.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.util.AttributeSet;

import com.duy.pascal.frontend.file.FragmentSelectFile;

/**
 * This view will be inside {@link FragmentSelectFile}
 * Created by Duy on 11-Feb-17.
 */
public class FileListView extends ListViewCompat {
    private static final String TAG = FileListView.class.getSimpleName();
    int lastEvent = -1;
    boolean isLastEventIntercepted = false;
    private float xDistance, yDistance, lastX, lastY;

    public FileListView(Context context) {
        super(context);
        setup(context);

    }

    public FileListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public FileListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
    }

    /**
     * set up recycle view, include layout manager, adapter , listener
     *
     * @param context - android context
     */
    private void setup(Context context) {

    }

}
